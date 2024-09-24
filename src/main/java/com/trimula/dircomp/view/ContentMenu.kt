package com.trimula.dircomp.view

import com.trimula.dircomp.dataprocessing.Log
import com.trimula.dircomp.dataprocessing.OsUtil
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
        var isTestMode = true

        fun addToTreeView(treeView: TreeView<*>) {
            val contextMenu = ContextMenu()

            val miOpen = MenuItem("Open")
            val miOpenExplorer = MenuItem("Open in explorer")
            val miCollapseAll = MenuItem("Collapse All")
            val miExpandAll = MenuItem("Expand All")
            val miCollapseLast = MenuItem("Collapse Last")
            val miExpandLast = MenuItem("Expand Last")
            val miDelete = MenuItem("Delete")               // Works if only TreeView<as File>

            contextMenu.items.add(miOpen)
            contextMenu.items.add(miOpenExplorer)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miCollapseAll)
            contextMenu.items.add(miExpandAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miCollapseLast)
            contextMenu.items.add(miExpandLast)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miDelete)


            miOpen.setOnAction {
                val selectedItems = treeView.selectionModel.selectedItems
                val firstSelectedItem = selectedItems.first()
                if (selectedItems.isNotEmpty() && firstSelectedItem.value is FileItem) {
                    OsUtil.openFile(   File((firstSelectedItem.value as FileItem).absolutePath)   )
                }
            }
            miOpenExplorer.setOnAction {
                val selectedItems = treeView.selectionModel.selectedItems
                val firstSelectedItem = selectedItems.first()
                if (selectedItems.isNotEmpty() && firstSelectedItem.value is FileItem) {
                    OsUtil.openInExplorer(   File((firstSelectedItem.value as FileItem).absolutePath)   )
                }
            }
            miCollapseAll.setOnAction { UiTreeView.collapseAll(treeView) }
            miExpandAll.setOnAction { UiTreeView.expandAll(treeView) }
            miCollapseLast.setOnAction { UiTreeView.collapseLast(treeView) }
            miExpandLast.setOnAction { UiTreeView.expandFirst(treeView) }
            miExpandLast.setOnAction { UiTreeView.expandFirst(treeView) }
            miDelete.setOnAction {
                val selectedFiles = treeView.selectionModel.selectedItems

                // Проверяем, есть ли выделенные элементы
                if (selectedFiles.isEmpty()) {
                    Log.appendTextTimed("No items selected to delete")
                    return@setOnAction // Выход, если нет выделенных файлов
                }

                // Подтверждение удаления
                if (!OsUtil.confirmDelete("  ${selectedFiles.size} files")) {
                    Log.appendTextTimed("Delete Canceled")
                    return@setOnAction // Выход, если удаление отменено
                }

                // Удаление файлов и вывод информации о статусе
                selectedFiles.forEach { treeItem ->
                    val file = treeItem.value as? File ?: return@forEach // Если не File, пропускаем итерацию
                    if (isTestMode) {
                        OsUtil.deleteToTmp(file)
                    } else {
                        file.delete()
                    }
                    treeItem.parent?.children?.remove(treeItem)
                }
            }
            treeView.contextMenu = contextMenu

        }

        //-----------------------------------------------------------------------------Table----------------------
        fun addToTableView(tableView: TableView<*>) {
            val contextMenu = ContextMenu()

//            val miCopy = MenuItem("Copy")
            val miOpen = MenuItem("Open")
            val miOpenExplorer = MenuItem("Open in explorer")
            val miSelectAll = MenuItem("Select All")
            val miDeleteSelected = MenuItem("Delete Selected")       // Works if only TreeView<as File>
//            val miConvertTo = MenuItem("Convert to..")

            // add menu items to menu

//            contextMenu.items.add(miCopy)
            contextMenu.items.add(miOpen)
            contextMenu.items.add(miOpenExplorer)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miSelectAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miDeleteSelected)

//            miCut.setOnAction       {tableView.cut() }
//            miCopy.setOnAction      {     }
//            miPaste.setOnAction     {codeArea.paste()   }


            miOpen.setOnAction {
                val selectedItems = tableView.selectionModel.selectedItems
                val firstSelectedItem = selectedItems.first()
                if (selectedItems.isNotEmpty() && firstSelectedItem is FileItem) {
                    OsUtil.openFile(   File((firstSelectedItem as FileItem).absolutePath)   )
                }
            }
            miOpenExplorer.setOnAction {
                val selectedItems = tableView.selectionModel.selectedItems
                val firstSelectedItem = selectedItems.first()
                if (selectedItems.isNotEmpty() && firstSelectedItem is FileItem) {
                    OsUtil.openInExplorer(  File((firstSelectedItem as FileItem).absolutePath)   )
                }
            }
            miSelectAll.setOnAction { tableView.selectionModel.selectAll() }
            miDeleteSelected.setOnAction {
                val selectedFiles = tableView.selectionModel.selectedItems

                // Проверяем, есть ли выделенные элементы
                if (selectedFiles.isEmpty()) {
                    Log.appendTextTimed("No items selected to delete")
                    return@setOnAction // Выход, если нет выделенных файлов
                }

                // Подтверждение удаления
                if (!OsUtil.confirmDelete("  ${selectedFiles.size} files")) {
                    Log.appendTextTimed("Delete Canceled")
                    return@setOnAction // Выход, если удаление отменено
                }

                // Удаление файлов и вывод информации о статусе
                selectedFiles.forEach { fileItem ->
                    val file = fileItem as? File ?: return@forEach // Если не File, пропускаем итерацию
                    if (isTestMode) {
                        OsUtil.deleteToTmp(file)
                    } else {
                        file.delete()
                    }
//                    tableView.items.remove(fileItem)
                }
                // Удаление выделенных строк из TableView
                tableView.items.removeAll(selectedFiles)
            }
            tableView.contextMenu = contextMenu
        }









        fun addToCodeArea(textArea: TextArea) {

            val contextMenu = ContextMenu()

            val miCut = MenuItem("Cut")
            val miCopy = MenuItem("Copy")
            val miPaste = MenuItem("Paste")
            val miSelectAll = MenuItem("Select All")
            val miUndo = MenuItem("Undo")
            val miRedo = MenuItem("Redo")

            // add menu items to menu
            contextMenu.items.add(miCut)
            contextMenu.items.add(miCopy)
            contextMenu.items.add(miPaste)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miSelectAll)
            contextMenu.items.add(SeparatorMenuItem())
            contextMenu.items.add(miUndo)
            contextMenu.items.add(miRedo)

            miCut.setOnAction { textArea.cut() }
            miCopy.setOnAction { textArea.copy() }
            miPaste.setOnAction { textArea.paste() }
            miSelectAll.setOnAction { textArea.selectAll() }
            miUndo.setOnAction { textArea.undo() }
            miRedo.setOnAction { textArea.redo() }

            textArea.contextMenu = contextMenu


        }



    }






}






