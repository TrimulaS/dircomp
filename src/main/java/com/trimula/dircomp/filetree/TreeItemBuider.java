package com.trimula.dircomp.filetree;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class TreeItemBuider {
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
            private final ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/file.png")));
            private final ImageView folderIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder.png")));

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


}
