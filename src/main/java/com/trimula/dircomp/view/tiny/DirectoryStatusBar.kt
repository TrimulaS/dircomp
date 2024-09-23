package com.trimula.dircomp.view.tiny

import javafx.scene.Node
import javafx.scene.control.Label

class DirectoryStatusBar (
    private val lFiles:Label,
    private val lFilesAll:Label,
    private val lDirectories:Label,
    private val lDirectoriesAll :Label,
    private val lTotal:Label,
    private val lTotalAll:Label) {


    private var directoriesAll = 0
    private var filesAll = 0
    private var totalAll = 0
    private val prefix = ""
    private val infix = "?"
    private val postfix = ""
    private val prefixAll = "  ( "
    private val postfixAll = " )"

    // First update - numbers took as unfiltered list parameters
    fun update( numOfDirectories:Int, numOfFiles:Int ){

        hide( lDirectoriesAll )
        hide( lFilesAll)
        hide( lTotalAll )

        directoriesAll = numOfDirectories
        filesAll = numOfFiles
        totalAll = numOfFiles + numOfDirectories

        lDirectories.text = prefix + directoriesAll     + postfix
        lFiles.text =       prefix + filesAll           + postfix
        lTotal.text =       prefix + totalAll           + postfix

    }
    fun updateFiltered( numOfDirectoriesFiltered:Int, numOfFilesFiltered:Int  ){

        show( lFilesAll )
        show( lDirectoriesAll )
        show( lTotalAll )

        lFiles.text =       prefix + numOfFilesFiltered                                 + postfix
        lDirectories.text = prefix + numOfDirectoriesFiltered                           + postfix
        lTotal.text =       prefix + ( numOfFilesFiltered + numOfDirectoriesFiltered)   + postfix

        lDirectoriesAll.text =  prefixAll + directoriesAll     + postfixAll
        lFilesAll.text =        prefixAll + filesAll           + postfixAll
        lTotalAll.text =        prefixAll + totalAll           + postfixAll


    }
    fun reset() {
        lFiles.text =           prefix  + infix + postfix
        lDirectories.text =     prefix  + infix + postfix
        lTotal.text =           prefix  + infix + postfix

        lFilesAll.text =       prefixAll  + infix + postfixAll
        lDirectoriesAll.text = prefixAll  + infix + postfixAll
        lTotalAll.text =       prefixAll  + infix + postfixAll
    }

    private fun hide(node: Node){
        node.isVisible = false
        node.isManaged = false
    }

    private fun show(node: Node){
        node.isVisible = true
        node.isManaged = true
    }
}