package com.trimula.dircomp.view

import com.trimula.dircomp.model.DataTableView
import com.trimula.dircomp.model.FileItem
import javafx.scene.control.*
import java.io.File

/**     Works in pair with
 *      UiTreeView.kt
 *      and    UiTableView
 *
 * */

class ContentMenu {
    companion object{
        fun  addToTreeView(treeView: TreeView<*>){
            val contextMenu = ContextMenu()

            val miCollapseAll =     MenuItem("Collapse All")
            val miExpandAll =       MenuItem("Expand All")
            val miCollapseLast =    MenuItem("Collapse Last")
            val miExpandLast =      MenuItem("Expand Last")


            contextMenu.items.add(miCollapseAll)
            contextMenu.items.add(miExpandAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miCollapseLast)
            contextMenu.items.add(miExpandLast)

            miCollapseAll.setOnAction   {   UiTreeView.collapseAll(treeView)                }
            miExpandAll.setOnAction     {   UiTreeView.expandAll(treeView)                  }
            miCollapseLast.setOnAction  {   UiTreeView.collapseLast(treeView)               }
            miExpandLast.setOnAction    {   UiTreeView.expandLast(treeView)                 }
            treeView.contextMenu = contextMenu

        }
        fun addToCodeArea(textArea: TextArea){

            val contextMenu = ContextMenu()

            val miCut =         MenuItem("Cut")
            val miCopy =        MenuItem("Copy")
            val miPaste =       MenuItem("Paste")
            val miSelectAll =   MenuItem("Select All")
            val miUndo =        MenuItem("Undo")
            val miRedo =        MenuItem("Redo")

            // add menu items to menu
            contextMenu.items.add(miCut)
            contextMenu.items.add(miCopy)
            contextMenu.items.add(miPaste)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miSelectAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miUndo)
            contextMenu.items.add(miRedo)

            miCut.setOnAction       {textArea.cut() }
            miCopy.setOnAction      {textArea.copy()   }
            miPaste.setOnAction     {textArea.paste()   }
            miSelectAll.setOnAction {textArea.selectAll()  }
            miUndo.setOnAction      {textArea.undo() }
            miRedo.setOnAction      {textArea.redo() }

            textArea.contextMenu = contextMenu

        }

        fun  addToTableView(tableView: TableView<FileItem>){
            val contextMenu = ContextMenu()

            val miCopy = MenuItem("Copy")
            val miSelectAll = MenuItem("Select All")
            val miDeleteSelected = MenuItem("Delete Selected")
            val miConvertTo = MenuItem("Convert to..")

            // add menu items to menu

//            contextMenu.items.add(miCopy)
//            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miSelectAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miDeleteSelected)

//            miCut.setOnAction       {tableView.cut() }
//            miCopy.setOnAction      {     }
//            miPaste.setOnAction     {codeArea.paste()   }
            miSelectAll.setOnAction {   tableView.selectionModel.selectAll()  }
            miDeleteSelected.setOnAction {  DataTableView.deleteSelected(tableView)  }
//            miRedo.setOnAction      {codeArea.redo() }

            tableView.contextMenu = contextMenu

        }
    }
}