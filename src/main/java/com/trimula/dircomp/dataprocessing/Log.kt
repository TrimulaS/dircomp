package com.trimula.dircomp.dataprocessing

//  !!!!!!!!!!! enableJavaFX()  ////////////////////////// It is enabled below :

import javafx.application.Platform
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

object Log {

    // Определяем формат даты
    private val dateFormat = DateTimeFormatter.ofPattern("yy.MM.dd__hh-mm-ss")

    // Очередь для хранения логов
    private val logList = ConcurrentLinkedQueue<String>()

    // Слушатели для изменения логов
    private val listeners = ConcurrentLinkedQueue<LogListener>()

    // Проверка на использование JavaFX
    private val isJavaFX = AtomicBoolean(true)          ////////////////////////////////////////

    // Счётчик для лога
    private val counter = AtomicInteger(0)

    // Метод для добавления текста в лог
    @JvmStatic
    @Synchronized
    fun appendText(text: String) {
        logList.add(text)
        notifyListeners()
    }

    // Метод для добавления текста с отметкой времени
    @JvmStatic
    @Synchronized
    fun appendTextTimed(text: String) {
        appendText("${dateFormat.format(LocalDateTime.now())} $text")
    }

    // Метод для добавления ошибки
    @JvmStatic
    @Synchronized
    fun addError(text: String) {
        appendText("  ______________ Error  $text")
    }

    // Получить текущие логи в виде строки
    @JvmStatic
    @Synchronized
    fun get(): String {
        return logList.joinToString(separator = "\n")
    }

    // Очистить логи
    @JvmStatic
    @Synchronized
    fun clear() {
        notifyBeforeClear()
        logList.clear()
    }

    // Метод для извлечения всех логов и очистки
    @JvmStatic
    @Synchronized
    fun pop(): String {
        val text = get()
        clear()
        return text
    }

    // Метод для уведомления слушателей после изменения
    private fun notifyListeners() {
        listeners.forEach { listener ->
            if (isJavaFX.get()) {
                // Обновление через JavaFX, если активен JavaFX UI
                Platform.runLater {
                    listener.onChange(logList.joinToString(separator = "\n"))
                }
            } else {
                // Обновление через обычный вызов для Java приложений
                listener.onChange(logList.joinToString(separator = "\n"))
            }
        }
    }

    // Метод для уведомления перед очисткой
    private fun notifyBeforeClear() {
        listeners.forEach { listener ->
            listener.onBeforeClear(logList.joinToString(separator = "\n"))
        }
    }

    // Добавить слушателя
    fun addListener(listener: LogListener) {
        listeners.add(listener)
    }

    // Удалить слушателя
    fun removeListener(listener: LogListener) {
        listeners.remove(listener)
    }

    // Включить поддержку JavaFX, когда нужно обновление через Platform.runLater
    fun enableJavaFX() {
        isJavaFX.set(true)
    }

    // Отключить поддержку JavaFX
    fun disableJavaFX() {
        isJavaFX.set(false)
    }

    // Интерфейс для слушателей
    interface LogListener {
        fun onChange(logText: String)
        fun onBeforeClear(logText: String)
    }
}
