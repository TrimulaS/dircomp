package com.trimula.dircomp.filetree;

import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import log.Log;

import java.io.File;

public class Comparator {
    TreeView treeView1;
    TreeView treeView2;
    public TreeItem<FileItem> root1;
    public TreeItem<FileItem> root2;
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
        TreeItemTraverse.each(root1,item1 -> {
            TreeItemTraverse.each(root2,item2 -> {
                FileItem fi1 = item1.getValue();
                FileItem fi2 = item2.getValue();
                //For directories
                if(fi1.isDirectory() && fi2.isDirectory()){
                    //Log.appendText("same Directories:" + fi1.getPath() + "   -    "  + fi2.getPath() );

                }
                //For files
                if(fi1.isFile() && fi2.isFile()){
                    if(fi1.length()==fi2.length()){
                        fi1.same.add(fi2);
                        fi2.same.add(fi1);
                        //Log.appendText("sameFiles:" + item1.getValue().getPath() + "   -    "  + item2.getValue().getPath() );
                    }
                }
            });
        });

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
