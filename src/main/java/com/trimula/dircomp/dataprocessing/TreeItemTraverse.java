package com.trimula.dircomp.dataprocessing;

import javafx.scene.control.TreeItem;

public class TreeItemTraverse<T> {

    // Метод для рекурсивного обхода всех элементов дерева
    public static <T> void each(TreeItem<T> root, Processing<T> pr) {
        // Обработка текущего элемента
        pr.apply(root);

        // Рекурсивный вызов для всех дочерних элементов
        if (root.getChildren() != null) {
            for (TreeItem<T> child : root.getChildren()) {
                each(child, pr);
            }
        }
    }

    // Интерфейс для обработки каждого узла
    // Делаем интерфейс public, чтобы он был доступен за пределами этого файла
    public interface Processing<T> {
        // Метод для обработки каждого TreeItem
        void apply(TreeItem<T> ti);
    }
}


//Usage
// Используем статический метод, не создавая объект
//        TreeItemTraverse.each(root, item -> {
//        System.out.println("Processing item: " + item.getValue());
//        });

//      TreeItemTraverse.each(root1,item -> {});