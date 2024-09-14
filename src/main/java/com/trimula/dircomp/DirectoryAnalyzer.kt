package com.trimula.dircomp

import java.io.File


object DirectoryAnalyzer {
    fun analyzeDirectory(directory: File): List<File> {
        // Пример анализа – рекурсивный сбор файлов и директорий
        return directory.walk().toList()
    }

    fun compareDirectories(directory1: File, directory2: File): ComparisonResult {
        // Пример базового сравнения директорий
        val files1 = directory1.walk().toList()
        val files2 = directory2.walk().toList()

        val fullMatches = files1.filter { file1 ->
            files2.any { file2 ->
                file1.name == file2.name && file1.length() == file2.length()
            }
        }

        val similarMatches = files1.filter { file1 ->
            files2.any { file2 ->
                file1.name == file2.name && file1.length() != file2.length()
            }
        }

        return ComparisonResult(fullMatches, similarMatches)
    }

    data class ComparisonResult(
        val fullMatches: List<File>,
        val similarMatches: List<File>
    )
}


//class DirectoryAnalyzer {
//}