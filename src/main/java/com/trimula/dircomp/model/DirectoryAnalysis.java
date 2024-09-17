package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.Log;
import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 *      This Class comprise all the information related to Single analyzed Directory
*/

public class DirectoryAnalysis {
    private int numTotal = 0, numDirectories =0, numFiles = 0;


//private int numOfSameFolders = 0,numOfSameFiles = 0, numOfSameIntersection = 0 ;

    public TreeItem<FileItem> root;
    private ObservableList<FileItem> observableList = null;



    //used in case of chosen by user
    private TreeItem<FileItem> rootFullMatch = null;
    private TreeItem<FileItem> rootDirOnly = null;
    private TreeItem<FileItem> rootFileOnly = null;


//private int sameFiles = 0, sameDirectories = 0, SimilarFiles = 0, similarDirectories = 0;
    //private int numOfItemsInDir1 = 0, numOfItemsInDir2 = 0;


    public DirectoryAnalysis (File dir){
        // Создаем корневой элемент TreeItem для текущей директории
        root = new TreeItem<>(new FileItem(dir));

        // Получаем список файлов и директорий внутри текущей директории
        File[] files = dir.listFiles();
        if (files != null) {
            // Проходим по каждому файлу и директории
            for (File file : files) {
                if (file.isDirectory()) {
                    // Если это директория, рекурсивно создаем поддерево
                    TreeItem<FileItem> directoryItem = parseDirectoryToTreeItem(file);
                    root.getChildren().add(directoryItem);
                    numDirectories++;
                } else {
                    // Если это файл, просто добавляем его в корневой элемент как листовой узел
                    TreeItem<FileItem> fileItem = new TreeItem<>(new FileItem(file));
                    root.getChildren().add(fileItem);
                    numFiles++;
                }
            }
        }
        numTotal = numDirectories + numFiles;
        // Log.appendText("Calculating Directories Sizes:");
        calculateDirectorySize(root);
        Log.appendText("\n---------------------------------Processed Folder: " + root.getValue().getName() + "\n" +
                "Path: " + root.getValue().getAbsolutePath() + "\n" +
                "Total size calculated: " + OsUtil.sizeAdopt(root.getValue().directorySize) + //"  ( " + root.getValue().directorySize + " )\n" +
                "Directories: " + numDirectories + "\tFiles: " + numFiles + "\n" +
                "Total: " + numTotal);
    }


    public TreeItem<FileItem> parseDirectoryToTreeItem(File dir){
        // Создаем корневой элемент TreeItem для текущей директории
        TreeItem<FileItem> root = new TreeItem<FileItem>(new FileItem(dir));

        // Получаем список файлов и директорий внутри текущей директории
        File[] files = dir.listFiles();
        if (files != null) {
            // Проходим по каждому файлу и директории
            for (File file : files) {
                if (file.isDirectory()) {
                    // Если это директория, рекурсивно создаем поддерево
                    TreeItem<FileItem> directoryItem = parseDirectoryToTreeItem(file);
                    root.getChildren().add(directoryItem);
                    numDirectories++;
                } else {
                    // Если это файл, просто добавляем его в корневой элемент как листовой узел
                    TreeItem<FileItem> fileItem = new TreeItem<>(new FileItem(file));
                    numFiles++;
                    root.getChildren().add(fileItem);
                }
            }
        }
        return root;
    }

    // Метод для вычисления размера директории и записи этого значения в поле directorySize для каждой директории
    private long calculateDirectorySize(TreeItem<FileItem> treeItem) {
        FileItem fileItem = treeItem.getValue();
        long totalSize = 0;

        // Если это директория, обрабатываем дочерние элементы рекурсивно
        if (fileItem.isDirectory()) {
            for (TreeItem<FileItem> child : treeItem.getChildren()) {
                totalSize += calculateDirectorySize(child);  // Рекурсивный вызов для всех дочерних элементов
            }
            fileItem.directorySize = totalSize;  // Записываем размер в поле directorySize
        } else {
            // Если это файл, просто получаем его размер
            totalSize = fileItem.length();
        }

        return totalSize;  // Возвращаем общий размер для текущего элемента
    }



    public void configureTreeItemStyle(TreeView<FileItem> treeView){

        // Настраиваем отображение имени файла и иконки
        treeView.setCellFactory(tv -> new TreeCell<FileItem>() {

//            //Icons
//            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_32x32.png")));
//            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_32x32.png")));
//
//            private final ImageView fileMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileMultipleW10_32x32.png")));
//            private final ImageView folderMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderMultipleW10_32x32.png")));
//
//            private final ImageView  folderEmptyIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_32x32.png")));
//            private final ImageView  folderEmptyMultipleIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyMultipleW10_32x32.png")));



            //Icons
            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileW10_16x16.png")));
            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderW10_16x16.png")));

            private final ImageView fileMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/fileMultipleW10_16x16.png")));
            private final ImageView folderMultipleIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folderMultipleW10_16x16.png")));

            private final ImageView  folderEmptyIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyW10_16x16.png")));
            private final ImageView  folderEmptyMultipleIcon= new ImageView(new Image(getClass().getResourceAsStream("/icons/folderEmptyMultipleW10_16x16.png")));



            /*@Override*/
            protected void updateItem(FileItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    // Устанавливаем иконки в зависимости от того, файл это или папка
                    if (item.isDirectory()) {
                        setGraphic(folderIcon);
                    } else {
                        setGraphic(fileIcon);
                    }
                }
            }
        });



    }




//    // Метод для получения системной иконки
//    private ImageView getSystemIcon(File file) {
//        // Используем FileSystemView для получения системной иконки
//        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
//
//        // Преобразуем Icon в Image
//        Image image = convertToFxImage(icon);
//        return new ImageView(image);
//    }
//
//    // Метод для конвертации системной иконки в формат JavaFX
//    private Image convertToFxImage(Icon icon) {
//        if (icon instanceof ImageIcon) {
//            java.awt.Image awtImage = ((ImageIcon) icon).getImage();
//            return SwingFXUtils.toFXImage((BufferedImage) awtImage, null);
//        } else {
//            int w = icon.getIconWidth();
//            int h = icon.getIconHeight();
//            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g = image.createGraphics();
//            icon.paintIcon(null, g, 0, 0);
//            g.dispose();
//            return javax.swing.SwingFXUtils.toFXImage(image, null);
//        }
//    }

    // Filtered TreeItems root
    public TreeItem<FileItem> getRootFullMatch() {
        if(rootFullMatch == null) rootFullMatch = TreeItemTraverse.filterTree(root, fileItem -> fileItem.same.size() > 0);
        return rootFullMatch;
    }


    public TreeItem<FileItem> getRootDirOnly() {
        if(rootDirOnly == null) rootDirOnly = TreeItemTraverse.filterTree(root,FileItem :: isDirectory);
        return rootDirOnly;
    }

    public TreeItem<FileItem> getRootFileOnly() {
        if(rootFileOnly == null) rootFileOnly = TreeItemTraverse.filterTree(root,FileItem :: isFile);
        return rootFileOnly;
    }

    public ObservableList<FileItem> getObservableList() {
        if(observableList == null){
            observableList = FXCollections.observableArrayList();;

            TreeItemTraverse.each(root, item->{
                observableList.add( item.getValue() );
            });
        }
        return observableList;
    }

    public int getNumTotal() {
    return numTotal;
    }

    public int getNumDirectories() {
        return numDirectories;
    }

    public int getNumFiles() {
        return numFiles;
    }

}
