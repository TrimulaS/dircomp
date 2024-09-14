package com.trimula.dircomp

import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TreeView


import javafx.application.Platform

import javafx.scene.control.*
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.stage.DirectoryChooser
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
    lateinit var treeViewDirectory1: TreeView<File>
    @FXML
    lateinit var treeViewDirectory2: TreeView<File>
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
        directory1 = File("c:\\Inst")
        comboBoxDirectory1.value = directory1?.absolutePath
        directory2 = File("c:\\Inst")
        comboBoxDirectory2.value = directory2?.absolutePath

        setupTreeViewSelection(treeViewDirectory1)
        setupTreeViewSelection(treeViewDirectory2)
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
            val result1 = analyzeDirectory(directory1!!)
            val result2 = analyzeDirectory(directory2!!)

            // После завершения анализа – заполнение TreeView
            if (isProcessing.get()) {
                Platform.runLater {
                    fillTreeView(treeViewDirectory1, result1)
                    fillTreeView(treeViewDirectory2, result2)
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
        textAreaStatus.appendText("Processing cancelled by user.\n")
    }
    private fun analyzeDirectory(directory: File): List<File> {
        // Пример анализа – рекурсивный сбор файлов и директорий
        return directory.walk().toList()
    }

    private fun fillTreeView(treeView: TreeView<File>, files: List<File>) {
        val root = TreeItem(files.first())
        files.drop(1).forEach {
            root.children.add(TreeItem(it))
        }
        treeView.root = root
    }

    //
    private fun setupTreeViewSelection(treeView: TreeView<File>) {
        treeView.selectionModel.selectedItemProperty().addListener { _, _, selectedItem ->
            selectedItem?.let {
                textAreaSelectedItemProperties.isVisible = true
                // Здесь можно отобразить дополнительные данные о выбранном элементе
                textAreaSelectedItemProperties.text = "Selected item: ${selectedItem.value.name}\n" +
                        "Size: ${selectedItem.value.length()} bytes\n" +
                        "Path: ${selectedItem.value.absolutePath}"
            }
        }

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
                        it.delete()
                        textAreaStatus.appendText("Deleted permanently: ${it.name}\n")
                    }
                }

                contextMenu.items.addAll(deleteRecycleBin, deletePermanent)
                contextMenu.show(treeView, event.screenX, event.screenY)
            }
        }
    }

    private fun deleteToRecycleBin(file: File) {
        // Реализация удаления файла в корзину (на разных платформах может отличаться)
        textAreaStatus.appendText("Deleted to Recycle Bin: ${file.name}\n")
    }
}




