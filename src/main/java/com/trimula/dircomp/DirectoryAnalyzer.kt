package com.trimula.dircomp

import com.trimula.dircomp.filetree.FileItem
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object DirectoryAnalyzer {
//    fun analyzeDirectory(directory: File): List<File> {
//        // Пример анализа – рекурсивный сбор файлов и директорий
//        return directory.walk().toList()
//    }
//    fun analyzeDirectory(directory: File, isProcessing: AtomicBoolean): List<FileItem> {
//        val result = mutableListOf<FileItem>()
//        directory.walk().forEach {
//            if (!isProcessing.get()) {
//                return result // Прерывание парсинга
//            }
//            result.add(it)
//        }
//        return result
//    }
//
//    fun compareDirectories(directory1: File, directory2: File): ComparisonResult {
//        // Пример базового сравнения директорий
//        val files1 = directory1.walk().toList()
//        val files2 = directory2.walk().toList()
//
//        val fullMatches = files1.filter { file1 ->
//            files2.any { file2 ->
//                file1.name == file2.name && file1.length() == file2.length()
//            }
//        }
//
//        val similarMatches = files1.filter { file1 ->
//            files2.any { file2 ->
//                file1.length() != file2.length()    //file1.name == file2.name &&
//            }
//        }
//
//        return ComparisonResult(fullMatches, similarMatches)
//    }
//
//    data class ComparisonResult(
//        val fullMatches: List<FileItem>,
//        val similarMatches: List<FileItem>
//    )
}


