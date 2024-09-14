package com.trimula.dircomp.ui;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


public class UiTreeView {

    // Collapse all nodes in the TreeView
    public static <T> void collapseAll(TreeView<T> tv) {
        TreeItem<T> root = tv.getRoot();
        if (root != null) {
            collapseRecursive(root);
        }
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

    // Collapse the first expanded node in every branch from the root
    public static <T> void collapseLast(TreeView<T> tv) {
        TreeItem<T> root = tv.getRoot();
        if (root != null) {
            collapseLastRecursive(root);
        }
    }

    // Expand the first collapsed node in every branch from the root
    public static <T> void expandLast(TreeView<T> tv) {
        TreeItem<T> root = tv.getRoot();
        if (root != null) {
            expandLastRecursive(root);
        }
    }

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

    // Recursive method to collapse the first expanded node in every branch
    private static <T> boolean collapseLastRecursive(TreeItem<T> item) {
        if (item.isExpanded()) {
            item.setExpanded(false);
            return true;
        }
        for (TreeItem<T> child : item.getChildren()) {
            if (collapseLastRecursive(child)) {
                return true;
            }
        }
        return false;
    }

    // Recursive method to expand the first collapsed node in every branch
    private static <T> boolean expandLastRecursive(TreeItem<T> item) {
        if (!item.isExpanded()) {
            item.setExpanded(true);
            return true;
        }
        for (TreeItem<T> child : item.getChildren()) {
            if (expandLastRecursive(child)) {
                return true;
            }
        }
        return false;
    }
}

