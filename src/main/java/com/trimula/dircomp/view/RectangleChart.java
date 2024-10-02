package com.trimula.dircomp.view;

import com.trimula.dircomp.dataprocessing.Log;
import com.trimula.dircomp.model.FileItem;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

import javafx.scene.transform.Scale;

public class RectangleChart {
    private Group group;
    private double zoomFactor = 1.0;
    private double panOffsetX = 0;
    private double panOffsetY = 0;
    private Point2D lastMousePosition = null;
    private final double padding = 3; // Паддинг в 2 пикселя

    public RectangleChart(Pane root, TreeItem<FileItem> rootItem) {
        group = new Group();
        root.getChildren().add(group);
        Canvas canvas = new Canvas(400, 600); // Размеры холста
        group.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Привязка ширины и высоты Pane к его родителю
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            redraw(gc, rootItem, canvas);
        });

        root.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            redraw(gc, rootItem, canvas);
        });

        // Отрисовка структуры
        drawFileTree(gc, rootItem, 10, 10, canvas.getWidth() - 20, canvas.getHeight() - 20);

        // Обработчик для Zoom с помощью колеса мыши
        root.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double zoomDelta = 0.1;
                if (event.getDeltaY() > 0) {
                    zoomFactor += zoomDelta;
                } else {
                    zoomFactor -= zoomDelta;
                    if (zoomFactor < 0.1) zoomFactor = 0.1; // Ограничение на минимальный зум
                }
                redraw(gc, rootItem, canvas);
                event.consume();
            }
        });

        // Обработчик для Pan
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            lastMousePosition = new Point2D(event.getX(), event.getY());
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (lastMousePosition != null) {
                double deltaX = event.getX() - lastMousePosition.getX();
                double deltaY = event.getY() - lastMousePosition.getY();
                panOffsetX += deltaX;
                panOffsetY += deltaY;
                lastMousePosition = new Point2D(event.getX(), event.getY());
                redraw(gc, rootItem, canvas);
            }
        });
    }

    private void redraw(GraphicsContext gc, TreeItem<FileItem> rootItem, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.save();
        gc.translate(panOffsetX, panOffsetY); // Применяем Pan с учётом смещения
        gc.scale(zoomFactor, zoomFactor);     // Применяем масштабирование только для прямоугольников
        drawFileTree(gc, rootItem, panOffsetX, panOffsetY, canvas.getWidth(), canvas.getHeight());
        gc.restore();
    }

    // Пример метода для рекурсивного построения дерева в виде прямоугольников
    private void drawFileTree(GraphicsContext gc, TreeItem<FileItem> item, double x, double y, double width, double height) {
        if (width < 3 || height < 3) return; // Не рисуем слишком маленькие элементы

        // Пример рисования: цвет зависит от того, файл это или директория
        if (item.getValue().isDirectory()) {

            if(item.getValue().getSame().isEmpty()) gc.setFill(Color.LIGHTGRAY);    // Is not same
            else gc.setFill(Color.ORANGE);

        } else {
            if(item.getValue().getSame().isEmpty()) gc.setFill(Color.LIGHTBLUE); // Is not same
            else gc.setFill(Color.YELLOW);
        }

        // Добавляем padding, который не будет зависеть от zoom
        double adjustedX = x + padding / zoomFactor;
        double adjustedY = y + padding / zoomFactor;
        double adjustedWidth = width - 2 * padding / zoomFactor;
        double adjustedHeight = height - 2 * padding / zoomFactor;

        gc.fillRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight);

        // Добавляем имя файла в левый верхний угол
        gc.setFill(Color.DARKBLUE);
        gc.fillText(item.getValue().getName(), adjustedX + 2 / zoomFactor, adjustedY + 10 / zoomFactor);

        // Рекурсивная прорисовка для детей (с учетом пропорций размеров)
        if (item.getValue().isDirectory()) {
            double totalSize = item.getValue().length();
            double currentY = adjustedY;
            for (TreeItem<FileItem> child : item.getChildren()) {
                double childHeight = adjustedHeight * (child.getValue().length() / totalSize);
                drawFileTree(gc, child, adjustedX + 10 / zoomFactor, currentY, adjustedWidth - 20 / zoomFactor, childHeight);
                currentY += childHeight;
            }
        }
    }
}