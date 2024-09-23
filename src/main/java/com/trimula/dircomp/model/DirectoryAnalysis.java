package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.Log;
import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.beans.value.ChangeListener;
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
    public DirectoryStatistics statisticDirOnly = new DirectoryStatistics();
        //Filtered table:
    private FilteredList<FileItem> filteredList = null;
    public SortedList<FileItem> filteredSortedList = null;
    public DirectoryStatistics statisticFilteredList = new DirectoryStatistics();



    // Inline class To store statistics for directories and filtered representation
    public class DirectoryStatistics{
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



            //Icons
            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_16x16.png")));
            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_16x16.png")));

            private final ImageView fileMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileMultipleW10_16x16.png")));
            private final ImageView folderMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderMultipleW10_16x16.png")));

            private final ImageView  folderEmptyIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_16x16.png")));
            private final ImageView  folderEmptyMultipleIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyMultipleW10_16x16.png")));



            /*@Override*/
            protected void updateItem(FileItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    // Устанавливаем иконки в зависимости от того, файл это или папка
                    if (item.isDirectory()) {
                        setGraphic(folderIcon);
                    } else {
                        setGraphic(fileIcon);
                    }
                }
            }
        });



    }



    public TreeItem<FileItem> getRootDirOnly() {

        if(rootDirOnly == null) rootDirOnly = TreeItemTraverse.filterTree(root,FileItem :: isDirectory);
        //Calculate statistics
        statisticDirOnly.reset();
        TreeItemTraverse.each(rootDirOnly, ti->{
            if(ti.getValue().isDirectory())statisticDirOnly.directories++;
            else statisticDirOnly.files++;
        });



//        AtomicInteger rootCount= new AtomicInteger();
//        AtomicInteger filteredCount = new AtomicInteger();
//        TreeItemTraverse.each(root, ti->{
//            rootCount.getAndIncrement();
//        });
//        TreeItemTraverse.each(root, ti->{
//            filteredCount.getAndIncrement();
//        });
//        Log.appendTextTimed("Filtering dir only: " +filteredCount.get()+ "  total: "+rootCount.get());
        rootDirOnly.setExpanded(true);
        return rootDirOnly;
    }


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

                statisticFilteredList.reset();
                for (FileItem fi : filteredList) {
                    if (fi.isDirectory()) {
                        statisticFilteredList.directories++;
                    } else {
                        statisticFilteredList.files++;
                    }
                }
            });
            // Привязка компаратора списка и таблицы для корректной работы сортировки
            filteredSortedList = new  SortedList(filteredList);
            //sortedList.comparatorProperty().bind(tableView.comparatorProperty())
        }
        return filteredList;
    }
}


