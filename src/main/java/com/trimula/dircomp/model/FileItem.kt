package com.trimula.dircomp.model

import com.trimula.dircomp.dataprocessing.OsUtil
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.File

class FileItem  {
    @JvmField
    var length: Long = 0
    var name = "";
    var lastModified:Long =0
    var absolutePath = ""
    public var isEmpty:Boolean = true

    var same:    ObservableList<FileItem> =FXCollections.observableArrayList()// null    //= FXCollections.observableArrayList()
    var similar: ObservableList<FileItem> =FXCollections.observableArrayList()// null    //.observableArrayList()
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
     Path: $absolutePath     size: ${OsUtil.sizeAdopt(length())}      (  ${length()}  )      Empty: ${isEmpty}    
     Number of same items: ${sameSize()} 
     """.trimIndent() +
                (if (same!= null && same!!.size > 0) """
     Same:
     ${listToString(same!!)}
     """/*.trimIndent()*/ else "")
    }           // Number of similar items: ${similarSize()}


    private fun listToString(ll: List<FileItem>): String {
        val result = StringBuilder()
        for (element in ll) {
            result.append("\t" + element.absolutePath).append("\n")
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


    fun getIco():ImageView{
        var iv: ImageView? = null
        if (isDirectory){
            if(isEmpty){
                return  when{   // Empty
                    sameSize() == 0 -> ImageView(folderEmptyIcon.image)
                    sameSize() == 1 -> ImageView(folderX2EmptyIcon.image)
                    sameSize() == 2 -> ImageView(folderX3EmptyIcon.image)
                    sameSize() > 2 -> ImageView(folderXNEmptyIcon.image)
                    else -> ImageView(folderEmptyIcon.image)
                }

            }else{
                return when{   // Not Empty
                    sameSize() == 0 -> ImageView(folderIcon.image)
                    sameSize() == 1 -> ImageView(folderX2Icon.image)
                    sameSize() == 2 -> ImageView(folderX3Icon.image)
                    sameSize() > 2 -> ImageView(folderXNIcon.image)
                    else -> ImageView(folderIcon.image)
                }

            }

        }else{
            return   when{   // File
                sameSize() == 0 -> ImageView(fileIcon.image)
                sameSize() == 1 -> ImageView(fileX2Icon.image)
                sameSize() == 2 -> ImageView(fileX3Icon.image)
                sameSize() > 2 -> ImageView(fileXNIcon.image)
                else -> ImageView(fileIcon.image)
            }

        }


    }




    companion object {
        //            //Icons
        //            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_32x32.png")));
        //            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_32x32.png")));
        //
        //            private final ImageView fileMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileMultipleW10_32x32.png")));
        //            private final ImageView folderMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderMultipleW10_32x32.png")));
        //
        //            private final ImageView  folderEmptyIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_32x32.png")));
        //            private final ImageView  folderEmptyMultipleIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyMultipleW10_32x32.png")));

        //Icons
        val fileIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/fileW10_16x16.png")))
        val folderIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderW10_16x16.png")))
        val folderEmptyIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderEmptyW10_16x16.png")))

        val fileX2Icon = ImageView(Image(javaClass.getResourceAsStream("/icons/fileW10_x2__16x16.png")))
        val folderX2Icon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderW10r_x2__16x16.png")))
        val folderX2EmptyIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderEmptyW10_x2__16x16.png")))

        val fileX3Icon = ImageView(Image(javaClass.getResourceAsStream("/icons/fileW10_x3__16x16.png")))
        val folderX3Icon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderW10_x3__16x16.png")))
        val folderX3EmptyIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderEmptyW10_x3__16x16.png")))

        val fileXNIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/fileW10_xN__16x16.png")))
        val folderXNIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/folderW10_xN__16x16.png")))
        val folderXNEmptyIcon =  ImageView(Image(javaClass.getResourceAsStream("/icons/folderEmptyW10_xN__16x16.png")))

//        val fileSuspectedIcon = ImageView(Image(javaClass.getResourceAsStream("/icons/fileSuspectedW10__16x16.png")))




    }
}
