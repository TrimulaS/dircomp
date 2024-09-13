package com.trimula.dircomp

import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TreeView


import javafx.application.Platform

import javafx.scene.control.*
import javafx.scene.control.TreeItem
import javafx.scene.layout.HBox
import javafx.stage.DirectoryChooser
import java.io.File
import kotlin.concurrent.thread

class HelloController {
    @FXML
    private val welcomeText: Label? = null

    @FXML
    protected fun onHelloButtonClick() {
        welcomeText!!.text = "Welcome to JavaFX Application!"
    }



    @FXML
    lateinit var comboBoxDirectory1: ComboBox<String>
    @FXML
    lateinit var comboBoxDirectory2: ComboBox<String>
    @FXML
    lateinit var treeViewDirectory1: TreeView<File>
    @FXML
    lateinit var treeViewDirectory2: TreeView<File>
    @FXML
    lateinit var textAreaStatus: TextArea
    @FXML
    lateinit var progressBar: ProgressBar
    @FXML
    lateinit var hBoxProgress: HBox

    private var directory1: File? = null
    private var directory2: File? = null

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
    }

    @FXML
    fun onCompareClicked() {
        if (directory1 == null || directory2 == null) {
            textAreaStatus.appendText("Both directories need to be selected!\n")
            return
        }

        hBoxProgress.isVisible = true

        // Запуск потоков для анализа директорий
        thread {
            val result1 = analyzeDirectory(directory1!!)
            val result2 = analyzeDirectory(directory2!!)

            // После завершения анализа – заполнение TreeView
            Platform.runLater {
                fillTreeView(treeViewDirectory1, result1)
                fillTreeView(treeViewDirectory2, result2)
                hBoxProgress.isVisible = false
            }
        }
    }

    @FXML
    fun onCancelProcessing(/*actionEvent: ActionEvent*/) {

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




}