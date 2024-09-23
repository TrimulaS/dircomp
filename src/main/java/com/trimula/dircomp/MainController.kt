package com.trimula.dircomp

import com.trimula.dircomp.dataprocessing.Log
import com.trimula.dircomp.dataprocessing.TreeItemTraverse
import com.trimula.dircomp.model.Comparator
import com.trimula.dircomp.model.DataTableView
import com.trimula.dircomp.model.DirectoryAnalysis
import com.trimula.dircomp.model.FileItem
import com.trimula.dircomp.view.ContentMenu
import com.trimula.dircomp.view.UiTreeView
import com.trimula.dircomp.view.tiny.DirectoryStatusBar
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


/**
 *  Nodes : show()  hide()
 *
 */
class MainController {

    @FXML    lateinit var vbMainInterface : VBox
    @FXML    lateinit var cbDir1Path: ComboBox<String>
    @FXML    lateinit var cbDir2Path: ComboBox<String>

    @FXML    lateinit var treeViewDir1:     TreeView<FileItem>
    @FXML    lateinit var treeViewDir2:     TreeView<FileItem>
    @FXML    lateinit var tableViewDir1:    TableView<FileItem>
    @FXML    lateinit var tableViewDir2:    TableView<FileItem>

    @FXML    lateinit var vb1TableView:VBox
    @FXML    lateinit var vb2TableView:VBox
    @FXML    lateinit var vbTreeView1:VBox
    @FXML    lateinit var vb2TreeView:VBox


    @FXML    lateinit var taSelectedItemProperties: TextArea
    @FXML    lateinit var taStatus: TextArea
    @FXML    lateinit var lProgress: Label
    @FXML    lateinit var progressBar: ProgressBar
    @FXML    lateinit var hBoxProgress: HBox

    @FXML    lateinit var cbSyncSelection: CheckBox
    @FXML    lateinit var cbScrollTo: CheckBox
    @FXML    lateinit var buttonCancelProcessing: Button

    // These button state should use to identify view type: Tree or Table
    @FXML    lateinit var tbDir1ViewTree:           ToggleButton
    @FXML    lateinit var tbDir1ViewTable:          ToggleButton
    @FXML    lateinit var tbDir2ViewTree:           ToggleButton
    @FXML    lateinit var tbDir2ViewTable:          ToggleButton
    @FXML    lateinit var tbDir2ViewAsMatchedTo1:   ToggleButton

    @FXML    lateinit var hb1MatchTypeButtonGroup: HBox
    @FXML    lateinit var tb1All:       ToggleButton
    @FXML    lateinit var tb1FullMatch: ToggleButton
    @FXML    lateinit var tb1Similar:   ToggleButton
    @FXML    lateinit var tb1Suspect:   ToggleButton
    @FXML    lateinit var tb1Unique:    ToggleButton

    @FXML    lateinit var tb1DirAndFile:    ToggleButton
    @FXML    lateinit var tb1DirOnly:       ToggleButton
    @FXML    lateinit var tb1FileOnly:      ToggleButton




    @FXML    lateinit var hb2MatchTypeButtonGroup: HBox
    @FXML    lateinit var tb2All:       ToggleButton
    @FXML    lateinit var tb2FullMatch: ToggleButton
    @FXML    lateinit var tb2Similar:   ToggleButton
    @FXML    lateinit var tb2Suspect:   ToggleButton
    @FXML    lateinit var tb2Unique:    ToggleButton

    @FXML    lateinit var tb2DirAndFile:ToggleButton
    @FXML    lateinit var tb2DirOnly:   ToggleButton
    @FXML    lateinit var tb2FileOnly:  ToggleButton

    @FXML    lateinit var l1Total              : Label
    @FXML    lateinit var l1TotalAll           : Label
    @FXML    lateinit var l1Directories        : Label
    @FXML    lateinit var l1DirectoriesAll     : Label
    @FXML    lateinit var l1Files              : Label
    @FXML    lateinit var l1FilesAll           : Label

    @FXML    lateinit var l2Total              : Label
    @FXML    lateinit var l2TotalAll           : Label
    @FXML    lateinit var l2Directories        : Label
    @FXML    lateinit var l2DirectoriesAll     : Label
    @FXML    lateinit var l2Files              : Label
    @FXML    lateinit var l2FilesAll           : Label

