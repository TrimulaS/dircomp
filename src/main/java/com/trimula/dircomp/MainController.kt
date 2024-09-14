package com.trimula.dircomp


import com.trimula.dircomp.filetree.Comparator
import com.trimula.dircomp.filetree.TreeItemBuilder
import com.trimula.dircomp.ui.UiTreeView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.stage.DirectoryChooser
import log.Log
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread


class MainController {
//    @FXML
//    private val welcomeText: Label? = null
//    @FXML
//    protected fun onHelloButtonClick() {
//        welcomeText!!.text = "Welcome to JavaFX Application!"
//    }



    @FXML
    lateinit var comboBoxDirectory1: ComboBox<String>
    @FXML
    lateinit var comboBoxDirectory2: ComboBox<String>
    @FXML
    lateinit var treeViewDir1: TreeView<File>
    @FXML
    lateinit var treeViewDir2: TreeView<File>
    @FXML
    lateinit var textAreaSelectedItemProperties: TextArea
    @FXML
    lateinit var textAreaStatus: TextArea
    @FXML
    lateinit var progressBar: ProgressBar
    @FXML
    lateinit var hBoxProgress: HBox
    @FXML
    lateinit var checkBoxShowSame: CheckBox
    @FXML
    lateinit var buttonCancelProcessing: Button

    private var directory1: File? = null
    private var directory2: File? = null
    private lateinit var comparator: Comparator

    private val isProcessing = AtomicBoolean(false)

    @FXML
    private fun initialize() {
        // Выбор директории для Directory1
        comboBoxDirectory1.setOnMouseClicked {
            val directoryChooser = DirectoryChooser()
            directory1 = directoryChooser.showDialog(null)
            comboBoxDirectory1.value = directory1?.absolutePath
        }

        // Выбор директории для Directory2
        comboBoxDirectory2.setOnMouseClicked {
            val directoryChooser = DirectoryChooser()
            directory2 = directoryChooser.showDialog(null)
            comboBoxDirectory2.value = directory2?.absolutePath
        }

        //Setup for testing
        directory1 = File("D:\\Dist\\IntelliJ\\GBTS_Exp41 migrate to StrTab")           //("c:\\Inst")
        comboBoxDirectory1.value = directory1?.absolutePath
        directory2 = File("D:\\Dist\\IntelliJ\\GBTS_Exp")     //("c:\\Inst")
        comboBoxDirectory2.value = directory2?.absolutePath



        setupListener(treeViewDir1)
        setupListener(treeViewDir2)

        comparator = Comparator(treeViewDir1, treeViewDir2)
        Log.appendText("Comparator initialized")

    }

    @FXML
    fun onCompareClicked() {
        if (directory1 == null || directory2 == null) {
            textAreaStatus.appendText("Both directories need to be selected!\n")
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

//                    fillTreeView(treeViewDirectory1, result1)
//                    fillTreeView(treeViewDirectory2, result2)
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
    private fun setupListener(treeView: TreeView<File>) {
        treeView.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            selectedItem?.let {
                textAreaSelectedItemProperties.isVisible = true
                // Здесь можно отобразить дополнительные данные о выбранном элементе
                textAreaSelectedItemProperties.text = selectedItem.value.toString()
//                    "Selected item: ${selectedItem.value.name}\n" +
//                        "Size: ${selectedItem.value.length()} bytes\n" +
//                        "Path: ${selectedItem.value.absolutePath}"
            }
        }

        // Addin Logs
            Log.addListener (object : Log.LogListener {
            override fun onChange(logText: String) {
//                println("Log has changed: $logText")
                textAreaStatus.text = Log.get()
            }

            override fun onBeforeClear(logText: String) {
                println("Log is about to be cleared: $logText")
            }
        } )

        // Добавление всплывающего меню
        treeView.setOnMouseClicked { event ->
            if (event.button == MouseButton.SECONDARY) {
                val contextMenu = ContextMenu()

                val deleteRecycleBin = MenuItem("Delete to Recycle Bin")
                deleteRecycleBin.setOnAction {
                    val selectedFile = treeView.selectionModel.selectedItem?.value
                    selectedFile?.let {
                        // Удаление в корзину
                        deleteToRecycleBin(it)
                    }
                }

                val deletePermanent = MenuItem("Delete Permanently")
                deletePermanent.setOnAction {
                    val selectedFile = treeView.selectionModel.selectedItem?.value
                    selectedFile?.let {
                        // Полное удаление файла
                        //it.delete()
                        Log.appendTextTimed("Deleted permanently: ${it.name}\n")
                    }
                }

                contextMenu.items.addAll(deleteRecycleBin, deletePermanent)
                contextMenu.show(treeView, event.screenX, event.screenY)
            }
        }
    }

    private fun deleteToRecycleBin(file: File) {

//        if (Desktop.isDesktopSupported(Desktop.Action.MOVE_TO_TRASH)) {
//            try {
//                java.awt.Desktop.moveToTrash(file)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                //return false
//            }
//        } else {
//            println("No Trash")
//            //return false
//        }
        Log.appendTextTimed("Deleted to Recycle Bin: ${file.name}\n")
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






