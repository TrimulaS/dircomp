package com.trimula.dircomp.model

import com.trimula.dircomp.dataprocessing.OsUtil
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.File

class FileItem : File {
    @JvmField
    var directorySize: Long = 0

    var same:    ObservableList<FileItem>? = null    //= FXCollections.observableArrayList()
    var similar: ObservableList<FileItem>? = null    //.observableArrayList()


    // Конструктор FileItem, используя путь к файлу
    constructor(pathname: String) : super(pathname)

    // Конструктор FileItem с File
    constructor(file: File) : super(file.path)

//    //Block for Lists porcessing---------------------------------------------
    fun sameAdd(fileItem: FileItem){
        same = same ?: FXCollections.observableArrayList()
        same!!.add(fileItem)
    }
    fun sameSize():Int{
        return same?.size?:0
    }
    fun similarSize():Int{
        return same?.size?:0
    }

//    // Метод для безопасной обработки каждого элемента
//    fun sameForEach(action: (FileItem) -> Unit) {
//        same?.forEach(action)
//    }
//    fun sameSize():Int {
//        return same?.size?:0
//    }



    override fun length(): Long {
        return if (isDirectory) directorySize
        else super.length()
    }


    override fun toString(): String {
        return """
     FileItem{Name: $name
     Path: $path     size: ${OsUtil.sizeAdopt(length())}      (  ${length()}  )
     Number of same items: ${sameSize()} 
     """.trimIndent() +
                (if (same!= null && same!!.size > 0) """
     Same:
     ${listToString(same!!)}
     """.trimIndent() else "")
    }           // Number of similar items: ${similarSize()}

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
