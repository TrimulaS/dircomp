package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.Log;
import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 *      This Class comprise all the information related to Single analyzed Directory
*/

public class DirectoryAnalysis {

    // Stores:
        //Tree
        //Observable list
    // Statistics
        //statistic
        //filteredStatistic

    public TreeItem<FileItem> root;
    public DirectoryStatistics statistic = new DirectoryStatistics();
        //Table - statistic same to root
    private ObservableList<FileItem> observableList =  FXCollections.observableArrayList();  //null;
    // Statistics same to root
        // Filtered tree
    private TreeItem<FileItem> rootDirOnly = null;
    public DirectoryStatistics statisticDirOnly = null;

    private TreeItem<FileItem> rootSameFileOnly = null;
    public DirectoryStatistics statisticSameFileOnly = null;
        //Filtered table:
    private FilteredList<FileItem> filteredList = null;
    public SortedList<FileItem> filteredSortedList = null;
    public DirectoryStatistics statisticFilteredList = new DirectoryStatistics();



    // Inline class To store statistics for directories and filtered representation
    public static class DirectoryStatistics{
        public int directories = 0, files = 0;
//        public void  set(int folders, int files){
//            this.directories = folders;
//            this.files = files;
//        }
        public int getTotal(){
            return directories + files;
        }
        public void reset(){
            directories = 0;
            files = 0;
        }
    }

    // I. Parse directory - fill up root
    public DirectoryAnalysis (File dir){
        statistic.reset();
        root = parseDirectoryToTreeAndList(dir);

        Log.appendTextTimed("\n------------ Processed Folder: <<   " + root.getValue().getName() +  "   >>    size: " + OsUtil.sizeAdopt(root.getValue().length) + "\n" + //"  ( " + root.getValue().directorySize + " )\n" + "\n" +
                "Path: " + root.getValue().getAbsolutePath() + "\n" +
                "Directories: " + statistic.directories + "\tFiles: " + statistic.files + "\tTotal: " + statistic.getTotal() + "\n" +
                "....Observable list: " + observableList.size() );

    }


    public TreeItem<FileItem> parseDirectoryToTreeAndList(File file){
        if (file == null || !file.exists()) {
            return null;
        }

        // +++ Creating FileItem item +++
        FileItem fileItem = new FileItem(file);
        TreeItem<FileItem> treeItem = new TreeItem<>(fileItem);
        observableList.add(fileItem);

        long totalSize = 0;

        // it is ---File---:
        if (file.isFile()) {
            statistic.files ++;
            return treeItem;
        }

        // it is ---Directory---
        statistic.directories++;

        // Получаем все файлы и папки в текущей директории
        File[] files = file.listFiles();
        if (files != null) {
            if(files.length > 0)fileItem.setEmpty(false);
            for (File f : files) {

                TreeItem<FileItem> childTreeItem = parseDirectoryToTreeAndList(f);
                if (childTreeItem != null) {
                    treeItem.getChildren().add(childTreeItem);
                    totalSize += childTreeItem.getValue().length();
                }
            }
            treeItem.getValue().length = totalSize;
        }
        return treeItem;

    }