    @FXML    private lateinit var ivSettings: ImageView
    @FXML    private lateinit var tpSettings: TabPane


    @FXML    private lateinit var cbTestMode:CheckBox

    private val progressBarTextDuringCompare = "Compare in progress: "

    private var directory1: File? = null
    private var directory2: File? = null
    private lateinit var comparator: Comparator

    private lateinit var dir1StatusBar : DirectoryStatusBar
    private lateinit var dir2StatusBar : DirectoryStatusBar

    private lateinit var tg1DirView:    ToggleGroup
    private lateinit var tg2DirView:    ToggleGroup
    private lateinit var tg1Type:       ToggleGroup
    private lateinit var tg2Type:       ToggleGroup
    private lateinit var tg1Filter:     ToggleGroup
    private lateinit var tg2Filter:     ToggleGroup

//    private  val tgs1Filter = ToggleGroupSingleStaySelected (tb1All, tb1FullMatch, tb1Similar, tb1Suspect, tb1Unique)
//    private  val tgs2Filter = ToggleGroupSingleStaySelected (tb2All, tb2FullMatch, tb2Similar, tb2Suspect, tb2Unique)
//    private lateinit var tgs1Filter : ToggleGroupSingleStaySelected
//    private lateinit var tgs2Filter : ToggleGroupSingleStaySelected


    private val isProcessing = AtomicBoolean(false)
    private var isSyncing = false


    @FXML
    private fun initialize() {
        Log.enableJavaFX()

        hide( tpSettings )
        progressBarShow(false)
        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS

//        tgs1Filter = ToggleGroupSingleStaySelected (tb1All, tb1FullMatch, tb1Similar, tb1Suspect, tb1Unique)
//        tgs2Filter = ToggleGroupSingleStaySelected (tb2All, tb2FullMatch, tb2Similar, tb2Suspect, tb2Unique)

        // Инициализация группы
        tg1DirView = ToggleGroup()
        tg2DirView = ToggleGroup()
        tg1Filter = ToggleGroup()
        tg2Filter = ToggleGroup()
        tg1Type = ToggleGroup()
        tg2Type = ToggleGroup()



        // Добавление кнопок в группу -
        tbDir1ViewTree.toggleGroup      = tg1DirView
        tbDir1ViewTable.toggleGroup     = tg1DirView
        tbDir1ViewTree.isSelected =true

        tbDir2ViewTree.toggleGroup                  = tg2DirView
        tbDir2ViewTable.toggleGroup                 = tg2DirView
        tbDir2ViewAsMatchedTo1.toggleGroup          = tg2DirView
        tbDir2ViewTree.isSelected = true


        tb1All.toggleGroup          = tg1Filter
        tb1FullMatch.toggleGroup    = tg1Filter
        tb1Similar.toggleGroup      = tg1Filter
        tb1Suspect.toggleGroup      = tg1Filter
        tb1Unique.toggleGroup       = tg1Filter
        tb1All.isSelected = true


        tb2All.toggleGroup = tg2Filter
        tb2FullMatch.toggleGroup = tg2Filter
        tb2Similar.toggleGroup = tg2Filter
        tb2Suspect.toggleGroup = tg2Filter
        tb2Unique.toggleGroup = tg2Filter
        tb2All.isSelected = true

        // labels in status bar: update in Comparator.processDirectories and after filtering
        dir1StatusBar = DirectoryStatusBar(l1Files,l1FilesAll,l1Directories,l1DirectoriesAll, l1Total,l1TotalAll)
        dir2StatusBar = DirectoryStatusBar(l2Files,l2FilesAll,l2Directories,l2DirectoriesAll, l2Total,l2TotalAll)

        tb1DirAndFile.toggleGroup       = tg1Type
        tb1DirOnly.toggleGroup          = tg1Type
        tb1FileOnly.toggleGroup         = tg1Type
        tb1DirAndFile.isSelected = true

        tb2DirAndFile.toggleGroup       = tg2Type
        tb2DirOnly.toggleGroup          = tg2Type
        tb2FileOnly.toggleGroup         = tg2Type
        tb2DirAndFile.isSelected = true

        comparator = Comparator()
        dir1StatusBar.reset()
        dir2StatusBar.reset()


        //TableView Configuration
//        tableViewDir1.selectionModel.selectionMode = SelectionMode.MULTIPLE
//        tableViewDir2.selectionModel.selectionMode = SelectionMode.MULTIPLE
        DataTableView.setupTableView(tableViewDir1)
        DataTableView.setupTableView(tableViewDir2)

        //TreeView  setup
        treeViewDir1.selectionModel.selectionMode = SelectionMode.MULTIPLE
        treeViewDir2.selectionModel.selectionMode = SelectionMode.MULTIPLE
        ContentMenu.addToTreeView(treeViewDir1)
        ContentMenu.addToTreeView(treeViewDir2)

        // Switch to TreeView
        dir1ViewChange()
        if(!vbTreeView1.isVisible) dir1ViewChange()
        dir2ViewChange()
        if(vb2TreeView.isVisible) dir2ViewChange()       //Switch to table view

        //Setup for testing
        directory1 = File("C:\\tmp\\Dir1")      //"c:\\Literature")     //"C:\\tmp\\Dir1")  //"C:\\Dist\\IntelliJ") //"c:\\Literature")     // //  //("D:\\Dist\\IntelliJ\\GBTS_Exp41 migrate to StrTab")           //
        cbDir1Path.value = directory1?.absolutePath
        directory2 = File("C:\\tmp\\Dir2")      //"c:\\Literature")     //"C:\\tmp\\Dir2")   //"C:\\Dist\\IntelliJ") //"c:\\Literature")     //)   //("D:\\Dist\\IntelliJ\\GBTS_Exp")     //("c:\\Inst")
        cbDir2Path.value = directory2?.absolutePath

        setupListeners()

        comparator = Comparator()
        // Listeners to update progress during comparison:
        comparator.setBeforeDirectoryParseListener {
            lProgress.text = "Starting parsing directories..  "
            progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS
        }
        comparator.setBeforeCompareListener {
            lProgress.text = "Compare start..  "
        }
        comparator.setCompareProgressListener { progress, txt ->
            progressBar.progress = progress
            lProgress.text = "$progressBarTextDuringCompare:  $txt  "
        }

        Log.appendTextTimed("Comparator initialized")

    }

