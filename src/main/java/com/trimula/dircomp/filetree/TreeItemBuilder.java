package com.trimula.dircomp.filetree;

import javafx.event.Event;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class TreeItemBuilder {
    public static TreeItem<File> getFull(File dir){
        // Создаем корневой элемент TreeItem для текущей директории
        TreeItem<File> root = new TreeItem<>(dir);

        // Получаем список файлов и директорий внутри текущей директории
        File[] files = dir.listFiles();
        if (files != null) {
            // Проходим по каждому файлу и директории
            for (File file : files) {
                if (file.isDirectory()) {
                    // Если это директория, рекурсивно создаем поддерево
                    TreeItem<File> directoryItem = getFull(file);
                    root.getChildren().add(directoryItem);
                } else {
                    // Если это файл, просто добавляем его в корневой элемент как листовой узел
                    TreeItem<File> fileItem = new TreeItem<>(file);

                    root.getChildren().add(fileItem);
                }
            }
        }
        return root;
    }



    public static void configureTreeItemStyle(TreeView<File> treeView){

        // Настраиваем отображение имени файла и иконки
        treeView.setCellFactory(tv -> new TreeCell<File>() {

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



            @Override
            protected void updateItem(File item, boolean empty) {
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


}
