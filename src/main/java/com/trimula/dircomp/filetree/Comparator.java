package com.trimula.dircomp.filetree;

import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import log.Log;

import java.io.File;

public class Comparator {
    TreeView treeView1;
    TreeView treeView2;
    public TreeItem<File> root1;
    public TreeItem<File> root2;
    private int sameFiles = 0, sameDirectories = 0, SimilarFiles = 0, similarDirectories = 0;

    public Comparator(TreeView treeView1, TreeView treeView2){
        this.treeView1 = treeView1;
        this.treeView2 = treeView2;

    }
    public void processDirectories(File dir1, File dir2){
        root1 = TreeItemBuilder.getFull(dir1);
        Log.appendText("root 1 finished " + root1.getChildren().size());


        root2 = TreeItemBuilder.getFull(dir2);
        Log.appendText("root 2 finished " + root2.getChildren().size());



        //Compare
//        TreeItemTraverse.each(root1,item1 -> {
//            TreeItemTraverse.each(root2,item2 -> {
//                //For directories
//                if(item1.getValue().isDirectory() && item2.getValue().isDirectory()){
//
//
//                }
//                //For files
//                if(item1.getValue().isFile() && item2.getValue().isFile()){
//                    if(item1.getValue().length()==item1.getValue().length()){
//                        Log.appendText("sameFiles:" + item1.getValue().getPath() + "   -    "  + item2.getValue().getPath() );
//                    }
//                }
//            });
//        });

        //Set style for treeview

    }
    //This Should be run in JavaFX UI thre
    public void fillUpTreeView(){
        treeView1.setRoot(root1);
        treeView2.setRoot(root2);

        TreeItemBuilder.configureTreeItemStyle(treeView1);
        TreeItemBuilder.configureTreeItemStyle(treeView2);
    }
    void conclusionFilesAreSame(){

    }


}
