package com.trimula.dircomp.dataprocessing;

import javafx.scene.control.TreeItem;

import java.util.function.Predicate;

public class TreeItemTraverse<T> {
    //-----------------------------------------------------------------------------------------------------------------
    // Method to parse all the tree items reqursivelly and perform some actions on each i
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
    //Usage
    // Используем статический метод, не создавая объект
    //        TreeItemTraverse.each(root, item -> {
    //        System.out.println("Processing item: " + item.getValue());
    //        });
    //      TreeItemTraverse.each(root1,item -> {});

    //-----------------------------------------------------------------------------------------------------------------
    // Рекурсивная функция для поиска TreeItem по значению FileItem
    public static <T> TreeItem<T> findByValue(TreeItem<T> treeItem, T value) {

        if (treeItem.getValue() == value) return treeItem;

        for (TreeItem<T> child : treeItem.getChildren()) {
            TreeItem<T> result = findByValue(child, value);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    //-----------------------------------------------------------------------------------------------------------------
    /**
     * Рекурсивная функция для фильтрации дерева.
     * @param sourceRoot корневой узел исходного дерева
     * @param filterPredicate условие фильтрации узлов
     * @return новое дерево, содержащее только те узлы, которые соответствуют условию
     */
    public static <T> TreeItem<T> filterTree(TreeItem<T> sourceRoot, Predicate<T> filterPredicate) {
        T value = sourceRoot.getValue();

        // If the Current Item value not match the condition, return null
        if (!filterPredicate.test(value)) {
            return null;
        }

        // if it is match - create new Item to add to result tree
        TreeItem<T> filteredRoot = new TreeItem<>(value);

        // Проходим по дочерним узлам и фильтруем их рекурсивно
        for (TreeItem<T> child : sourceRoot.getChildren()) {
            TreeItem<T> filteredChild = filterTree(child, filterPredicate);
            if (filteredChild != null) {
                filteredRoot.getChildren().add(filteredChild);
            }
        }

        return filteredRoot;
    }
    // Usage:
    // TreeItem<File> rootIsFile = filterTree(root, File::isFile);
    // TreeItem<File> rootIsDirectory = filterTree(root, File::isDirectory);
    // TreeItem<File> rootIsGreaterThan10MB = filterTree(root, file -> file.length() > 10 * 1024 * 1024);
//-----------------------------------------------------------------------------------------------------------------

}

