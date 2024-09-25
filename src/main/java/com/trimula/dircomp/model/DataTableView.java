package com.trimula.dircomp.model;

import com.trimula.dircomp.dataprocessing.OsUtil;
import com.trimula.dircomp.dataprocessing.TreeItemTraverse;
import com.trimula.dircomp.view.ContentMenu;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataTableView {

    // Fill Up TableView with the same Items Only
    public static void fillTableViewWithSameFiles(TreeItem<FileItem> root, FileItem fileItem1, TableView<FileItem> targetTableView) {
        // Получаем размер целевого файла
        long fileItemSize1;
        if(fileItem1.isDirectory())fileItemSize1 = fileItem1.length;
            else    fileItemSize1 = fileItem1.length();

        // Очищаем TableView перед добавлением новых результатов
        targetTableView.getItems().clear();

        // Создаем список для хранения найденных файлов
        ObservableList<FileItem> matchingFiles = FXCollections.observableArrayList();

        // Рекурсивный вызов для поиска файлов
        TreeItemTraverse.each(root, item ->{
            FileItem fileItem2 = item.getValue();
            boolean isSame = false;
            if(fileItem2.isDirectory()){
                if(fileItemSize1==fileItem2.length && fileItem1.getName().equals(fileItem2.getName())) isSame=true;
                //Log.appendText("Found same directory: " + fileItem2.getName());
            }
            else{
                if(fileItemSize1==fileItem2.length() && fileItem1.getName().equals(fileItem2.getName())) isSame=true;
                //Log.appendText("Found same file: " + fileItem2.getName());
            }

            if (isSame) matchingFiles.add(fileItem2);

        });


        // Добавляем найденные файлы в TableView
        targetTableView.setItems(matchingFiles);
    }

    public static void setupTableView(TableView<FileItem> tableView){
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ContentMenu.Companion.addToTableView( tableView);


        // Колонка для иконки файлов/директорий
        TableColumn<FileItem, Boolean> icoColumn = new TableColumn<>("Ico");
        icoColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDirectory()));

        // Настраиваем ячейки для отображения иконки
        icoColumn.setCellFactory(column -> new TableCell<FileItem, Boolean>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Boolean isFile, boolean empty) {
                super.updateItem(isFile, empty);

                if (empty || isFile == null) {
                    setGraphic(null);  // Убираем иконку, если строка пустая
                } else {
                    FileItem fileItem = getTableRow().getItem();
                    // Подставляем иконку в зависимости от значения
                    imageView.setImage(fileItem.getIco().getImage());
                    imageView.setPreserveRatio(true);  // Сохраняем пропорции изображения
                    imageView.setFitHeight(getTableRow().getHeight() - 10);  // Подгоняем под высоту строки, оставляя небольшой отступ
                    setGraphic(imageView);  // Устанавливаем изображение в ячейку
                }
            }
        });




        TableColumn<FileItem, String> nameColumn = new TableColumn<>("File Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<FileItem, String> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(OsUtil.sizeAdopt(cellData.getValue().length())));

        TableColumn<FileItem, Long> sizeBColumn = new TableColumn<>("SizeB");
        sizeBColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().length()));

        TableColumn<FileItem, Integer> sameColumn = new TableColumn<>("Same");
        sameColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSame().size()).asObject());

//        TableColumn<FileItem, Integer> sameColumn = new TableColumn<>("Same");
//        sameColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSame().size()));


//        TableColumn<FileItem, Boolean> isFileColumn = new TableColumn<>("IsDir");
//        isFileColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDirectory()));

        TableColumn<FileItem, Boolean> isFileColumn = new TableColumn<>("Type");
        isFileColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDirectory()));

        // Настройка CellFactory для отображения "D" для директорий и "F" для файлов
        isFileColumn.setCellFactory(column -> new TableCell<FileItem, Boolean>() {
            @Override
            protected void updateItem(Boolean isDirectory, boolean empty) {
                super.updateItem(isDirectory, empty);
                if (empty || isDirectory == null) {
                    setText(null); // Пустая ячейка
                } else {
                    // Отображаем "D" для директорий и "F" для файлов
                    setText(isDirectory ? "D" : "F");
                }
            }
        });


        TableColumn<FileItem, String> pathColumn = new TableColumn<>("Path");
        pathColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAbsolutePath()));

        TableColumn<FileItem, String> lastModifiedColumn = new TableColumn<>("Last Modified");
        lastModifiedColumn.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            return new SimpleStringProperty(sdf.format(new Date(cellData.getValue().lastModified())));
        });
        tableView.getColumns().clear();

        // !!! Hardcoded
        icoColumn.setPrefWidth(30.0);
        sameColumn.setPrefWidth(40.0);
        sizeBColumn.setPrefWidth(40.0);
        isFileColumn.setPrefWidth(40.0);
        pathColumn.setPrefWidth(300.0);

        tableView.getColumns().addAll(icoColumn, nameColumn, sameColumn,sizeColumn, sizeBColumn,isFileColumn, pathColumn, lastModifiedColumn);

        // Привязка компаратора списка и таблицы для корректной работы сортировки


    }





//    // It is working code:
//    public static void deleteSelected(TableView<FileItem> tableView){
//        ObservableList<FileItem> selectedFiles = tableView.getSelectionModel().getSelectedItems();
//        // Проверяем, есть ли выделенные элементы
//        if (selectedFiles.isEmpty()) {
//            System.out.println("Нет выделенных файлов для удаления.");
//            return;
//        }
//
//        if(!OsUtil.confirmDelete("  " + selectedFiles.size() + " files")) return;
//
//        // Удаление файлов и вывод информации о статусе
//        for (FileItem fileItem : selectedFiles) {
//                boolean deleted = OsUtil.deleteToTmp(fileItem);    //fileItem.delete();
//        }
//        // Удаление выделенных строк из TableView
//        tableView.getItems().removeAll(selectedFiles);
//    }
}
