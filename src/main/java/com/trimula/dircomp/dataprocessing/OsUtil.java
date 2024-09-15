package com.trimula.dircomp.dataprocessing;
import javafx.scene.control.ButtonType;
import log.Log;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class OsUtil {


    public static String sizeAdopt(long sizeInBytes) {
        // Единицы измерения
        int unit = 1024;
        if (sizeInBytes < unit) {
            return sizeInBytes + " Байт";
        }

        // Массив с суффиксами единиц измерения
        //String[] units = {"КБ", "МБ", "ГБ", "ТБ", "ПБ"};
        String[] units = {"KB", "MB", "GB", "TB", "PB"};
        double size = (double) sizeInBytes / unit;
        int unitIndex = 0;

        // Продолжаем делить на 1024, пока размер больше единицы измерения
        while (size >= unit && unitIndex < units.length - 1) {
            size /= unit;
            unitIndex++;
        }

        // Возвращаем результат с двумя знаками после запятой
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    public static void openInExplorer(File file) throws IOException {
        String absolutePath = file.getAbsolutePath();
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            if (file.isDirectory()) {
                new ProcessBuilder("explorer", absolutePath).start();
                Log.appendText("Opening Directory in Win: " + absolutePath);
            } else {
                new ProcessBuilder("explorer", "/select,", absolutePath).start();
                Log.appendText("Opening File in Win: " + absolutePath);
            }
        } else if (os.contains("mac")) {
            // macOS
            new ProcessBuilder("open", absolutePath).start();
        } else if (os.contains("nux")) {
            // Linux
            new ProcessBuilder("xdg-open", absolutePath).start();
        } else {
            throw new UnsupportedOperationException("Операционная система не поддерживается.");
        }
    }

    public static boolean confirmDelete(File file) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete?");
        alert.setContentText("This will permanently delete the file or folder: " + file.getName());

        // Опции "Yes" и "No"
        ButtonType yesButton = ButtonType.YES;
        ButtonType noButton = ButtonType.NO;

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Ждем, пока пользователь не выберет вариант
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }


    public static void deleteToRecycleBin(File file) {
        if (file.exists()) {
            String os = System.getProperty("os.name").toLowerCase();
            try {
                if (os.contains("win")) {
                    // Windows
                    String command = "cmd /c move \"" + file.getAbsolutePath() + "\" \"C:\\$Recycle.Bin\\\"";
                    Runtime.getRuntime().exec(command);
                } else if (os.contains("mac")) {
                    // Mac
                    String command = "mv \"" + file.getAbsolutePath() + "\" ~/.Trash/";
                    Runtime.getRuntime().exec(command);
                } else if (os.contains("nix") || os.contains("nux")) {
                    // Linux
                    String command = "mv \"" + file.getAbsolutePath() + "\" ~/.local/share/Trash/files/";
                    Runtime.getRuntime().exec(command);
                } else {
                    System.out.println("Unsupported OS");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist.");
        }
    }



}