    private fun initClear(){
        Log.clear()
        taStatus.text = ""
        taSelectedItemProperties.text = ""
        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS;
    }


    @FXML
    fun selectDirectory1() {
        val path = DirectoryChooser().showDialog(null)
        if(path!=null){
            directory1 = path
            cbDir1Path.value = directory1?.absolutePath
        }
    }
    @FXML
    fun selectDirectory2() {
        val path = DirectoryChooser().showDialog(null)
        if(path!=null){
            directory2 = path
            cbDir2Path.value = directory2?.absolutePath
        }
    }

    @FXML
    fun onCompareClicked() {
        initClear()
        directory1 = File(cbDir1Path.value)
        directory2 = File(cbDir2Path.value)

        if (directory1?.exists() != true || directory2?.exists() != true)  {
            taStatus.appendText("Both directories need to be selected and exists!\n")
            return
        }


        progressBarShow(true)
        // Atomic variable to cancel processing
        isProcessing.set(true)

        // Запуск потоков для анализа директорий
        thread {
            comparator.processDirectories(directory1,directory2)

            // После завершения анализа – заполнение TreeView
            if (isProcessing.get()) {
                Platform.runLater {

                    comparator.fillAllDir1(treeViewDir1)     //Only in JavaFX UI Thread
                    comparator.fillAllDir2(treeViewDir2)
                    //hBoxProgress.isVisible = false
                    progressBarShow(false)
                    dir1StatusBar.update(comparator.da1.statistic.directories, comparator.da1.statistic.files)
                    dir2StatusBar.update(comparator.da2.statistic.directories, comparator.da2.statistic.files)
                }
            }
        }

    }


    @FXML
    fun onCancelProcessing(/*actionEvent: ActionEvent*/) {
        // Прерывание обработки
        isProcessing.set(false)
        //hBoxProgress.isVisible = false
        progressBarShow(false)
        Log.appendTextTimed("Processing cancelled by user.\n")

    }
    private fun analyzeDirectory(directory: File): List<File> {
        // Пример анализа – рекурсивный сбор файлов и директорий
        return directory.walk().toList()
    }

