package com.trimula.dircomp.filetree;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileItem {
    File file;

    List<FileItem> same;
    List<FileItem> similar;

    public FileItem(File file){
        this.file = file;
        same = new LinkedList<>();
        similar =  new LinkedList<>();
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "Name: " + file.getName() + "\n" +
                "Path: " + file.getPath() + "\n" +
                (file.isFile() ?
                        "is file, size: " + FileSizeAdoptFormat.apply( file.length() ) + "\n" :
                        "is directory\n") +
                "Number of same items: " + same.size() + "\n" +
                "Number of similar items: " + similar.size() + "\n" ;
    }
}
