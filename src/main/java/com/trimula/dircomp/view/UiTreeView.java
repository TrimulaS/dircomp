package com.trimula.dircomp.view;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


public class UiTreeView {

    // Collapse all nodes in the TreeView
    public static <T> void collapseAll(TreeView<T> tv) {
        TreeItem<T> root = tv.getRoot();
        if (root != null) {
            collapseRecursive(root);
        }
        tv.getRoot().setExpanded(true);
    }

    // Expand all nodes in the TreeView
    public static <T> void expandAll(TreeView<T> tv) {
        TreeItem<T> root = tv.getRoot();
        if (root != null) {
            expandRecursive(root);
        }
    }

    // Collapse the selected item
    public static <T> void collapseSelected(TreeView<T> tv) {
        TreeItem<T> selectedItem = tv.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isExpanded()) {
            selectedItem.setExpanded(false);
        }
    }

    // Expand the selected item
    public static <T> void expandSelected(TreeView<T> tv) {
        TreeItem<T> selectedItem = tv.getSelectionModel().getSelectedItem();
        if (selectedItem != null && !selectedItem.isExpanded()) {
            selectedItem.setExpanded(true);
        }
    }

    // Collapse the first expanded node in every branch from the root-------------------
    public static <T> void collapseLast(TreeView<T> tv) {
        TreeItem<?> treeItem = tv.getSelectionModel().getSelectedItem();
        if(treeItem.isExpanded())collapseIfNoExpandedChildren(treeItem);
    }

    // Recursive method to collapse the first expanded node in every branch
    public static void collapseIfNoExpandedChildren(TreeItem<?> treeItem) {

        boolean allChildrenCollapsedOrLeaf = true;
        // Check if it contains any expanded
        for (TreeItem<?> child : treeItem.getChildren()) {
            if(child.isExpanded() ){
                allChildrenCollapsedOrLeaf = false;
                collapseIfNoExpandedChildren(child);
            }
        }
        if(allChildrenCollapsedOrLeaf)treeItem.setExpanded(false);


    }


//    // Recursive method to collapse the first expanded node in every branch
//    private static <T> boolean collapseLastRecursive(TreeItem<T> item) {
//        if (item.isExpanded()) {
//            item.setExpanded(false);
//            return true;
//        }
//        for (TreeItem<T> child : item.getChildren()) {
//            if (collapseLastRecursive(child)) {
//                return true;
//            }
//        }
//        return false;
//    }

    // Expand the first collapsed node in every branch from the root--------------------

    public static void expandFirst(TreeView<?> tv) {
        TreeItem<?> treeItem = tv.getSelectionModel().getSelectedItem();
        expandVisibleNodes( treeItem);
    }

    static void expandVisibleNodes(TreeItem<?> treeItem ) {
        // Если узел раскрыт, продолжаем анализировать вложенные узлы
        if (treeItem.isExpanded()) {
            for (TreeItem<?> child : treeItem.getChildren()) {
                expandVisibleNodes(child); // Рекурсивный вызов для вложенных узлов
            }
        } else {
            // Если узел свернут, раскрываем его, но не продолжаем анализ вложений
            treeItem.setExpanded(true);
        }
    }


//    public static <T> void expandFirst1(TreeView<T> tv) {
//        TreeItem<T> root = tv.getRoot();
//        if (root != null) {
//            expandFirstRecursive(root);
//        }
//    }
//    // Recursive method to expand the first collapsed node in every branch
//    private static <T> boolean expandFirstRecursive(TreeItem<T> item) {
//        if (!item.isExpanded()) {
//            item.setExpanded(true);
//            return true;
//        }
//        for (TreeItem<T> child : item.getChildren()) {
//            if (expandFirstRecursive(child)) {
//                return true;
//            }
//        }
//        return false;
//    }

    // Recursive method to collapse all nodes in the TreeItem
    private static <T> void collapseRecursive(TreeItem<T> item) {
        if (item.isExpanded()) {
            item.setExpanded(false);
        }
        for (TreeItem<T> child : item.getChildren()) {
            collapseRecursive(child);
        }
    }

    // Recursive method to expand all nodes in the TreeItem
    private static <T> void expandRecursive(TreeItem<T> item) {
        if (!item.isExpanded()) {
            item.setExpanded(true);
        }
        for (TreeItem<T> child : item.getChildren()) {
            expandRecursive(child);
        }
    }




}

