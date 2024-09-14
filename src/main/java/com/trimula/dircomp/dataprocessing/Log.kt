package log


import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

object Log {

     private const val TAG = "Log"
//     private val dateFormat = SimpleDateFormat("yy.MM.dd__hh-mm-ss", Locale.getDefault())
    private val dateFormat = DateTimeFormatter.ofPattern("yy.MM.dd__hh-mm-ss")

    private val logList = CopyOnWriteArrayList<String>()
    private val listeners = CopyOnWriteArrayList<LogListener>()
    @JvmStatic
    @Synchronized
    fun setText(text: String) {
        logList.clear()
        appendText(text)
    }
    @JvmStatic
    @Synchronized
    fun appendText(text: String) {
        logList.add(text)
        for (listener in listeners) {
            listener.onChange(logList.toString())
        }
    }
    @JvmStatic
    @Synchronized
    fun appendTextTimed(text: String) {
        appendText("${dateFormat.format(LocalDateTime.now())} $text")
    }
    @JvmStatic
    @Synchronized
    fun addError(text: String){
        appendText("  ______________ Error  " + text)
    }
    @JvmStatic
    @Synchronized
    fun get(): String {
        return logList.joinToString(separator = "\n")
    }
    @JvmStatic
    @Synchronized
    fun clear() {
        for (listener in listeners) {
            listener.onBeforeClear(logList.toString())
        }
        logList.clear()
    }
    @JvmStatic
    @Synchronized
    fun pop():String{
        val text = logList.joinToString(separator = "\n")
        logList.clear()

        return text
    }

    fun addListener(listener: LogListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: LogListener) {
        listeners.remove(listener)
    }

    interface LogListener {
        fun onChange(logText: String)
        fun onBeforeClear(logText: String)
    }
}

// Usage
//        Log.addListener (object : Log.LogListener {
//            override fun onChange(logText: String) {
//                println("Log has changed: $logText")
//            }
//
//            override fun onBeforeClear(logText: String) {
//                println("Log is about to be cleared: $logText")
//            }
//        } )