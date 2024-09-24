package com.trimula.dircomp.dataprocessing;
import com.trimula.dircomp.model.FileItem;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;

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

//        if (os.contains("win")) {
//            // Windows
//            if (file.isDirectory()) {
//                new ProcessBuilder("explorer", absolutePath).start();
//                Log.appendText("Opening Directory in Win: " + absolutePath);
//            } else {
//                new ProcessBuilder("explorer", "/select,", absolutePath).start();
//                Log.appendText("Opening File in Win: " + absolutePath);
//            }
//        } else if (os.contains("mac")) {
//            // macOS
//            new ProcessBuilder("open", absolutePath).start();
//        } else if (os.contains("nux")) {
//            // Linux
//            new ProcessBuilder("xdg-open", absolutePath).start();
//        } else {
//            throw new UnsupportedOperationException("Операционная система не поддерживается.");
//        }





    }


    public static boolean confirmDelete(String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete?");
        alert.setContentText("This will permanently delete the file or folder: " + text);

        // Опции "Yes" и "No"
        ButtonType yesButton = ButtonType.YES;
        ButtonType noButton = ButtonType.NO;

        alert.getButtonTypes().setAll(yesButton, noButton);

        // Ждем, пока пользователь не выберет вариант
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }


    /// Doesn't work!

//    public static void deleteToRecycleBin(FileItem fileItem) {
//        deleteToRecycleBin(new File(fileItem.getAbsolutePath()));
//    }
//
//    public static void deleteToRecycleBin(File file) {
//        if (file.exists()) {
//            String os = System.getProperty("os.name").toLowerCase();
//            try {
//                if (os.contains("win")) {
//                    // Windows
//                    String command = "cmd /c move \"" + file.getAbsolutePath() + "\" \"C:\\$Recycle.Bin\\\"";
//                    Runtime.getRuntime().exec(command);
//                } else if (os.contains("mac")) {
//                    // Mac
//                    String command = "mv \"" + file.getAbsolutePath() + "\" ~/.Trash/";
//                    Runtime.getRuntime().exec(command);
//                } else if (os.contains("nix") || os.contains("nux")) {
//                    // Linux
//                    String command = "mv \"" + file.getAbsolutePath() + "\" ~/.local/share/Trash/files/";
//                    Runtime.getRuntime().exec(command);
//                } else {
//                    System.out.println("Unsupported OS");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("File does not exist.");
//        }
//    }


    public static boolean deleteToTmp(FileItem fileItem) {
        return deleteToTmp(new File(fileItem.getAbsolutePath()));
    }

    // Function to test (move to tmp instead of delete)
    public static boolean deleteToTmp(File file) {
        // Определяем целевую директорию
        String targetDirectory = "C:/tmp/tmp";

        // Проверяем, существует ли целевая директория, если нет, создаем её
        File directory = new File(targetDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Создаем директорию, если её нет
        }

        // Генерируем новое имя файла
        File targetFile = new File(targetDirectory + "/" + file.getName());

        // Если файл уже существует, генерируем новое уникальное имя
        if (targetFile.exists()) {
            String fileName = file.getName();
            String fileExtension = "";

            // Если у файла есть расширение, разделяем имя и расширение
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileExtension = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
            }

            // Генерируем случайную последовательность
            String randomSequence = generateRandomHash();

            // Создаем новое имя файла
            targetFile = new File(targetDirectory + "/" + fileName + "_" + randomSequence + fileExtension);
        }

        // Перемещаем файл
        try {
            Files.move(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Файл перемещён в: " + targetFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка перемещения файла: " + e.getMessage());
            return false;
        }
    }

    // Метод для генерации случайной последовательности (например, хэш)
    private static String generateRandomHash() {
        try {
            Random random = new Random();
            String randomString = String.valueOf(random.nextInt(999999));

            // Хэшируем строку с помощью SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(randomString.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString().substring(0, 8); // Возвращаем только первые 8 символов хэша
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка создания хэша: " + e.getMessage());
        }
    }

}
