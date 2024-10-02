package com.trimula.dircomp.model;

import javafx.application.Platform;
import javafx.scene.control.TreeView;
import com.trimula.dircomp.dataprocessing.Log;
import java.util.concurrent.atomic.AtomicInteger;


import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class Comparator {

    private BeforeDirectoryParseListener beforeDirectoryParseListener;
    private BeforeCompareListener beforeCompareListener;
    private CompareProgressListener compareProgressListener;

    

    public DirectoryAnalysis da1;
    public DirectoryAnalysis da2;

    int numOfSameDirectories = 0, numOfSameFiles = 0, numOfSameIntersection = 0;
    public int sameListLimit = 20;

    public void processDirectories(File dir1, File dir2){
        Log.appendTextTimed("Starting processing directories");
        // Implement Listener
        if (beforeCompareListener != null) {
            Platform.runLater(() -> beforeCompareListener.onBeforeCompare());
        }

        da1 = new DirectoryAnalysis(dir1);
//        Log.appendTextTimed("Directory 1:  Size: " + OsUtil.sizeAdopt(da1.root.getValue().directorySize)+ "\n" + dir1 +
//                "\nProcessed:  \n\tDirectories: " + da1.rooStat.directories + "\n\tFiles: " +  da1.rooStat.files);

        da2 = new DirectoryAnalysis(dir2);
//        Log.appendTextTimed("\nDirectory 2:  Size: " + OsUtil.sizeAdopt(da2.root.getValue().directorySize)+ "\n" + dir2 +
//                "\nProcessed:  \n\tDirectories: " + da2.rooStat.directories + "\n\tFiles: " +  da2.rooStat.files);
//

        //Compare - tree
        Log.appendTextTimed("Starting to compare (ObservableLists) ..." );
        // Implement Listener
        if (beforeCompareListener != null) {
            Platform.runLater(() -> beforeCompareListener.onBeforeCompare());
        }

        AtomicInteger counter1 = new AtomicInteger(0);
        double  maxIterations = da1.statistic.getTotal();
        AtomicReference<Double> lastProgress = new AtomicReference<>(-1.0);

        for (FileItem fi1 : da1.getObservableList() ) {
            counter1.incrementAndGet();
            double progress = (double) counter1.get() / maxIterations;

            // Sending progress when its growth more than progressDelta
            double progressDelta = 0.1;
            if (compareProgressListener != null && (int)(progress * 100) >= (int)(lastProgress.get() * 100 + progressDelta)) { // <----- +1 Delta whe to send
                Platform.runLater(() -> compareProgressListener.onProgressUpdate(progress, (int) (progress * 100) + "%"));
                lastProgress.set(progress);  // обновляем последний прогресс через AtomicReference

            }

            for (FileItem fi2 : da2.getObservableList() ) {



                // Ignore File in same location:...................................................Comparison Logic
                // Suspected checked during creation of FileIem in constructor
                if(!fi1.getAbsolutePath().equals(fi2.getAbsolutePath())) {
                    if(fi1.isDirectory() && fi2.isDirectory()){

                        //For directories
                        if(fi1.sameTo(fi2)){      //areSimilar(fi1,fi2)){

                            numOfSameDirectories++;
                            if(fi1.getSame().size() < sameListLimit) fi1.getSame().add(fi2);
                            if(fi2.getSame().size() < sameListLimit) fi2.getSame().add(fi1);
                        }
                    }

                    if(!fi1.isDirectory() && !fi2.isDirectory()){

                        //For files
                        if(fi1.sameTo(fi2)){         //areSimilar(fi1,fi2)){

                            numOfSameFiles ++;
                            if(fi1.getSame().size() < sameListLimit) fi1.getSame().add(fi2);
                            if(fi2.getSame().size() < sameListLimit) fi2.getSame().add(fi1);
                        }

                    }
                }
                else{
                    // This is intersection: Same file or folder in selected directories
                    numOfSameIntersection++;
                }

            }
        }
        //Update statistics:
        da1.statistic.directoriesSame = numOfSameDirectories;
        da1.statistic.filesSame = numOfSameFiles;
        da1.statistic.intersection = numOfSameIntersection;
        da2.statistic.directoriesSame = numOfSameDirectories;
        da2.statistic.filesSame = numOfSameFiles;
        da2.statistic.intersection = numOfSameIntersection;
        Log.appendText("Path1 " + da1.statistic.toString());
        Log.appendText("Path2 " + da2.statistic.toString());


        Log.appendTextTimed("Compare complete:");
        if(numOfSameIntersection>0) Log.appendText("(!!!) Selected directories have intersection: " + numOfSameIntersection);
        Log.appendText("Same folders: " + numOfSameDirectories + " and files:  " + numOfSameFiles);
        Log.appendText("--For debug: Items in Path1: " + counter1.get());
        //Set style for treeview

        //Todo: Double check statistics:
        da1.statistic.reset();
        for (FileItem fi : da1.getObservableList() ) {
            da1.statistic.calcForFileItem(fi);
        }
        da2.statistic.reset();
        Log.appendText("Path1 " + da1.statistic.toString());
        for (FileItem fi : da2.getObservableList() ) {
            da2.statistic.calcForFileItem(fi);
        }
        Log.appendText("Path2 " + da2.statistic.toString());

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
