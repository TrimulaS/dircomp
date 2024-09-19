package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.application.Platform;
import javafx.scene.control.TreeView;
import com.trimula.dircomp.dataprocessing.Log;
import java.util.concurrent.atomic.AtomicInteger;


import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class Comparator {
//    TreeView treeView1;
//    TreeView treeView2;

    //FileItem largestFile, largestDirectory;
    //FileItem largestFile = new File();
    private BeforeDirectoryParseListener beforeDirectoryParseListener;
    private BeforeCompareListener beforeCompareListener;
    private CompareProgressListener compareProgressListener;



    public DirectoryAnalysis da1;
    public DirectoryAnalysis da2;

    int numOfSameFolders = 0, numOfSameFiles = 0, numOfSameIntersection = 0;

    public Comparator(){
//        this.treeView1 = treeView1;
//        this.treeView2 = treeView2;

    }
    public void processDirectories(File dir1, File dir2){
        // Implement Listener
        if (beforeCompareListener != null) {
            Platform.runLater(() -> beforeCompareListener.onBeforeCompare());
        }

        da1 = new DirectoryAnalysis(dir1);
        Log.appendText("Directory 1:  Size: " + OsUtil.sizeAdopt(da1.root.getValue().directorySize)+ "\n" + dir1 +
                "\nProcessed:  \n\tDirectories: " + da1.getNumDirectories() + "\n\tFiles: " +  da1.getNumFiles());

        da2 = new DirectoryAnalysis(dir2);
        Log.appendText("\nDirectory 2:  Size: " + OsUtil.sizeAdopt(da2.root.getValue().directorySize)+ "\n" + dir2 +
                "\nProcessed:  \n\tDirectories: " + da2.getNumDirectories() + "\n\tFiles: " +  da2.getNumFiles());



        //Compare
        Log.appendText("Starting to compare..." );
        // Implement Listener
        if (beforeCompareListener != null) {
            Platform.runLater(() -> beforeCompareListener.onBeforeCompare());
        }

        AtomicInteger counter1 = new AtomicInteger(0);
        double  maxIterations = da1.getNumTotal();
        AtomicReference<Double> lastProgress = new AtomicReference<>(-1.0);

        TreeItemTraverse.each(da1.root,item1 -> {

            counter1.incrementAndGet();

            double progress = (double) counter1.get() / maxIterations;

            // Проверяем, изменился ли прогресс на 5%
            if (compareProgressListener != null && (int)(progress * 100) >= (int)(lastProgress.get() * 100 + 0.1)) { // <----- +1 Delta whe to send
                lastProgress.set(progress);  // обновляем последний прогресс через AtomicReference
                Platform.runLater(() -> compareProgressListener.onProgressUpdate(progress, (int) (progress * 100) + "%"));
            }
//            if (compareProgressListener != null) {
//                Platform.runLater(() -> compareProgressListener.onProgressUpdate(counter1.get()/maxIterations, counter1.get()+" "+maxIterations+"  " + da2.getNumTotal()));
//            }

//            // Логика для обновления прогресса каждые 1% прогресса
//            if (counter1.get() % (maxIterations / 100) == 0) {
//                double progress = (double) counter1.get() / maxIterations;
//
//                // Обновляем прогресс в UI потоке, если слушатель установлен
//                if (compareProgressListener != null) {
//                    Platform.runLater(() -> compareProgressListener.onProgressUpdate(progress, ""));
//                }
//            }





            TreeItemTraverse.each(da2.root,item2 -> {
                FileItem fi1 = item1.getValue();
                FileItem fi2 = item2.getValue();

                // Ignore File in same location:...................................................Comparison Logic
                if(fi1.getAbsolutePath()!=fi2.getAbsolutePath()) {

                    if(fi1.isDirectory() && fi2.isDirectory()){

                        //For directories
                        if(FileItem.Companion.areSimilar(fi1,fi2)){

                            numOfSameFolders ++;
                            fi1.same.add(fi2);
                            fi2.same.add(fi1);
                        }


                    }

                    if(fi1.isFile() && fi2.isFile()){

                        //For files
                        if(FileItem.Companion.areSimilar(fi1,fi2)){

                            numOfSameFiles ++;
                            fi1.same.add(fi2);
                            fi2.same.add(fi1);
                        }

//                        //For files
//                        if(FileItem.Companion.areSame(fi1,fi2)){
//                            numOfSameFiles ++;
//                            fi1.same.add(fi2);
//                            fi2.same.add(fi1);
//                        }


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
        Log.appendText("--For debug: Items in Path1: " + counter1.get());
        //Set style for treeview

    }




    //This Should be run in JavaFX UI --------------------------------------RunLater
    public void fillAllDir1(TreeView treeView){
        treeView.setRoot(da1.root);
        da1.root.setExpanded(true);
        da1.configureTreeItemStyle(treeView);
    }
    public void fillAllDir2(TreeView treeView){
        treeView.setRoot(da2.root);
        da2.root.setExpanded(true);
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

    //-------------------------------------------------------------Block Of Listeners to Update UI

    // Beginning - before offered directories parse
    public interface BeforeDirectoryParseListener{
        void onBeforeDirectoryParse();
    }
    public void setBeforeDirectoryParseListener(BeforeDirectoryParseListener listener) {
        this.beforeDirectoryParseListener = listener;
    }

    // After directories parsed but before comparison started
    public interface BeforeCompareListener {
        void onBeforeCompare();
    }
    public void setBeforeCompareListener(BeforeCompareListener listener) {
        this.beforeCompareListener = listener;
    }


    // Compare ongoing - update the progress
    public interface CompareProgressListener {
        void onProgressUpdate(double progress,String text);
    }
    public void setCompareProgressListener(CompareProgressListener listener) {
        this.compareProgressListener = listener;
    }



}
