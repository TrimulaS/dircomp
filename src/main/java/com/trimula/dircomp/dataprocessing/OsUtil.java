package com.trimula.dircomp.dataprocessing;
import com.trimula.dircomp.model.FileItem;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OsUtil {
    public static String tempDeletePath = "C://tmp//tmp";
    public static boolean isTestMode = true;

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




    // Function to open the file using the system's default application
    public static void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                String osName = System.getProperty("os.name").toLowerCase();
                if (osName.contains("win")) {
                    Runtime.getRuntime().exec("explorer " + file.getAbsolutePath());
                } else if (osName.contains("mac")) {
                    Runtime.getRuntime().exec("open " + file.getAbsolutePath());
                } else if (osName.contains("nix") || osName.contains("nux")) {
                    Runtime.getRuntime().exec("xdg-open " + file.getAbsolutePath());
                } else {
                    System.out.println("Unsupported operating system: " + osName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Opening file in explorer
    public static void openInExplorer(File file) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        String absolutePath = file.getAbsolutePath();
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



    public static boolean confirmDelete (String text) {
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




    //---------------------------------------------------------------------------Delete------------
    public static boolean delete(String absolutePath) {
        return delete(new File(absolutePath));
    }
    public static boolean delete(File file) {
        if(isTestMode){
            return deleteToTmp(file);
        }
        else{
            boolean deleteResult;
            if(file.isDirectory()){
                deleteResult = deleteDirectory(file);
                if(deleteResult){
                    Log.appendTextTimed(file.getAbsolutePath() + "\t - -=directory=- permanently deleted");
                    return deleteResult;
                }
                else {
                    Log.appendTextTimed(file.getAbsolutePath() + "\t - -=directory=- not deleted ( tried to permanently delete )");
                    return deleteResult;
                }

            }
            else {
                deleteResult = file.delete();
                if(deleteResult){
                    Log.appendTextTimed(file.getAbsolutePath() + "\t - -=file=- permanently deleted");
                    return deleteResult;
                }
                else {
                    Log.appendTextTimed(file.getAbsolutePath() + "\t - -=file=- not deleted ( tried to permanently delete )");
                }
                return deleteResult;
            }

        }

    }

    // Метод для рекурсивного удаления директории и всех её содержимого
    public static boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            System.out.println("Directory does not exist: " + directory.getAbsolutePath());
            return false;
        }

        // Если это директория, удаляем содержимое рекурсивно
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) { // Защита от null
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file); // Рекурсивный вызов для поддиректорий
                    } else {
                        file.delete(); // Удаляем файл
                    }
                }
            }
        }

        // После того как все содержимое удалено, удаляем саму директорию
        return directory.delete();
    }

    public static boolean deleteToTmp(File file){
        return deleteToTmp(file, tempDeletePath);
    }
    // Function to test (move to tmp instead of delete)
    public static boolean deleteToTmp(File file, String targetDirectory)  {

        // Проверяем, существует ли целевая директория, если нет, создаем её
        File directory = new File(targetDirectory);
        if (!directory.exists()) {
            Log.appendTextTimed("Target directory for delete backup: "+ directory.getAbsolutePath() +" not exists");
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
        if(file.isFile()){
            try {
                Files.move(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Log.appendTextTimed(targetFile.getAbsolutePath() + "\t - deleted safely, moved to : " + tempDeletePath);
                return true;
            } catch (IOException e) {
                Log.appendTextTimed(targetFile.getAbsolutePath() + "\t - (!) failed to delete: " + e.getMessage());
                return false;
            }
        }
//        else{
//            if(moveDirectory(file, "/tmp/tmp/")){
//                Log.appendTextTimed(file.getAbsolutePath() + " - Directory is moved");
//                return true;
//            }
//
//            else{
//                Log.appendTextTimed(file.getAbsolutePath() + " - (!) Failed to move directory");
//                return false;
//            }
//
//        }
        return false;
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

    //
//    public static boolean moveDirectory(Path source, Path target) {
//        try {
//            // Проверяем, является ли это директорией
//            if (Files.isDirectory(source)) {
//                // Создаем целевую директорию, если она не существует
//                if (!Files.exists(target)) {
//                    Files.createDirectories(target);
//                }
//
//                // Рекурсивно копируем содержимое директории
//                Files.walk(source).forEach(sourcePath -> {
//                    Path targetPath = target.resolve(source.relativize(sourcePath));
//                    try {
//                        if (Files.isDirectory(sourcePath)) {
//                            // Если это директория, создаем её в целевой директории
//                            if (!Files.exists(targetPath)) {
//                                Files.createDirectories(targetPath);
//                            }
//                        } else {
//                            // Если это файл, перемещаем его
//                            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
//                        }
//                    } catch (IOException e) {
//                        Log.appendTextTimed("Failed to move: " + sourcePath + " -> " + targetPath + " : " + e.getMessage());
//                    }
//                });
//
//                // Удаляем исходную директорию после перемещения всех файлов
//                Files.delete(source);
//
//            } else {
//                // Это файл, просто перемещаем его
//                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
//            }
//
//            Log.appendTextTimed("Moved: " + source.toString() + " -> " + target.toString());
//            return true;
//
//        } catch (IOException e) {
//            Log.appendTextTimed("Failed to move directory: " + e.getMessage());
//            return false;
//        }
//    }
//    public static void moveDirectory(File directory, String destinationBasePath) throws IOException {
//        if (!directory.exists() || !directory.isDirectory()) {
//            System.out.println("Исходная директория не существует.");
//            return;
//        }
//
//        // Проверяем, существует ли директория /tmp/tmp, если нет, создаем её
//        File destinationBaseDir = new File(destinationBasePath);
//        if (!destinationBaseDir.exists()) {
//            if (destinationBaseDir.mkdirs()) {
//                System.out.println("Директория " + destinationBasePath + " успешно создана.");
//            } else {
//                throw new IOException("Не удалось создать директорию: " + destinationBasePath);
//            }
//        }
//
//        // Генерируем уникальное имя для директории, если она уже существует в целевой директории
//        File newDestinationDir = new File(destinationBaseDir, directory.getName());
//        while (newDestinationDir.exists()) {
//            newDestinationDir = new File(destinationBaseDir, generateNewName(directory.getName()));
//        }
//
//        // Перемещаем директорию
//        Files.move(directory.toPath(), newDestinationDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//        System.out.println("Директория " + directory.getAbsolutePath() + " успешно перемещена в " + newDestinationDir.getAbsolutePath());
//    }
//
//    // Метод для генерации нового имени, добавляя "_" и 3 случайных символа
//    public static String generateNewName(String originalName) {
//        Random random = new Random();
//        StringBuilder newName = new StringBuilder(originalName);
//        newName.append("_");
//        for (int i = 0; i < 3; i++) {
//            newName.append((char) ('a' + random.nextInt(26))); // случайная буква от 'a' до 'z'
//        }
//        return newName.toString();
//    }


}
