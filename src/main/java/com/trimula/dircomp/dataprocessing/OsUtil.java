package com.trimula.dircomp.dataprocessing;
import log.Log;

import java.io.File;
import java.io.IOException;

public class OsUtil {

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



}
