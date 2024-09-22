package com.trimula.dircomp.view.tiny

import javafx.scene.control.Label

class DirectoryStatusBar (
    private val lFiles:Label,
    private val lFilesFiltered:Label,
    private val lDirectories:Label,
    private val lDirectoriesFiltered :Label,
    private val lTotal:Label,
    private val lTotalFiltered:Label) {


    private val prefix = ""
    private val infix = "?"
    private val postfix = ""
    private val prefixFiltered = " / ( "
    private val postfixFiltered = " )"
    fun update( numOfFiles:Int, numOfDirectories:Int  ){

        lFilesFiltered.isVisible =          false
        lDirectoriesFiltered.isVisible =    false
        lTotalFiltered.isVisible =          false

        lFiles.text =       prefix + numOfFiles                         + postfix
        lDirectories.text = prefix + numOfDirectories                   + postfix
        lTotal.text =       prefix + (numOfFiles + numOfDirectories)    + postfix
    }
    fun updateFiltered( numOfFilesFiltered:Int, numOfDirectoriesFiltered:Int  ){

        lFilesFiltered.isVisible =          true
        lDirectoriesFiltered.isVisible =    true
        lTotalFiltered.isVisible =          true

        lFilesFiltered.text =       prefixFiltered + numOfFilesFiltered                                 + postfixFiltered
        lDirectoriesFiltered.text = prefixFiltered + numOfDirectoriesFiltered                           + postfixFiltered
        lTotalFiltered.text =       prefixFiltered + ( numOfFilesFiltered + numOfDirectoriesFiltered)   + postfixFiltered
    }
    fun reset() {
        lFiles.text =           prefix  + infix + postfix
        lDirectories.text =     prefix  + infix + postfix
        lTotal.text =           prefix  + infix + postfix

        lFilesFiltered.text =       prefixFiltered  + infix + postfixFiltered
        lDirectoriesFiltered.text = prefixFiltered  + infix + postfixFiltered
        lTotalFiltered.text =       prefixFiltered  + infix + postfixFiltered
    }
}