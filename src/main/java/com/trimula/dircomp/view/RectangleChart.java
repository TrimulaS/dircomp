package com.trimula.dircomp.view;

import com.trimula.dircomp.dataprocessing.Log;
import com.trimula.dircomp.model.FileItem;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class RectangleChart {
    private ScrollPane scrollPane;
    private double zoomFactor = 1.0;  // Уровень масштабирования
    private double panOffsetX = 0;    // Смещение по X для Pan
    private double panOffsetY = 0;    // Смещение по Y для Pan
    private Point2D lastMousePosition = null;

    public RectangleChart(ScrollPane scrollPane, TreeItem<FileItem> rootItem) {
        this.scrollPane = scrollPane;
        Canvas canvas = new Canvas(1980, 600); // Размеры холста
        scrollPane.setContent(canvas);
        scrollPane.setPannable(true);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Отрисовка структуры
        drawFileTree(gc, rootItem, 10, 10, canvas.getWidth() - 20, canvas.getHeight() - 20);

        // Обработчик для Zoom с помощью колеса мыши
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
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

    // Перерисовка холста
    private void redraw(GraphicsContext gc, TreeItem<FileItem> rootItem, Canvas canvas) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawFileTree(gc, rootItem, panOffsetX, panOffsetY, canvas.getWidth(), canvas.getHeight());
    }

    // Метод для рекурсивного построения дерева в виде прямоугольников с учетом zoom и pan
    private void drawFileTree(GraphicsContext gc, TreeItem<FileItem> item, double x, double y, double width, double height) {
        // Применение масштабирования к координатам и размерам
        x *= zoomFactor;
        y *= zoomFactor;
        width *= zoomFactor;
        height *= zoomFactor;

        if (width < 3 || height < 3) return; // Не рисуем слишком маленькие элементы

        // Пример рисования: цвет зависит от того, файл это или директория
        if (item.getValue().isDirectory()) {
            gc.setFill(Color.LIGHTGRAY);
        } else {
            gc.setFill(Color.DARKBLUE);
        }
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);

        // Текст не должен масштабироваться
        gc.setFill(Color.BLACK);
        gc.fillText(item.getValue().getName(), x + 5, y + 15); // Смещение текста от границы

        // Рекурсивная прорисовка для дочерних элементов
        if (item.getValue().isDirectory()) {
            double totalSize = item.getValue().length();
            double currentY = y;
            for (TreeItem<FileItem> child : item.getChildren()) {
                double childHeight = height * (child.getValue().length() / totalSize);
                drawFileTree(gc, child, x + 10 / zoomFactor, currentY, width - 20 / zoomFactor, childHeight);
                currentY += childHeight;
            }
        }
    }
}
