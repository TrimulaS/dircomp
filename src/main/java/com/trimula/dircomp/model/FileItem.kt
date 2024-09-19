package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.OsUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileItem extends File {
    public long directorySize = 0;

    public ObservableList<FileItem> same;
    public ObservableList<FileItem> similar;


    // Конструктор FileItem, используя путь к файлу
    public FileItem(String pathname) {
        super(pathname);
        init();
    }

    // Конструктор FileItem с File
    public FileItem(File file) {
        super(file.getPath());
        init();
    }

    private  void init(){
        same = FXCollections.observableArrayList();
        similar =  FXCollections.observableArrayList();
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "Name: " + getName() + "\n" +
                "Path: " + getPath() + "\n" +
                (isFile() ?
                        "is file,      size: " + OsUtil.sizeAdopt( length() ) + "\n" :
                        "is directory, size: " + OsUtil.sizeAdopt( directorySize) +"\n") +
                "Number of same items: " + same.size() + "\n" +
                "Number of similar items: " + similar.size() + "\n" +
                (same.size()>0 ?
                        "Same:" + "\n" +listToString(same):"" );
    }

    private String listToString(List<FileItem> ll){
        StringBuilder result = new StringBuilder();
        for (FileItem element : ll) {
            result.append(element.getAbsolutePath()).append("\n");
        }
        return result.toString();
    }

//    public static getIco(){
//
//    }

}
