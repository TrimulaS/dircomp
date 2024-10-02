package com.trimula.dircomp.view.tiny

import com.trimula.dircomp.model.DirectoryStatistics
import javafx.scene.Node
import javafx.scene.control.Label

class DirectoryStatusBar (
    private val lTotal:Label,
    private val lTotalSame:Label,
    private val lTotalAll:Label,
    private val lTotalAllSame:Label,

    private val lDirectories:Label,
    private val lDirectoriesSame:Label,
    private val lDirectoriesAll :Label,
    private val lDirectoriesAllSame :Label,
    private val lDirectoriesAllPostfix :Label,

    private val lFiles:Label,
    private val lFilesSame:Label,
    private val lFilesAll:Label,
    private val lFilesAllSame:Label,
    private val lFilesAllPostfix:Label,


) {


    private var directoriesAll = 0
    private var directoriesAllSame = 0
    private var filesAll = 0
    private var filesAllSame = 0
    private var totalAll = 0
    private var totalAllSame = 0


    private val infix = "?"
    private val prefix = ""
    private val postfix = ""

    private val prefixSame = " / "
    private val postfixSame = ""

    private val prefixAll = "  ( "
    private val postfixAll = " /"

    private val prefixAllSame = ""
    private val postfixAllSame = " )"

    init {
        reset()
    }

    // First update - numbers took as unfiltered list parameters - "All option hidden"

    fun update(directoryStatistics: DirectoryStatistics){
        update( directoryStatistics.directories,
                directoryStatistics.directoriesSame,
                directoryStatistics.files,
                directoryStatistics.filesSame)
    }
    private fun update( numOfDirectories:Int, numOfDirectoriesSame:Int, numOfFiles:Int, numOfFilesSame:Int ){

        hide( lTotalAll )
        hide( lTotalAllSame )
        hide( lDirectoriesAll )
        hide( lDirectoriesAllSame )
        hide( lDirectoriesAllPostfix )
        hide( lFilesAll )
        hide( lFilesAllSame )
        hide( lFilesAllPostfix)



        totalAll = numOfFiles + numOfDirectories
        directoriesAll = numOfDirectories
        filesAll = numOfFiles

        totalAllSame = numOfFilesSame + numOfDirectoriesSame
        directoriesAllSame = numOfDirectoriesSame
        filesAllSame = numOfFilesSame




        lTotal.text =       prefix + totalAll           + postfix
        lDirectories.text = prefix + directoriesAll     + postfix
        lFiles.text =       prefix + filesAll           + postfix

        lTotalSame.text =       prefixSame + totalAllSame           + postfixSame
        lDirectoriesSame.text = prefixSame + directoriesAllSame     + postfixSame
        lFilesSame.text =       prefixSame + filesAllSame           + postfixSame


    }
    fun updateFiltered(directoryStatistics: DirectoryStatistics){
        updateFiltered( directoryStatistics.directories,
            directoryStatistics.directoriesSame,
            directoryStatistics.files,
            directoryStatistics.filesSame)
    }
    fun updateFiltered( numOfDirectoriesFiltered:Int,numOfDirectoriesSameFiltered:Int, numOfFilesFiltered:Int, numOfFilesSameFiltered:Int  ){

        show( lTotalAll )
        show( lTotalAllSame )
        show( lDirectoriesAll )
        show( lDirectoriesAllSame )
        show( lDirectoriesAllPostfix )
        show( lFilesAll )
        show( lFilesAllSame )
        show( lFilesAllPostfix)

        lTotal.text =       prefix + ( numOfFilesFiltered + numOfDirectoriesFiltered)   + postfix
        lDirectories.text = prefix + numOfDirectoriesFiltered                           + postfix
        lFiles.text =       prefix + numOfFilesFiltered                                 + postfix

        lTotalSame.text =       prefixSame +  (numOfDirectoriesSameFiltered+numOfFilesSameFiltered)     + postfixSame
        lDirectoriesSame.text = prefixSame +  numOfDirectoriesSameFiltered                              + postfixSame
        lFilesSame.text =       prefixSame +  numOfFilesSameFiltered                                    + postfixSame

        lTotalAll.text =        prefixAll + totalAll            + postfixAll
        lDirectoriesAll.text =  prefixAll + directoriesAll      + postfixAll
        lFilesAll.text =        prefixAll + filesAll            + postfixAll

        lTotalAllSame.text =        prefixAllSame + totalAllSame            + postfixAllSame
        lDirectoriesAllSame.text =  prefixAllSame + directoriesAllSame      + postfixAllSame
        lFilesAllSame.text =        prefixAllSame + filesAllSame            + postfixAllSame


        // if filtered = same hide filtered and just show same

    }
    fun reset() {

//                lTotal / lTotalSame                   lTotalAll / lTotalAllSame
//        Total:    5    /     3                         ( 7      /       5 )

//          lDirectories / lDirectoriesAll   lDirectoriesAllSame  /    lDirectoriesAllPostfix
//        D:           2 / 1                             ( 3      /       2 )

//                lFiles / lFilesAll                lFilesAllSame /  lFilesAllPostfix
//        F:           3 / 2                             ( 4      /       3 )



        lTotal.text =           prefix  + infix + postfix
        lFiles.text =           prefix  + infix + postfix
        lDirectories.text =     prefix  + infix + postfix

        lTotalAll.text =       prefixAll  + infix + postfixAll
        lFilesAll.text =       prefixAll  + infix + postfixAll
        lDirectoriesAll.text = prefixAll  + infix + postfixAll

        lTotalAllSame.text =       prefixAllSame  + infix + postfixAllSame
        lFilesAllSame.text =       prefixAllSame  + infix + postfixAllSame
        lDirectoriesAllSame.text = prefixAllSame  + infix + postfixAllSame

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