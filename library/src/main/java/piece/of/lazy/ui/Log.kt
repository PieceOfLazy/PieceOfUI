package piece.of.lazy.ui

/**
 * Created by zpdl
 */
open class Log {
    enum class LEVEL {
        NOT, VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    companion object {
        var level: LEVEL = LEVEL.ERROR

        fun v(tag: String, format: String, vararg args: Any?) {
            if (LEVEL.VERBOSE.ordinal >= level.ordinal)
                android.util.Log.v(tag, String.format(format, args))
        }

        fun d(tag: String, format: String, vararg args: Any?) {
            if (LEVEL.DEBUG.ordinal >= level.ordinal)
                android.util.Log.d(tag, String.format(format, args))
        }

        fun i(tag: String, format: String, vararg args: Any?) {
            if (LEVEL.INFO.ordinal >= level.ordinal)
                android.util.Log.i(tag, String.format(format, args))
        }

        fun w(tag: String, format: String, vararg args: Any?) {
            if (LEVEL.WARN.ordinal >= level.ordinal)
                android.util.Log.w(tag, String.format(format, args))
        }

        fun e(tag: String, format: String, vararg args: Any?) {
            if (LEVEL.ERROR.ordinal >= level.ordinal)
                android.util.Log.e(tag, String.format(format, args))
        }
    }
}