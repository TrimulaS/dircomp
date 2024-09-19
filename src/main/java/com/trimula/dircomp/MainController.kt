package com.trimula.dircomp


import com.trimula.dircomp.dataprocessing.Log
import com.trimula.dircomp.dataprocessing.TreeItemTraverse
import com.trimula.dircomp.model.Comparator
import com.trimula.dircomp.model.DataTableView
import com.trimula.dircomp.model.FileItem
import com.trimula.dircomp.view.ContentMenu
import com.trimula.dircomp.view.UiTreeView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class MainController {

    @FXML    lateinit var comboBoxDirectory1: ComboBox<String>
    @FXML    lateinit var comboBoxDirectory2: ComboBox<String>

    @FXML    lateinit var treeViewDir1:     TreeView<FileItem>
    @FXML    lateinit var treeViewDir2:     TreeView<FileItem>
    @FXML    lateinit var tableViewDir1:    TableView<FileItem>
    @FXML    lateinit var tableViewDir2:    TableView<FileItem>

    @FXML    lateinit var vbTableView1:VBox
    @FXML    lateinit var vbTableView2:VBox
    @FXML    lateinit var vbTreeView1:VBox
    @FXML    lateinit var vbTreeView2:VBox


    @FXML    lateinit var taSelectedItemProperties: TextArea
    @FXML    lateinit var taStatus: TextArea
    @FXML    lateinit var lProgress: Label
    @FXML    lateinit var progressBar: ProgressBar
    @FXML    lateinit var hBoxProgress: HBox

    @FXML    lateinit var cbSyncSelection: CheckBox
    @FXML    lateinit var buttonCancelProcessing: Button

    // These button state should use to identify view type: Tree or Table
    @FXML    lateinit var tbDir1ViewTree: ToggleButton
    @FXML    lateinit var tbDir1ViewTable: ToggleButton
    @FXML    lateinit var tbDir2ViewTree: ToggleButton
    @FXML    lateinit var tbDir2ViewTable: ToggleButton
    @FXML    lateinit var tbDir2ViewAsMatchedTo1:  ToggleButton

    @FXML    lateinit var tb1All:       ToggleButton
    @FXML    lateinit var tb1FullMatch: ToggleButton
    @FXML    lateinit var tb1Similar:   ToggleButton
    @FXML    lateinit var tb1Suspected: ToggleButton
    @FXML    lateinit var tb1Unique:    ToggleButton

    @FXML    lateinit var tb1DirAndFile:    ToggleButton
    @FXML    lateinit var tb1DirOnly:       ToggleButton
    @FXML    lateinit var tb1FileOnly:      ToggleButton




    @FXML    lateinit var hbDir2MatchTypeButtonGroup: HBox
    @FXML    lateinit var tb2All:       ToggleButton
    @FXML    lateinit var tb2FullMatch: ToggleButton
    @FXML    lateinit var tb2Similar:   ToggleButton
    @FXML    lateinit var tb2Suspected: ToggleButton
    @FXML    lateinit var tb2Unique:    ToggleButton

    @FXML    lateinit var tb2DirAndFile:ToggleButton
    @FXML    lateinit var tb2DirOnly:   ToggleButton
    @FXML    lateinit var tb2FileOnly:  ToggleButton

    private val progressBarTextDuringCompare = "Compare in progress: "

    private var directory1: File? = null
    private var directory2: File? = null
    private lateinit var comparator: Comparator

    private lateinit var tg1DirView: ToggleGroup
    private lateinit var tg2DirView: ToggleGroup
    private lateinit var tg1Type: ToggleGroup
    private lateinit var tg2Type: ToggleGroup
    private lateinit var tg1Filter: ToggleGroup
    private lateinit var tg2Filter: ToggleGroup


    private val isProcessing = AtomicBoolean(false)
    private var isSyncing = false


    @FXML
    private fun initialize() {
        Log.enableJavaFX()
        //hBoxProgress.isVisible = false
        progressBarShow(false)
        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS

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
        tb1Suspected.toggleGroup    = tg1Filter
        tb1Unique.toggleGroup       = tg1Filter
        tb1All.isSelected = true

        //tbDir2ViewMatchToSelectedIn1.toggleGroup = tg2Filter
        tb2All.toggleGroup = tg2Filter
        tb2FullMatch.toggleGroup = tg2Filter
        tb2Similar.toggleGroup = tg2Filter
        tb2Suspected.toggleGroup = tg2Filter
        tb2Unique.toggleGroup = tg2Filter
        tb2All.isSelected = true

        tb1DirAndFile.toggleGroup       = tg1Type
        tb1DirOnly.toggleGroup          = tg1Type
        tb1FileOnly.toggleGroup         = tg1Type
        tb1DirAndFile.isSelected = true

        tb2DirAndFile.toggleGroup       = tg2Type
        tb2DirOnly.toggleGroup          = tg2Type
        tb2FileOnly.toggleGroup         = tg2Type
        tb2DirAndFile.isSelected = true

        comparator = Comparator()


        //TableView Configuration
        tableViewDir1.selectionModel.selectionMode = SelectionMode.MULTIPLE
        tableViewDir2.selectionModel.selectionMode = SelectionMode.MULTIPLE
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
        if(vbTreeView2.isVisible) dir2ViewChange()       //Switch to table view



        //Setup for testing
        directory1 = File("C:\\tmp\\Dir1")  //"c:\\Literature")   //("D:\\Dist\\IntelliJ\\GBTS_Exp41 migrate to StrTab")           //
        comboBoxDirectory1.value = directory1?.absolutePath
        directory2 = File("C:\\tmp\\Dir2")   //("D:\\Dist\\IntelliJ\\GBTS_Exp")     //("c:\\Inst")
        comboBoxDirectory2.value = directory2?.absolutePath



//        setupListener(treeViewDir1)
//        setupListener(treeViewDir2)

        setupListeners()

        treeViewDir1.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    when{
                        tbDir2ViewTree.isSelected  -> syncSelection(selectedItem.value,  treeViewDir2)
                        tbDir2ViewTable.isSelected -> syncSelection(selectedItem.value, tableViewDir2)
                        tbDir2ViewAsMatchedTo1.isSelected -> tableViewDir2.items = selectedItem.value.same
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
                    syncSelection(selectedItem,if (tbDir2ViewTree.isSelected) treeViewDir2 else tableViewDir2)
                }
            }

        }
        tableViewDir2.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            if(!isSyncing){
                selectedItem?.let {
                    when{
                        tbDir1ViewTree.isSelected -> syncSelection(selectedItem, treeViewDir1)
                        tbDir1ViewTable.isSelected -> syncSelection(selectedItem, tableViewDir1)
                    }
                }
            }

        }


        comparator = Comparator()
        // Listeners:
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

        Log.appendText("Comparator initialized")

    }

    private fun initClear(){
        Log.clear()
        taStatus.text = ""
        taSelectedItemProperties.text = ""
        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS;
    }


    @FXML
    fun selectDirectory1() {
        val directoryChooser = DirectoryChooser()
        directory1 = directoryChooser.showDialog(null)
        comboBoxDirectory1.value = directory1?.absolutePath
    }
    @FXML
    fun selectDirectory2() {
        val directoryChooser = DirectoryChooser()
        directory2 = directoryChooser.showDialog(null)
        comboBoxDirectory2.value = directory2?.absolutePath
    }

    @FXML
    fun onCompareClicked() {
        initClear()
        if (directory1 == null || directory2 == null) {
            taStatus.appendText("Both directories need to be selected!\n")
            return
        }

        //hBoxProgress.isVisible = true
        progressBarShow(true)
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
    }

    //
    private fun setupListener(treeView: TreeView<FileItem>) {


        treeView.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            selectedItem?.let {
                taSelectedItemProperties.text = selectedItem.value.toString()

                //     <<--   Only for first (Second shows only equivalent FileItems)
                if(cbSyncSelection.isSelected && treeView!=treeViewDir2){
                    // Очищаем предыдущее выделение в treeViewDir2
                    treeViewDir2.selectionModel.clearSelection()
                    var isFirstSelect = true

                    // Выбираем элементы из списка `same` в treeViewDir2
                    for (fi: FileItem in selectedItem.value.same) {
                        // Найдем элемент в treeViewDir2, соответствующий FileItem
                        val treeItemToSelect = TreeItemTraverse.findByValue(treeViewDir2.root, fi)
                        treeItemToSelect?.let {
                            treeViewDir2.selectionModel.select(treeItemToSelect)
                        }
                    }
                    // Properties MODE:  Second Tableview - Show properties of the first

                    if (tbDir2ViewAsMatchedTo1.isSelected) {
                        tableViewDir2.items = selectedItem.value.same
                        if (isFirstSelect){
                            // Scroll to this selected Item
                        }
                        //Log.appendText("Tried to find properties")
                    }
                }
            }
        }











