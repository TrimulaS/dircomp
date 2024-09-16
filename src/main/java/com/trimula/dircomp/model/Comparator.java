package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import log.Log;


import java.io.File;

public class Comparator {
//    TreeView treeView1;
//    TreeView treeView2;

    //FileItem largestFile, largestDirectory;
    //FileItem largestFile = new File();
    private int numOfSameFolders = 0,numOfSameFiles = 0, numOfSameIntersection = 0 ;

    public TreeItem<FileItem> root1;
    public TreeItem<FileItem> root2;
    public ObservableList<FileItem> ol1;
    public ObservableList<FileItem> ol2;

    //used in case of chosen by user
    private TreeItem<FileItem> rootFullMatch1 = null;
    private TreeItem<FileItem> rootFullMatch2 = null;
    private TreeItem<FileItem> rootDirOnly1 = null;
    private TreeItem<FileItem> rootDirOnly2 = null;


    private int sameFiles = 0, sameDirectories = 0, SimilarFiles = 0, similarDirectories = 0;
    private int numOfItemsInDir1 = 0, numOfItemsInDir2 = 0;

    public Comparator(){
//        this.treeView1 = treeView1;
//        this.treeView2 = treeView2;

    }
    public void processDirectories(File dir1, File dir2){
        root1 = TreeItemBuilder.getFull(dir1);
        Log.appendText("root 1 finished " + root1.getChildren().size());


        root2 = TreeItemBuilder.getFull(dir2);
        Log.appendText("root 2 finished " + root2.getChildren().size());



//        // Calculating Directories Sizes:
        Log.appendText("Calculating Directories Sizes for folder 1:");
        Log.appendText("Total size before: " + OsUtil.sizeAdopt(root1.getValue().directorySize) + "  ( " + root1.getValue().directorySize + " )");
        TreeItemBuilder.calculateDirectorySize(root1);
        Log.appendText("Total size calculated: " + OsUtil.sizeAdopt(root1.getValue().directorySize) + "  ( " + root1.getValue().directorySize + " )");
//
        Log.appendText("Calculating Directories Sizes for folder 2:");
        TreeItemBuilder.calculateDirectorySize(root2);
        Log.appendText("Total size calculated: " + OsUtil.sizeAdopt(root2.getValue().directorySize) + "  ( " + root2.getValue().directorySize + " )");


        //Compare
        Log.appendText("Starting to compare..." + root2.getChildren().size());
        TreeItemTraverse.each(root1,item1 -> {
            TreeItemTraverse.each(root2,item2 -> {
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

        Log.appendText("Compre complete:");
        if(numOfSameIntersection>0) Log.appendText("(!) Selected directories have intersection: Same file or folder: " + numOfSameIntersection);
        Log.appendText("Same folders: " + numOfSameFolders + " and files:  " + numOfSameFiles);

        //Set style for treeview

    }
    //This Should be run in JavaFX UI thre
    public void fillAllDir1(TreeView treeView){
        treeView.setRoot(root1);
        TreeItemBuilder.configureTreeItemStyle(treeView);
    }
    public void fillAllDir2(TreeView treeView){
        treeView.setRoot(root2);
        TreeItemBuilder.configureTreeItemStyle(treeView);
    }

    //Filters:-------------------------------------------------------------------------------------------
    // Full Match
    public void fillFullMatch1(TreeView treeView){
        if(rootFullMatch1==null){
            rootFullMatch1 = TreeItemTraverse.filterTree(root1,fileItem -> fileItem.same.size() > 0);
        }else{
            treeView.setRoot(rootFullMatch1);
            TreeItemBuilder.configureTreeItemStyle(treeView);
        }
    }
    public void fillFullMatch2(TreeView treeView){
        if(rootFullMatch2==null){
            rootFullMatch2 = TreeItemTraverse.filterTree(root2,fileItem -> fileItem.same.size() > 0);
        }else{
            treeView.setRoot(rootFullMatch2);
            TreeItemBuilder.configureTreeItemStyle(treeView);
        }
    }
    // Full Match
    public void fillDirOnly1(TreeView treeView){
        if(rootDirOnly1==null)
            rootDirOnly1 = TreeItemTraverse.filterTree(root1,FileItem :: isDirectory);
        else{
            treeView.setRoot(rootDirOnly1);
            TreeItemBuilder.configureTreeItemStyle(treeView);
        }
    }
    public void fillDirOnly2(TreeView treeView){
        if(rootDirOnly2==null)
            rootDirOnly2 = TreeItemTraverse.filterTree(root2,FileItem :: isDirectory);
        else{
            treeView.setRoot(rootDirOnly2);
            TreeItemBuilder.configureTreeItemStyle(treeView);
        }
    }


}
