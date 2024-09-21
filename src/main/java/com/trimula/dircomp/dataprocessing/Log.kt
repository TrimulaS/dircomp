package com.trimula.dircomp.dataprocessing;

import javafx.application.Platform
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

object Log {

    private val dateFormat = DateTimeFormatter.ofPattern("yy.MM.dd  HH:mm:ss")
    private val logList = ConcurrentLinkedQueue<String>()
    private val listeners = ConcurrentLinkedQueue<LogListener>()
    private val isJavaFX = AtomicBoolean(true)
    private var lastLogTime: LocalDateTime? = null // Время последнего вызова

    @JvmStatic
    @Synchronized
    fun appendText(text: String) {
        logList.add(text)
        notifyListeners()
    }

    @JvmStatic
    @Synchronized
    fun appendTextTimed(text: String) {
        val now = LocalDateTime.now()

        val timeDifference = lastLogTime?.let {
            val duration = Duration.between(it, now)
            formatDuration(duration)
        } ?: ""

        val timeStamp = dateFormat.format(now)
        appendText("$timeStamp $timeDifference$text")

        // Обновляем время последнего вызова
        lastLogTime = now
    }

    // Форматирование разницы времени
    private fun formatDuration(duration: Duration): String {
        val seconds = duration.seconds
        return when {
            seconds < 1 -> "( ${duration.toMillis()} ms ) "  // Если меньше секунды, показываем в миллисекундах
            seconds < 60 -> "( ${seconds} s ) "  // Меньше минуты, показываем в секундах
            seconds < 3600 -> {  // Меньше часа, показываем в минутах и секундах
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                "(${minutes}m${remainingSeconds} s) "
            }
            else -> {  // Если больше часа
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val remainingSeconds = seconds % 60
                "( ${hours} h  ${minutes} m  ${remainingSeconds} s ) "
            }
        }
    }
    fun appendTextAndTime(text: String) {
        appendText("${dateFormat.format(LocalDateTime.now())} $text")
    }


    @JvmStatic
    @Synchronized
    fun addError(text: String) {
        appendText("  ______________ Error  $text")
    }

    @JvmStatic
    @Synchronized
    fun get(): String {
        return logList.joinToString(separator = "\n")
    }

    @JvmStatic
    @Synchronized
    fun clear() {
        notifyBeforeClear()
        logList.clear()
    }

    @JvmStatic
    @Synchronized
    fun pop(): String {
        val text = get()
        clear()
        return text
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            if (isJavaFX.get()) {
                Platform.runLater {
                    listener.onChange(logList.joinToString(separator = "\n"))
                }
            } else {
                listener.onChange(logList.joinToString(separator = "\n"))
            }
        }
    }

    private fun notifyBeforeClear() {
        listeners.forEach { listener ->
            listener.onBeforeClear(logList.joinToString(separator = "\n"))
        }
    }

    fun addListener(listener: LogListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: LogListener) {
        listeners.remove(listener)
    }

    fun enableJavaFX() {
        isJavaFX.set(true)
    }

    fun disableJavaFX() {
        isJavaFX.set(false)
    }

    interface LogListener {
        fun onChange(logText: String)
        fun onBeforeClear(logText: String)
    }
}