    //-----------------------------------------------------------------------------------  Functions ------------------
    private fun setupListeners(){
        // Adding Logs
        Log.addListener (object : Log.LogListener {
            override fun onChange(logText: String) {
                taStatus.text = Log.get()

                taStatus.positionCaret(taStatus.text.length);
                taStatus.scrollTop = Double.MAX_VALUE;
            }

            override fun onBeforeClear(logText: String) {
//                println("Log is about to be cleared: $logText")
            }
        } )

        // Synchronize selection Path 1 <-> 2
        treeViewDir1.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    when{
                        tbDir2ViewTree.isSelected  -> syncSelection(selectedItem.value,  treeViewDir2)
                        tbDir2ViewTable.isSelected -> syncSelection(selectedItem.value, tableViewDir2)
                        tbDir2ViewAsMatchedTo1.isSelected -> syncProperties(selectedItem.value)
                    }
                }
            }
        }
        treeViewDir2.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    when{
                        tbDir1ViewTree.isSelected  -> syncSelection(selectedItem.value,  treeViewDir1)
                        tbDir1ViewTable.isSelected -> syncSelection(selectedItem.value, tableViewDir1)
                    }
                }
            }
        }
        tableViewDir1.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    if (tbDir2ViewAsMatchedTo1.isSelected) {
                        syncProperties(selectedItem)
                    }
                    else {
                        syncSelection(selectedItem,if (tbDir2ViewTree.isSelected) treeViewDir2 else tableViewDir2)
                    }

                }
            }
        }
        tableViewDir2.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    when{
                        tbDir1ViewTree.isSelected ->  syncSelection(selectedItem, treeViewDir1)
                        tbDir1ViewTable.isSelected -> syncSelection(selectedItem, tableViewDir1)
                    }
                }
            }
        }


    }



    private fun syncSelection(fileItem:FileItem,view: Any){
        taSelectedItemProperties.text = fileItem.toString()

        //Select same items in other path

        if(cbSyncSelection.isSelected){
            var isFirst = true
            //Log.appendText("sync Selection")
            when (view) {
                is TreeView<*> -> {
                    isSyncing = true
                    //Log.appendText("Target is TreeView selected: " + fileItem.name)


                    val treeView = view as? TreeView<FileItem> ?: return

                    treeView.selectionModel.clearSelection()

                    // Если список same не пустой, обрабатываем его
                    fileItem.same?.let { sameList ->
                        var isFirst = true

                        sameList.forEach { fi ->
                            val treeItemToSelect = TreeItemTraverse.findByValue(treeView.root, fi)

                            treeItemToSelect?.let {
                                treeView.selectionModel.select(treeItemToSelect)

                                // Прокрутка к первому элементу
                                if (isFirst && cbScrollTo.isSelected) {
                                    treeView.scrollTo(treeView.getRow(treeItemToSelect))
                                    isFirst = false
                                }
                            }
                        }
                    }
                }

                is TableView<*> -> {
                    isSyncing = true
                    val tableView = view as? TableView<FileItem>?: return
                    //Log.appendText("Target is TableView selected: " + fileItem.name)
                    tableView.selectionModel.clearSelection()

                    fileItem.same?.let { sameList ->
                        for (fi: FileItem in sameList){
                            tableView.items.forEachIndexed { index, fi ->
                                if ( fi.length() == fileItem.length() ) {
                                    tableView.selectionModel.select(index)
                                    if (isFirst && cbScrollTo.isSelected) {
                                        tableView.scrollTo(index)
                                        isFirst = false
                                    }
                                }
                            }
                        }
                    }
                }
            }
            isSyncing = false
        }


    }

        //Changed to FindBy Value
//    // Рекурсивная функция для поиска TreeItem по значению FileItem
//    private fun findTreeItem(treeItem: TreeItem<FileItem>, fi: FileItem): TreeItem<FileItem>? {
//        if (treeItem.value == fi) return treeItem
//
//        for (child in treeItem.children) {
//            val result = findTreeItem(child, fi)
//            if (result != null) {
//                return result
//            }
//        }
//        return null
//    }
    private fun progressBarShow(isVisible: Boolean){
            hBoxProgress.isVisible = isVisible

            hBoxProgress.isManaged = isVisible

//        if(isVisible) hBoxProgress.maxHeight = HBox.USE_COMPUTED_SIZE
//        else hBoxProgress.maxHeight = 0.0
    }

    @FXML fun tv1CollapseAll() =    UiTreeView.collapseAll(treeViewDir1)
    @FXML fun tv1ExpandAll() =      UiTreeView.expandAll(treeViewDir1)

    @FXML fun tv1CollapseLast() =   UiTreeView.collapseLast(treeViewDir1)
    @FXML fun tv1ExpandLast() =     UiTreeView.expandFirst(treeViewDir1)

