package com.trimula.dircomp.filetree;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileItem extends File {
    //File file;

    public List<FileItem> same;
    public List<FileItem> similar;

    public FileItem(File file){
        super(file.getAbsolutePath());
        //this.file = file;
        same = new LinkedList<>();
        similar =  new LinkedList<>();
    }

    @Override
    public String toString() {
        return "FileItem{" +
                "Name: " + getName() + "\n" +
                "Path: " + getPath() + "\n" +
                (isFile() ?
                        "is file, size: " + FileSizeAdoptFormat.apply( length() ) + "\n" :
                        "is directory\n") +
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

}
