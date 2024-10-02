package com.trimula.dircomp.model;


// Inline class To store statistics for directories and filtered representation
public class DirectoryStatistics{
    public int directories = 0, files = 0, directoriesSame = 0, filesSame = 0 , intersection = 0;
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
        directoriesSame = 0;
        filesSame = 0;
        intersection = 0;
    }
    public void calcForFileItem(FileItem fi){
        if(fi.isDirectory()){
            this.directories++;
            if(!fi.getSame().isEmpty())this.directoriesSame++;
        }
        else {
            this.files++;
            if(!fi.getSame().isEmpty())this.filesSame++;
        }
    }
    public void calcForFileItemBeforeCompare(FileItem fi){
        /**
         *  This calculation is for the first parse (before compare)
         */
        if(fi.isDirectory()){
            this.directories++;
        }
        else {
            this.files++;
        }
    }

    @Override
    public String toString() {

        return "Total: " + (directories+files) + "  ( " + (directoriesSame+ filesSame) + " )"+
                "\tFolders: " + directories + "  ( " + directoriesSame + " )"+
                "\tFiles: " + files + "  ( " + filesSame + " ) \t (same)\t Intersection: " + intersection;
    }
}