//        // Добавление всплывающего меню
//        treeView.setOnMouseClicked { event ->
//            if (event.button == MouseButton.SECONDARY) {
//                val contextMenu = ContextMenu()
//
//                val openInExplorer = MenuItem("Open in Explorer")
//                openInExplorer.setOnAction {
//                    val selectedFile = treeView.selectionModel.selectedItem?.value
//                    selectedFile?.let {
//                        OsUtil.openInExplorer(it)
//                        // Полное удаление файла
//                        //it.delete()
//                        //Log.appendTextTimed("Deleted permanently: ${it.name}\n")
//                    }
//                }
//
//                val deleteRecycleBin = MenuItem("Delete to Recycle Bin")
//                deleteRecycleBin.setOnAction {
//                    val selectedFile = treeView.selectionModel.selectedItem?.value
//                    selectedFile?.let {
//                        // Удаление в корзину
//                        //OsUtil.deleteToRecycleBin(it)
//
//                    }
//                }
//
//                val deletePermanent = MenuItem("Delete Permanently")
//                deletePermanent.setOnAction {
//                    val selectedFile = treeView.selectionModel.selectedItem?.value
//                    selectedFile?.let {
//                        // Полное удаление файла
//                        if(OsUtil.confirmDelete(it.name)) OsUtil.deleteToTmp(it)    //it.delete()
//                        Log.appendTextTimed("Deleted permanently: ${it.name}\n")
//                    }
//                }
//
//
//
//                contextMenu.items.addAll(openInExplorer, deleteRecycleBin, deletePermanent)
//                contextMenu.show(treeView, event.screenX, event.screenY)
//            }
//        }
    }

