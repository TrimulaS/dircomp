package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import log.Log;
import java.util.concurrent.atomic.AtomicInteger;


import java.io.File;

public class Comparator {
//    TreeView treeView1;
//    TreeView treeView2;

    //FileItem largestFile, largestDirectory;
    //FileItem largestFile = new File();
    private ProgressListener progressListener;
    public DirectoryAnalysis da1;
    public DirectoryAnalysis da2;

    int numOfSameFolders = 0, numOfSameFiles = 0, numOfSameIntersection = 0;

    public Comparator(){
//        this.treeView1 = treeView1;
//        this.treeView2 = treeView2;

    }
    public void processDirectories(File dir1, File dir2){

        da1 = new DirectoryAnalysis(dir1);
        Log.appendText("Directory 1:  Size: " + OsUtil.sizeAdopt(da1.root.getValue().directorySize)+ "\n" + dir1 +
                "\nProcessed:  \n\tDirectories: " + da1.getNumOfDirectories() + "\n\tFiles: " +  da1.getNumOfFiles());

        da2 = new DirectoryAnalysis(dir2);
        Log.appendText("\nDirectory 2:  Size: " + OsUtil.sizeAdopt(da2.root.getValue().directorySize)+ "\n" + dir2 +
                "\nProcessed:  \n\tDirectories: " + da2.getNumOfDirectories() + "\n\tFiles: " +  da2.getNumOfFiles());


        //Compare
        Log.appendText("Starting to compare..." );

        AtomicInteger counter1 = new AtomicInteger(0);
        double  maxIterations = da1.getNumOfItems();
        TreeItemTraverse.each(da1.root,item1 -> {

            counter1.addAndGet(1);

            // Логика для обновления прогресса каждые 1% прогресса
            if (counter1.get() % (maxIterations / 100) == 0) {
                double progress = (double) counter1.get() / maxIterations;

                // Обновляем прогресс в UI потоке, если слушатель установлен
                if (progressListener != null) {
                    Platform.runLater(() -> progressListener.onProgressUpdate(progress));
                }
            }

            TreeItemTraverse.each(da2.root,item2 -> {
                FileItem fi1 = item1.getValue();
                FileItem fi2 = item2.getValue();

                // Ignore File in same location:
                if(fi1.getAbsolutePath()!=fi2.getAbsolutePath()) {
                    //For directories
                    if(fi1.isDirectory() && fi2.isDirectory()){
                        numOfSameFolders ++;
                        //Log.appendText("same Directories:" + fi1.getPath() + "   -    "  + fi2.getPath() );

                    }
                    //For files
                    if(fi1.isFile() && fi2.isFile()){
                        if(fi1.length()==fi2.length()){
                            numOfSameFiles ++;
                            fi1.same.add(fi2);
                            fi2.same.add(fi1);
                            //Log.appendText("sameFiles:" + item1.getValue().getPath() + "   -    "  + item2.getValue().getPath() );
                        }
                    }
                }
                else{
                    // This is intersection: Same file or folder in selected directories
                    numOfSameIntersection++;
                }

            });
        });

        //


        Log.appendText("Compare complete:");
        if(numOfSameIntersection>0) Log.appendText("(!!!) Selected directories have intersection: Same file or folder: " + numOfSameIntersection);
        Log.appendText("Same folders: " + numOfSameFolders + " and files:  " + numOfSameFiles);

        //Set style for treeview

    }




    //This Should be run in JavaFX UI --------------------------------------RunLater
    public void fillAllDir1(TreeView treeView){
        treeView.setRoot(da1.root);
        da1.configureTreeItemStyle(treeView);
    }
    public void fillAllDir2(TreeView treeView){
        treeView.setRoot(da2.root);
        da2.configureTreeItemStyle(treeView);
    }

    //Filters:-------------------------------------------------------------------------------------------
//    // Full Match
//    public void fillFullMatch1(TreeView treeView){
//        if(rootFullMatch1==null){
//            rootFullMatch1 = TreeItemTraverse.filterTree(root1,fileItem -> fileItem.same.size() > 0);
//        }else{
//            treeView.setRoot(rootFullMatch1);
//            //DirectoryAnalysis.configureTreeItemStyle(treeView);
//        }
//    }
//    public void fillFullMatch2(TreeView treeView){
//        if(rootFullMatch2==null){
//            rootFullMatch2 = TreeItemTraverse.filterTree(root2,fileItem -> fileItem.same.size() > 0);
//        }else{
//            treeView.setRoot(rootFullMatch2);
//            //DirectoryAnalysis.configureTreeItemStyle(treeView);
//        }
//    }
//    // Directory Only
//    public void fillDirOnly1(TreeView treeView){
//        if(rootDirOnly1==null)
//            rootDirOnly1 = TreeItemTraverse.filterTree(root1,FileItem :: isDirectory);
//        else{
//            treeView.setRoot(rootDirOnly1);
//            //DirectoryAnalysis.configureTreeItemStyle(treeView);
//        }
//    }
//    public void fillDirOnly2(TreeView treeView){
//        if(rootDirOnly2==null)
//            rootDirOnly2 = TreeItemTraverse.filterTree(root2,FileItem :: isDirectory);
//        else{
//            treeView.setRoot(rootDirOnly2);
//            //DirectoryAnalysis.configureTreeItemStyle(treeView);
//        }
//    }
//    // Directory Only
//    public void fillFileOnly1(TreeView treeView){
//        if(rootFileOnly1==null)
//            rootFileOnly1 = TreeItemTraverse.filterTree(root1,FileItem :: isFile);
//        else{
//            treeView.setRoot(rootFileOnly1);
//            //TreeItemBuilder.configureTreeItemStyle(treeView);
//        }
//    }
//    public void fillFileOnly2(TreeView treeView){
//        if(rootFileOnly2==null)
//            rootFileOnly2 = TreeItemTraverse.filterTree(root2,FileItem :: isFile);
//        else{
//            treeView.setRoot(rootFileOnly2);
//            //DirectoryAnalysis.configureTreeItemStyle(treeView);
//        }
//    }

    // Interface to register Progress update to UI
    public interface ProgressListener {
        void onProgressUpdate(double progress);
    }
    // Метод для установки слушателя прогресса
    public void setProgressListener(ProgressListener listener) {
        this.progressListener = listener;
    }


}