//    @FXML fun tv1CollapseSelected() =  UiTreeView.collapseSelected(treeViewDir1)
//    @FXML fun tv1ExpandSelected() =  UiTreeView.expandSelected(treeViewDir1)

    @FXML fun tv2CollapseAll() =    UiTreeView.collapseAll(treeViewDir2)
    @FXML fun tv2ExpandAll() =      UiTreeView.expandAll(treeViewDir2)

    @FXML fun tv2CollapseLast() =   UiTreeView.collapseLast(treeViewDir2)
    @FXML fun tv2ExpandLast() =     UiTreeView.expandFirst(treeViewDir2)

//    @FXML fun tv2CollapseSelected() =  UiTreeView.collapseSelected(treeViewDir2)
//    @FXML fun tv2ExpandSelected() =  UiTreeView.expandSelected(treeViewDir2)



//    @FXML fun tb1DirAndFile()    { treeViewDir1.root = comparator.da1.root}   //comparator.da1.getfillAllDir1(treeViewDir1)}
//    @FXML fun tb2DirAndFile()    { treeViewDir2.root = comparator.da2.root }   // = comparator.fillAllDir2(treeViewDir2)
//
//    @FXML fun tb1DirOnly()       { treeViewDir1.root = comparator.da1.rootDirOnly }  // = comparator.fillDirOnly1(treeViewDir1)
//    @FXML fun tb2DirOnly()       { treeViewDir2.root = comparator.da2.rootDirOnly }  //  = comparator.fillDirOnly2(treeViewDir2)


    //--------------------------------------------------------------- Interface Configuration--------------------------
    @FXML
    fun dir1ViewChange() {

        when{
            tbDir1ViewTree.isSelected -> {                      // Tree
                hide ( vb1TableView )
//                hide ( tb1FileOnly )
                hide ( hb1MatchTypeButtonGroup )

                show ( vbTreeView1 )
            }
            tbDir1ViewTable.isSelected -> {                     // Table
                hide (vbTreeView1)

                show ( hb1MatchTypeButtonGroup )
                show ( vb1TableView )
//                show ( tb1FileOnly )
            }
        }
        filterDir1()
    }
    @FXML
    fun dir2ViewChange() {

        when{
            tbDir2ViewTree.isSelected -> {
                hide ( vb2TableView )
//                hide ( tb2FileOnly )
                hide ( hb2MatchTypeButtonGroup )

                show ( vb2TreeView )
            }
            tbDir2ViewTable.isSelected -> {
                hide ( vb2TreeView )

                show ( hb2MatchTypeButtonGroup )
                show ( vb2TableView )
//                show ( tb2FileOnly )
            }
            tbDir2ViewAsMatchedTo1.isSelected -> {
                hide ( vb2TreeView )
                hide ( hb2MatchTypeButtonGroup )

                show ( vb2TableView )
            }
        }
        filterDir2()

    }

    private fun hide(node: Node){
        node.isVisible = false
        node.isManaged = false
    }

    private fun show(node: Node){
        node.isVisible = true
        node.isManaged = true
    }

    //......................................................................................... Filters................


    // Mode where
    private fun syncProperties(fileItem:FileItem){
        val ds = DirectoryAnalysis.getStatisticFromObservableList(fileItem.same)
        dir2StatusBar.update(ds.directories,ds.files)
        tableViewDir2.items = fileItem.same
    }

    @FXML fun filterDir1(){
        filtering(
            comparator.da1,

            tbDir1ViewTree,
            tbDir1ViewTable,

            tb1All,
            tb1FullMatch,
            tb1Similar,
            tb1Suspect,
            tb1Unique,

            tb1DirAndFile,
            tb1DirOnly,
            tb1FileOnly,

            treeViewDir1,
            tableViewDir1,

            dir1StatusBar   )
    }
    @FXML fun filterDir2(){
        if(!tbDir2ViewAsMatchedTo1.isSelected)
        filtering(
            comparator.da2,

            tbDir2ViewTree,
            tbDir2ViewTable,

            tb2All,
            tb2FullMatch,
            tb2Similar,
            tb2Suspect,
            tb2Unique,

            tb2DirAndFile,
            tb2DirOnly,
            tb2FileOnly,

            treeViewDir2,
            tableViewDir2,

            dir2StatusBar   )
    }
    private fun filtering(
        directoryAnalysis: DirectoryAnalysis?,
        viewTree:ToggleButton,
        viewTable:ToggleButton,

        all:ToggleButton,
        fullMatch:ToggleButton,
        similar:ToggleButton,
        suspect:ToggleButton,
        unique:ToggleButton,

        dirAndFile:ToggleButton,
        dirOnly:ToggleButton,
        fileOnly:ToggleButton,

        treeView: TreeView<FileItem>,
        tableView: TableView<FileItem>,

        directoryStatusBar :DirectoryStatusBar

    ){
        directoryAnalysis?.let { da ->
            when{
                // Tree
                viewTree.isSelected -> {

                    when{
                        dirAndFile.isSelected    -> {
                            treeView.root = da.root
                            directoryStatusBar.update(da.statistic.directories, da.statistic.files)
                        }
                        dirOnly.isSelected       ->{
                            treeView.root = da.rootDirOnly
                            directoryStatusBar.updateFiltered(da.statisticDirOnly.directories, da.statisticDirOnly.files)
                        }
                        fileOnly.isSelected     ->{
                            treeView.root = da.rootSameFileOnly
                            directoryStatusBar.updateFiltered(da.statisticSameFileOnly.directories, da.statisticSameFileOnly.files)
                        }
                    }
                }

                // Table
                viewTable.isSelected -> {

                    //if no filter applied:
                    if(all.isSelected && dirAndFile.isSelected){
                        tableView.items = da.observableList
                        directoryStatusBar.update(da.statistic.directories, da.statistic.files)
                        return
                    }

                    // If filtering conditions chosen
                    val filterMatch = when {
                        all.isSelected       -> { _: FileItem -> true }
                        fullMatch.isSelected -> { fileItem: FileItem -> fileItem.same!!.isNotEmpty() }
                        similar.isSelected   -> { fileItem: FileItem -> fileItem.similar!!.isNotEmpty() }
                        suspect.isSelected   -> { fileItem: FileItem -> fileItem.isSuspected() }
                        unique.isSelected    -> { fileItem: FileItem -> fileItem.same!!.isEmpty() && fileItem.similar!!.isEmpty() }
                        else -> { _: FileItem -> true }
                    }

                    val filterType = when {
                        dirAndFile.isSelected    -> { _: FileItem -> true }
                        dirOnly.isSelected       -> { fileItem: FileItem -> fileItem.isDirectory }
                        fileOnly.isSelected      -> { fileItem: FileItem -> !fileItem.isDirectory }
                        else -> { _: FileItem -> true }
                    }

                    // Apply the combined filter
                    da.filteredList.setPredicate { fileItem -> filterMatch(fileItem) && filterType(fileItem) }

                    // Adding Sorting
                    tableView.items = da.filteredSortedList

                    // Check if binding already applied
                    if (da.filteredSortedList.comparatorProperty().isNull.value) {
                        // Если компаратор еще не привязан, привязываем его
                        da.filteredSortedList.comparatorProperty().bind(tableView.comparatorProperty())
                    }


                    //Show statistics
                    directoryStatusBar.updateFiltered(da.statisticFilteredList.directories, da.statisticFilteredList.files)

                    //                Log.appendText("filteredList to Table 1")
                }

                //else -> { Log.addError("Wrong ")}
            }


        } ?: run {
            Log.addError("DirectoryAnalysis, not defined")
        }


    }

    @FXML fun onSettingsClick(){
        if(tpSettings.isVisible){
            hide( tpSettings )
            show( vbMainInterface )
            ivSettings.image = Image(javaClass.getResource("/icons/icoGear.png").toExternalForm())
        }
        else{
            show( tpSettings )
            hide( vbMainInterface )
            ivSettings.image = Image(javaClass.getResource("/icons/icoBack.png").toExternalForm())
        }
    }

}