//    private fun syncSelectionTree(fileItem:FileItem,treeView){
//        taSelectedItemProperties.text = fileItem.toString()
//        if(cbSyncSelection.isSelected){
//            var isFirst = true
//        }
//
//
//    }

    private fun syncSelection(fileItem:FileItem,view: Any){
        taSelectedItemProperties.text = fileItem.toString()

        //Select same items in other path

        if(cbSyncSelection.isSelected){
            var isFirst = true
            Log.appendText("sync Selection")
            when (view) {
                is TreeView<*> -> {
                    isSyncing = true
                    Log.appendText("Target is TreeView selected: " + fileItem.name)
                    val treeView = view as TreeView<FileItem>
                    treeView.selectionModel.clearSelection()

                    // Select in treeview all the items from List    same
                    for (fi: FileItem in fileItem.same) {

                        val treeItemToSelect = TreeItemTraverse.findByValue(treeView.root, fi)
                        treeItemToSelect?.let {
                            treeView.selectionModel.select(treeItemToSelect)

                            if (isFirst) {
                                treeView.scrollTo(treeView.getRow(treeItemToSelect)) // Прокрутка к элементу по индексу
                                isFirst = false
                            }
                        }
                    }
                }

                is TableView<*> -> {
                    isSyncing = true
                    val tableView = view as TableView<FileItem>
                    Log.appendText("Target is TableView selected: " + fileItem.name)
                    tableView.selectionModel.clearSelection()

                    for (fi: FileItem in fileItem.same){
                        tableView.items.forEachIndexed { index, fi ->
                            if ( fi.length() == fileItem.length() ) {
                                Log.appendText("----Table Found match" + fi.name + "   "+ index)
                                tableView.selectionModel.select(index)
                                if (isFirst) {
                                    tableView.scrollTo(index)
                                    isFirst = false
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

    @FXML fun tv1CollapseAll() =  UiTreeView.collapseAll(treeViewDir1)
    @FXML fun tv1ExpandAll() =  UiTreeView.expandAll(treeViewDir1)

    @FXML fun tv1CollapseLast() =  UiTreeView.collapseLast(treeViewDir1)
    @FXML fun tv1ExpandLast() =  UiTreeView.expandLast(treeViewDir1)

    @FXML fun tv1CollapseSelected() =  UiTreeView.collapseSelected(treeViewDir1)
    @FXML fun tv1ExpandSelected() =  UiTreeView.expandSelected(treeViewDir1)

    @FXML fun tv2CollapseAll() =  UiTreeView.collapseAll(treeViewDir2)
    @FXML fun tv2ExpandAll() =  UiTreeView.expandAll(treeViewDir2)

    @FXML fun tv2CollapseLast() =  UiTreeView.collapseLast(treeViewDir2)
    @FXML fun tv2ExpandLast() =  UiTreeView.expandLast(treeViewDir2)

    @FXML fun tv2CollapseSelected() =  UiTreeView.collapseSelected(treeViewDir2)
    @FXML fun tv2ExpandSelected() =  UiTreeView.expandSelected(treeViewDir2)


    // Filters
    @FXML fun filter1AllClick() { treeViewDir1.root = comparator.da1.root }   //comparator.da1.getfillAllDir1(treeViewDir1)}
    @FXML fun filter2AllClick() { treeViewDir2.root = comparator.da2.root }   // = comparator.fillAllDir2(treeViewDir2)

    @FXML fun filter1FullMatchClick() { treeViewDir1.root = comparator.da1.rootFullMatch }    // = comparator.fillFullMatch1(treeViewDir1)
    @FXML fun filter2FullMatchClick() { treeViewDir2.root = comparator.da2.rootFullMatch }    // = comparator.fillFullMatch2(treeViewDir2)

    @FXML fun tb1ViewDirOnlyClick(){ treeViewDir1.root = comparator.da1.rootDirOnly }  // = comparator.fillDirOnly1(treeViewDir1)
    @FXML fun tb2ViewDirOnlyClick(){ treeViewDir2.root = comparator.da2.rootDirOnly }  //  = comparator.fillDirOnly2(treeViewDir2)


    //--------------------------------------- Interface Configuration
    @FXML
    fun dir1ViewChange() {

        when{
            tbDir1ViewTree.isSelected -> {
                // Скрываем TabView и показываем TreeView
                vbTableView1.isVisible = false
                vbTableView1.isManaged = false
                tb1FileOnly.isVisible = false
                tb1FileOnly.isManaged = false

                vbTreeView1.isVisible = true
                vbTreeView1.isManaged = true

            }
            tbDir1ViewTable.isSelected -> {
                // Скрываем TreeView и показываем TabView and button: fileOnly
                vbTreeView1.isVisible = false
                vbTreeView1.isManaged = false


                vbTableView1.isVisible = true
                vbTableView1.isManaged = true
                tb1FileOnly.isVisible = true
                tb1FileOnly.isManaged = true
            }
        }
        filterDir1()
    }
    @FXML
    fun dir2ViewChange() {

        when{
            tbDir2ViewTree.isSelected -> {
                // Скрываем TabView и показываем TreeView
                vbTableView2.isVisible = false
                vbTableView2.isManaged = false
                tb2FileOnly.isVisible = false
                tb2FileOnly.isManaged = false

                hbDir2MatchTypeButtonGroup.isVisible = true
                hbDir2MatchTypeButtonGroup.isManaged = true
                vbTreeView2.isVisible = true
                vbTreeView2.isManaged = true

            }
            tbDir2ViewTable.isSelected -> {
                // Скрываем TreeView и показываем TabView
                vbTreeView2.isVisible = false
                vbTreeView2.isManaged = false


                hbDir2MatchTypeButtonGroup.isVisible = true
                hbDir2MatchTypeButtonGroup.isManaged = true
                vbTableView2.isVisible = true
                vbTableView2.isManaged = true
                tb2FileOnly.isVisible = true
                tb2FileOnly.isManaged = true
            }
            tbDir2ViewAsMatchedTo1.isSelected -> {
                vbTreeView2.isVisible = false
                vbTreeView2.isManaged = false
                vbTreeView2.isVisible = false
                vbTreeView2.isManaged = false

                hbDir2MatchTypeButtonGroup.isVisible = false
                hbDir2MatchTypeButtonGroup.isManaged = false

                vbTableView2.isVisible = true
                vbTableView2.isManaged = true
            }
        }
        filterDir2()

    }

    @FXML fun filterDir1(){
        when{

            // Tree
            tbDir1ViewTree.isSelected -> {

                when{
                    tb1All.isSelected           -> treeViewDir1.root = comparator.da1?.root
                    tb1DirAndFile.isSelected    -> treeViewDir1.root = comparator.da1?.root
                    tb1FullMatch.isSelected     -> treeViewDir1.root = comparator.da1?.rootFullMatch
                    tb1DirOnly.isSelected       -> treeViewDir1.root = comparator.da1?.rootDirOnly
                }



            }

            // Table
            tbDir1ViewTable.isSelected -> {

//                if(tb1All.isSelected) {
//                    tableViewDir1.items = comparator.da1?.observableList
//                    Log.appendText("All to Table 1")
//                    return
//                }

                // Apply filtering based on toggle group selections

                val filterMatch = when {
                    tb1All.isSelected       -> { _: FileItem -> true }
                    tb1FullMatch.isSelected -> { fileItem: FileItem -> fileItem.same.isNotEmpty() }
                    tb1Similar.isSelected   -> { fileItem: FileItem -> fileItem.similar.isNotEmpty() }
                    tb1Unique.isSelected    -> { fileItem: FileItem -> fileItem.same.isEmpty() && fileItem.similar.isEmpty() }
                    else -> { _: FileItem -> true }
                }

                val filterType = when {
                    tb1DirAndFile.isSelected    -> { _: FileItem -> true }
                    tb1DirOnly.isSelected       -> { fileItem: FileItem -> fileItem.isDirectory }
                    tb1FileOnly.isSelected      -> { fileItem: FileItem -> !fileItem.isDirectory }
                    else -> { _: FileItem -> true }
                }

                // Apply the combined filter
                comparator.da1.filteredList.setPredicate { fileItem -> filterMatch(fileItem) && filterType(fileItem) }
                tableViewDir1.items = comparator.da1.filteredList
//                Log.appendText("filteredList to Table 1")

            }
        }


    }
    @FXML fun filterDir2(){
        when{

            // Tree
            tbDir2ViewTree.isSelected -> {

                when{
                    tb2All.isSelected           -> {
                        treeViewDir2.root = comparator.da2?.root
                        Log.appendText("Tree2 - All Filter applied")

                    }
                    tb2DirAndFile.isSelected    -> treeViewDir2.root = comparator.da2?.root
                    tb2FullMatch.isSelected     -> treeViewDir2.root = comparator.da2?.rootFullMatch
                    tb2DirOnly.isSelected       -> {
                        treeViewDir2.root = comparator.da2?.rootDirOnly
                        Log.appendText("Tree2 - dir only Filter applied")
                    }
                }



            }

            // Table
            tbDir2ViewTable.isSelected -> {

                // Apply filtering based on toggle group selections

                val filterMatch = when {
                    tb2All.isSelected       -> { _: FileItem -> true }
                    tb2FullMatch.isSelected -> { fileItem: FileItem -> fileItem.same.isNotEmpty() }
                    tb2Similar.isSelected   -> { fileItem: FileItem -> fileItem.similar.isNotEmpty() }
                    tb2Unique.isSelected    -> { fileItem: FileItem -> fileItem.same.isEmpty() && fileItem.similar.isEmpty() }
                    else -> { _: FileItem -> true }
                }

                val filterType = when {
                    tb2DirAndFile.isSelected    -> { _: FileItem -> true }
                    tb2DirOnly.isSelected       -> { fileItem: FileItem -> fileItem.isDirectory }
                    tb2FileOnly.isSelected      -> { fileItem: FileItem -> !fileItem.isDirectory }
                    else -> { _: FileItem -> true }
                }

                // Apply the combined filter
                comparator.da2.filteredList.setPredicate { fileItem -> filterMatch(fileItem) && filterType(fileItem) }
                tableViewDir2.items = comparator.da2.filteredList
//                Log.appendText("filteredList to Table 2")

            }
        }
    }


//
//    // Function to update filter
//    fun updateFilter() {
//        filteredList.predicate = { fileItem ->
//            var matchesType = true
//            var matchesFileType = true
//
//            // Apply filter for Match Type
//            when {
//                matchesToggle.isSelected -> matchesType = fileItem.same.size > 1
//                similarToggle.isSelected -> matchesType = fileItem.similar.size > 1
//                uniqueToggle.isSelected -> matchesType = fileItem.same.isEmpty()
//            }
//
//            // Apply filter for File Type
//            when {
//                directoriesOnlyToggle.isSelected -> matchesFileType = fileItem.isDirectory
//                filesOnlyToggle.isSelected -> matchesFileType = !fileItem.isDirectory
//            }
//
//            matchesType && matchesFileType
//        }
//    }

}