    public void configureTreeItemStyle(TreeView<FileItem> treeView){

        // Настраиваем отображение имени файла и иконки
        treeView.setCellFactory(tv -> new TreeCell<FileItem>() {

//            //Icons
//            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_32x32.png")));
//            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_32x32.png")));
//
//            private final ImageView fileMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileMultipleW10_32x32.png")));
//            private final ImageView folderMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderMultipleW10_32x32.png")));
//
//            private final ImageView  folderEmptyIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_32x32.png")));
//            private final ImageView  folderEmptyMultipleIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyMultipleW10_32x32.png")));



//            //Icons
//            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_16x16.png")));
//            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_16x16.png")));
//            private final ImageView folderEmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_16x16.png")));
//
//            private final ImageView fileX2Icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_x2__16x16.png")));
//            private final ImageView folderX2Icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10r_x2.png")));
//            private final ImageView folderX2EmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_x2__16x16.png")));
//
//            private final ImageView fileX3Icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_x3__16x16.png")));
//            private final ImageView folderX3Icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_x3__16x16.png")));
//            private final ImageView folderX3EmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_x3__16x16.png")));
//
//            private final ImageView fileXNIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_xN__16x16.png")));
//            private final ImageView folderXNIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_xN__16x16.png")));
//            private final ImageView folderXNEmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_xN__16x16.png")));

            private final ImageView fileSuspectedIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileSuspectedW10__16x16.png")));


            /*@Override*/
            protected void updateItem(FileItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    // Устанавливаем иконки в зависимости от того, файл это или папка
                    setGraphic(item.getIco());
                }
            }
        });

    }




    //-------------------------------------------------------------------------------------filtered Trees and Lists---------
    public TreeItem<FileItem> getRootDirOnly() {

        if(rootDirOnly == null) {
            rootDirOnly = TreeItemTraverse.filterTree(root,FileItem :: isDirectory);

            //Calculate statistics
            statisticDirOnly = new DirectoryStatistics();
            TreeItemTraverse.each(rootDirOnly, ti->{
                if(ti.getValue().isDirectory())statisticDirOnly.directories++;
                else statisticDirOnly.files++;
            });
        }



        rootDirOnly.setExpanded(true);
        return rootDirOnly;
    }

    //.......................................................................build root with same files only...........
    public TreeItem<FileItem> getRootSameFileOnly(){
        if(rootSameFileOnly==null){
            rootSameFileOnly = filterTreeItemByIsMultiple(root)
;
            //Calculate statistics
            statisticSameFileOnly = new DirectoryStatistics();
            TreeItemTraverse.each(rootSameFileOnly, ti->{
                if(ti.getValue().isDirectory())statisticSameFileOnly.directories++;
                else statisticSameFileOnly.files++;
            });
        }
        rootSameFileOnly.setExpanded(true);
        return rootSameFileOnly;
    }



    // Function to filter TreeItem<FileItem> nodes where any node or its descendants have isMultiple == true
    private TreeItem<FileItem> filterTreeItemByIsMultiple(TreeItem<FileItem> original) {
        // Create a new root for the filtered tree
        TreeItem<FileItem> filteredRoot = new TreeItem<>(original.getValue());

        // Recursively filter the children and build the tree
        if (filterChildren(original, filteredRoot)) {
            return filteredRoot; // If any valid children were added, return the filtered root
        } else {
            return null; // If no valid children were added and root is not valid, return null
        }
    }

    // Helper recursive function to filter children and add to the new TreeItem
    // Returns true if the filteredNode should be kept (i.e., it has isMultiple == true or contains children that have isMultiple == true)
    private boolean filterChildren(TreeItem<FileItem> originalNode, TreeItem<FileItem> filteredNode) {
        boolean hasValidChild = false;

        for (TreeItem<FileItem> child : originalNode.getChildren()) {
            TreeItem<FileItem> newChild = new TreeItem<>(child.getValue());

            // Recursively process the child's children
            boolean childHasValidDescendant = filterChildren(child, newChild);

            // If this child has isMultiple == true or has valid descendants, add it to the filteredNode
            if (!child.getValue().getSame().isEmpty() || childHasValidDescendant) {
                filteredNode.getChildren().add(newChild);
                hasValidChild = true;  // Mark that we found a valid child
            }
        }

        // Return true if this node has valid children or if it itself is valid
        return !originalNode.getValue().getSame().isEmpty() || hasValidChild;
    }


//..............................................................................................................

    public ObservableList<FileItem> getObservableList() {
//        if(observableList == null){
//            observableList = FXCollections.observableArrayList();
//
//            TreeItemTraverse.each(root, item->{
//                observableList.add( item.getValue() );
//            });
//        }
        return observableList;
    }

    // Getter for Filtered list - if i is not requested filtered list wil not be created
    public FilteredList<FileItem> getFilteredList() {
        if (filteredList == null) {
            filteredList = new FilteredList<>(getObservableList(), fileItem -> true); // Используем лямбда для предиката


            //Listener to calculate statistics when predicate updated, e.g. when filteredList.setPredicate { fileItem -> filterMatch(fileItem) )
            filteredList.predicateProperty().addListener((observable, oldValue, newValue) -> {
                statisticFilteredList = getStatisticFromObservableList(filteredSortedList);
//                statisticFilteredList.reset();
//                for (FileItem fi : filteredList) {
//                    if (fi.isDirectory()) {
//                        statisticFilteredList.directories++;
//                    } else {
//                        statisticFilteredList.files++;
//                    }
//                }
            });
            // Привязка компаратора списка и таблицы для корректной работы сортировки
            filteredSortedList = new  SortedList(filteredList);
            //sortedList.comparatorProperty().bind(tableView.comparatorProperty())
        }
        return filteredList;
    }
    public static DirectoryStatistics getStatisticFromObservableList(ObservableList<FileItem> ol){
        DirectoryStatistics ds = new DirectoryStatistics();
        for (FileItem fi : ol) {
            if (fi.isDirectory()) {
                ds.directories++;
            } else {
                ds.files++;
            }
        }
        return ds;
    }
}


