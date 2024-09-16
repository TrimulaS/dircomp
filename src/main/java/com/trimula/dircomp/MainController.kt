package com.trimula.dircomp


import com.trimula.dircomp.dataprocessing.OsUtil
import com.trimula.dircomp.model.Comparator
import com.trimula.dircomp.model.DataTableView
import com.trimula.dircomp.model.FileItem
import com.trimula.dircomp.view.ContentMenu
import com.trimula.dircomp.view.UiTreeView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import log.Log
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
    @FXML    lateinit var progressBar: ProgressBar
    @FXML    lateinit var hBoxProgress: HBox
    @FXML    lateinit var checkBoxShowSame: CheckBox
    @FXML    lateinit var buttonCancelProcessing: Button

    @FXML    lateinit var tbDir1ViewType: ToggleButton
    @FXML    lateinit var tbDir2ViewType: ToggleButton
    //@FXML    lateinit var filter1: ComboBox<String>

    @FXML    lateinit var filter1All:       ToggleButton
    @FXML    lateinit var filter1FullMatch: ToggleButton
    @FXML    lateinit var filter1Similar:   ToggleButton
    @FXML    lateinit var filter1Suspected: ToggleButton
    @FXML    lateinit var filter1Unique:    ToggleButton

    @FXML    lateinit var tb1ViewAll:       ToggleButton
    @FXML    lateinit var tb1ViewDirOnly:   ToggleButton
    @FXML    lateinit var tb1ViewFileOnly:  ToggleButton
    @FXML    lateinit var tb2ViewAll:       ToggleButton
    @FXML    lateinit var tb2ViewDirOnly:   ToggleButton
    @FXML    lateinit var tb2ViewFileOnly:  ToggleButton


    @FXML    lateinit var filter2MatchTo1:  ToggleButton
    @FXML    lateinit var filter2All:       ToggleButton
    @FXML    lateinit var filter2FullMatch: ToggleButton
    @FXML    lateinit var filter2Similar:   ToggleButton
    @FXML    lateinit var filter2Suspected: ToggleButton
    @FXML    lateinit var filter2Unique:    ToggleButton

    val  DIR_VIEW_TREE = "☷"
    val  DIR_VIEW_TABLE = "\uD83C\uDF33"

    private var directory1: File? = null
    private var directory2: File? = null
    private lateinit var comparator: Comparator

    private lateinit var tg1Filter: ToggleGroup
    private lateinit var tg2Filter: ToggleGroup
    private lateinit var tg1View: ToggleGroup
    private lateinit var tg2View: ToggleGroup

    private val isProcessing = AtomicBoolean(false)

    @FXML
    private fun initialize() {
        hBoxProgress.isVisible = false
        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS

        // Инициализация группы
        tg1Filter = ToggleGroup()
        tg2Filter = ToggleGroup()
        tg1View = ToggleGroup()
        tg2View = ToggleGroup()

        // Добавление кнопок в группу
        filter1All.toggleGroup = tg1Filter
        filter1FullMatch.toggleGroup = tg1Filter
        filter1Similar.toggleGroup = tg1Filter
        filter1Suspected.toggleGroup = tg1Filter
        filter1Unique.toggleGroup = tg1Filter

        filter2MatchTo1.toggleGroup = tg2Filter
        filter2All.toggleGroup = tg2Filter
        filter2FullMatch.toggleGroup = tg2Filter
        filter2Similar.toggleGroup = tg2Filter
        filter2Suspected.toggleGroup = tg2Filter
        filter2Unique.toggleGroup = tg2Filter

        tb1ViewAll.toggleGroup      = tg1View
        tb1ViewDirOnly.toggleGroup  = tg1View
        tb1ViewFileOnly.toggleGroup = tg1View

        tb1ViewAll.toggleGroup      = tg2View
        tb1ViewDirOnly.toggleGroup  = tg2View
        tb1ViewFileOnly.toggleGroup = tg2View

        // Установим начально выбранную кнопку (например, filter2All)
        filter1All.isSelected = true
        filter2MatchTo1.isSelected = true
        //filter2MatchTo1.isSelected = true

        //TableView Configuration
        DataTableView.setupTableView(tableViewDir1)
        DataTableView.setupTableView(tableViewDir2)

        ContentMenu.addToTreeView(treeViewDir1)
        ContentMenu.addToTreeView(treeViewDir2)


        // Switch to TreeView
        dir1ViewTypeClick()
        if(!vbTreeView1.isVisible) dir1ViewTypeClick()
        dir2ViewTypeClick()
        if(vbTreeView2.isVisible) dir2ViewTypeClick()       //Switch to table view



        //Setup for testing
        directory1 = File("c:\\Literature")   //("D:\\Dist\\IntelliJ\\GBTS_Exp41 migrate to StrTab")           //
        comboBoxDirectory1.value = directory1?.absolutePath
        directory2 = File("c:\\Literature")   //("D:\\Dist\\IntelliJ\\GBTS_Exp")     //("c:\\Inst")
        comboBoxDirectory2.value = directory2?.absolutePath



        setupListener(treeViewDir1)
        setupListener(treeViewDir2)

        comparator = Comparator(treeViewDir1, treeViewDir2)
        Log.appendText("Comparator initialized")

    }

    private fun initClear(){
        Log.clear()
        taStatus.text = ""
        taSelectedItemProperties.text = ""
    }

    @FXML
    fun dir1ViewTypeClick() {
        if (vbTreeView1.isVisible)
        {
            // Скрываем TreeView и показываем TabView
            vbTreeView1.isVisible = false
            vbTreeView1.isManaged = false
            vbTableView1.isVisible = true
            vbTableView1.isManaged = true
            tbDir1ViewType.text = DIR_VIEW_TABLE  // Меняем текст на кнопке, если нужно
        } else
        {
            // Скрываем TabView и показываем TreeView
            vbTableView1.isVisible = false
            vbTableView1.isManaged = false
            vbTreeView1.isVisible = true
//            vbTreeView1.maxHeight = Double.MAX_VALUE
            vbTreeView1.isManaged = true
            tbDir1ViewType.text = DIR_VIEW_TREE  // Меняем текст на кнопке обратно
        }
    }
    @FXML
    fun dir2ViewTypeClick() {
        if (vbTreeView2.isVisible)
        {
            // Скрываем TreeView и показываем TabView
            vbTreeView2.isVisible = false
//            vbTreeView2.maxHeight = 0.0
            vbTreeView2.isManaged = false
            vbTableView2.isVisible = true
//            vbTableView2.maxHeight = Double.MAX_VALUE
            vbTableView2.isManaged = true
            tbDir2ViewType.text = DIR_VIEW_TABLE  // Меняем текст на кнопке, если нужно
        } else
        {
            // Скрываем TabView и показываем TreeView
            vbTableView2.isVisible = false
//            vbTableView2.maxHeight = 0.0
            vbTableView2.isManaged = false
            vbTreeView2.isVisible = true
//            vbTreeView2.maxHeight = Double.MAX_VALUE
            vbTreeView2.isManaged = true
            tbDir2ViewType.text = DIR_VIEW_TREE  // Меняем текст на кнопке обратно
        }
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

        hBoxProgress.isVisible = true
        isProcessing.set(true)

        // Запуск потоков для анализа директорий
        thread {

            comparator.processDirectories(directory1,directory2)

            // После завершения анализа – заполнение TreeView
            if (isProcessing.get()) {
                Platform.runLater {

                    comparator.fillUpTreeView()     //Only in JavaFX UI Thread
                    hBoxProgress.isVisible = false
                }
            }
        }
    }


    @FXML
    fun onCancelProcessing(/*actionEvent: ActionEvent*/) {
        // Прерывание обработки
        isProcessing.set(false)
        hBoxProgress.isVisible = false
        Log.appendTextTimed("Processing cancelled by user.\n")

    }
    private fun analyzeDirectory(directory: File): List<File> {
        // Пример анализа – рекурсивный сбор файлов и директорий
        return directory.walk().toList()
    }


    //
    private fun setupListener(treeView: TreeView<FileItem>) {
        treeView.selectionModel.selectionMode = SelectionMode.MULTIPLE

        treeView.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            selectedItem?.let {
                taSelectedItemProperties.isVisible = true
                // Здесь можно отобразить дополнительные данные о выбранном элементе
                taSelectedItemProperties.text = selectedItem.value.toString()

                //Only for first
                if(treeView!=treeViewDir2){
                    // Очищаем предыдущее выделение в treeViewDir2
                    treeViewDir2.selectionModel.clearSelection()

                    // Выбираем элементы из списка `same` в treeViewDir2
                    for (fi: FileItem in selectedItem.value.same) {
                        // Найдем элемент в treeViewDir2, соответствующий FileItem
                        val treeItemToSelect = findTreeItem(treeViewDir2.root, fi)
                        treeItemToSelect?.let {
                            treeViewDir2.selectionModel.select(treeItemToSelect)
                        }
                    }
                    // Properties MODE:  Second Tableview - Show properties of the first

                    if (filter2MatchTo1.isSelected) {
                        DataTableView.fillTableViewWithSameFiles(comparator.root2,selectedItem.value,tableViewDir2)
                        //Log.appendText("Tried to find properties")
                    }
                }
//                    "Selected item: ${selectedItem.value.name}\n" +
//                        "Size: ${selectedItem.value.length()} bytes\n" +
//                        "Path: ${selectedItem.value.absolutePath}"
            }
        }



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






        // Добавление всплывающего меню
        treeView.setOnMouseClicked { event ->
            if (event.button == MouseButton.SECONDARY) {
                val contextMenu = ContextMenu()

                val openInExplorer = MenuItem("Open in Explorer")
                openInExplorer.setOnAction {
                    val selectedFile = treeView.selectionModel.selectedItem?.value
                    selectedFile?.let {
                        OsUtil.openInExplorer(it)
                        // Полное удаление файла
                        //it.delete()
                        //Log.appendTextTimed("Deleted permanently: ${it.name}\n")
                    }
                }

                val deleteRecycleBin = MenuItem("Delete to Recycle Bin")
                deleteRecycleBin.setOnAction {
                    val selectedFile = treeView.selectionModel.selectedItem?.value
                    selectedFile?.let {
                        // Удаление в корзину
                        //OsUtil.deleteToRecycleBin(it)

                    }
                }

                val deletePermanent = MenuItem("Delete Permanently")
                deletePermanent.setOnAction {
                    val selectedFile = treeView.selectionModel.selectedItem?.value
                    selectedFile?.let {
                        // Полное удаление файла
                        if(OsUtil.confirmDelete(it.name)) OsUtil.deleteToTmp(it)    //it.delete()
                        Log.appendTextTimed("Deleted permanently: ${it.name}\n")
                    }
                }



                contextMenu.items.addAll(openInExplorer, deleteRecycleBin, deletePermanent)
                contextMenu.show(treeView, event.screenX, event.screenY)
            }
        }
    }


    // Рекурсивная функция для поиска TreeItem по значению FileItem
    private fun findTreeItem(treeItem: TreeItem<FileItem>, fi: FileItem): TreeItem<FileItem>? {
        if (treeItem.value == fi) return treeItem

        for (child in treeItem.children) {
            val result = findTreeItem(child, fi)
            if (result != null) {
                return result
            }
        }
        return null
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
}






