package com.trimula.dircomp.model

import com.trimula.dircomp.dataprocessing.OsUtil
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.File

class FileItem : File {
    @JvmField
    var directorySize: Long = 0

    @JvmField
    var same:    ObservableList<FileItem> = FXCollections.observableArrayList()
    var similar: ObservableList<FileItem> = FXCollections.observableArrayList()


    // Конструктор FileItem, используя путь к файлу
    constructor(pathname: String) : super(pathname) {

    }

    // Конструктор FileItem с File
    constructor(file: File) : super(file.path) {

    }



    override fun length(): Long {
        return if (isDirectory) directorySize
        else super.length()
    }


    override fun toString(): String {
        return """
     FileItem{Name: $name
     Path: $path     size: ${OsUtil.sizeAdopt(length())}      (  ${length()}  )
     Number of same items: ${same!!.size} Number of similar items: ${similar!!.size}
     """.trimIndent() +
                (if (same!!.size > 0) """
     Same:
     ${listToString(same!!)}
     """.trimIndent() else "")
    }

    private fun listToString(ll: List<FileItem>): String {
        val result = StringBuilder()
        for (element in ll) {
            result.append("\t" + element.absolutePath).append("\n")
        }
        return result.toString()
    }

    companion object {
        fun areSame(fileItem1: FileItem, fileItem2: FileItem): Boolean {
            return fileItem1.length() == fileItem2.length() && fileItem1.name === fileItem2.name
        }
        fun areSimilar(fileItem1: FileItem, fileItem2: FileItem): Boolean {
            return fileItem1.length() == fileItem2.length()
        }
        fun isSuspected(fileItem: FileItem): Boolean {
            return fileItem.length()<100
        }
    }
}
