package edu.ncsu.log

interface LogAdapter {
    fun v(message: String?)
    fun e(message: String?)
    fun w(message: String?)
    fun wtf(message: String?)
    fun i(message: String?)
    fun d(message: String?)
    fun entering()
    fun exiting()

    companion object {
        const val minimumLevels = 5
        const val targetIndex = 4

        const val Verbose = "AppVerbose"
        const val Info = "AppInfo"
        const val Debug = "AppDebug"
        const val Warning = "AppWarning"
        const val Error = "AppError"
        const val WTF = "AppWTF"

        fun instance(isDebugMode: Boolean = false) = DefaultLogAdapter(isDebugMode)
    }
}

class DefaultLogAdapter(private val isDebugMode: Boolean = false) : LogAdapter {
    override fun v(message: String?) {
        log(LogAdapter.Verbose, message)
    }

    override fun e(message: String?) {
        log(LogAdapter.Error, message)
    }

    override fun w(message: String?) {
        log(LogAdapter.Warning, message)
    }

    override fun wtf(message: String?) {
        log(LogAdapter.WTF, message)
    }

    override fun i(message: String?) {
        log(LogAdapter.Info, message)
    }

    override fun d(message: String?) {
        log(LogAdapter.Debug, message)
    }

    override fun entering() {
        log(LogAdapter.Verbose, "entering...")
    }

    override fun exiting() {
        log(LogAdapter.Verbose, "exiting...")
    }

    private fun log(tagName: String,
                    message: String?) {

        message?.let {msg ->
            if (isDebugMode
                || tagName.equals(LogAdapter.WTF, ignoreCase = true)
                || tagName.equals(LogAdapter.Error, ignoreCase = true)
                || tagName.equals(LogAdapter.Warning, ignoreCase = true)) {
                val stacktrace = Thread.currentThread().stackTrace
                val e: StackTraceElement = if (stacktrace.size >= LogAdapter.minimumLevels) {
                    stacktrace[LogAdapter.targetIndex]
                } else {
                    stacktrace[stacktrace.size - 1]
                }
                val methodName = e.methodName
                var className = e.className
                val index = className.lastIndexOf(".")

                className = className.substring(index + 1)
                println("[${tagName}] $className::$methodName(): $msg")
            }
        }
    }
}