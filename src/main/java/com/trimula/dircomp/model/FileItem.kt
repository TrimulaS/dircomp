package com.trimula.dircomp.model

import com.trimula.dircomp.dataprocessing.OsUtil
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.File

class FileItem  {
    @JvmField
    var length: Long = 0
    var name = "";
    var lastModified:Long =0
    var absolutePath = ""

    var same:    ObservableList<FileItem>? = null    //= FXCollections.observableArrayList()
    var similar: ObservableList<FileItem>? = null    //.observableArrayList()
    var isDirectory = false


    // Конструктор FileItem с File
    constructor(file: File) {
        initiation(file)

    }

    private fun initiation(file:File){
        this.isDirectory =      file.isDirectory
        this.name =             file.name
        this.lastModified =     file.lastModified()
        this.absolutePath =     file.absolutePath
        this.length =           file.length()
    }

//    //Block for Lists processing---------------------------------------------
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



    fun length() = length

    fun lastModified() = lastModified


    override fun toString(): String {
        return """
     Name: $name
     Path: $absolutePath     size: ${OsUtil.sizeAdopt(length())}      (  ${length()}  )
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
            result.append("\t" + element.absolutePath).append("\n\t")
        }
        return result.toString()
    }



    //Comparing
    fun sameTo(fileItem: FileItem): Boolean {
//        Log.appendTextTimed("length: " + length + " = " + fileItem.length() + "   name: " + name + " = " + fileItem.name + "result: " + (length == fileItem.length() && name == fileItem.name) )
        return length == fileItem.length() && name == fileItem.name
    }

    fun similarTo(fileItem: FileItem): Boolean {
        return length > 0 && length == fileItem.length
    }
    fun isSuspected(): Boolean {
        return length <= 16
    }



    companion object {


    }
}
