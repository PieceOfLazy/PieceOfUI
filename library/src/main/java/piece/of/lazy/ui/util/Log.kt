package piece.of.lazy.ui.util

/**
 * Created by zpdl
 */
open class Log constructor(private val tag: String)   {

    enum class LEVEL {
        NOT, VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    companion object {
        var level: LEVEL = LEVEL.ERROR
        var prefix = ""

        fun v(tag: String, msg: String) {
            if (LEVEL.VERBOSE.ordinal >= level.ordinal)
                android.util.Log.v("$prefix$tag", msg)
        }

        fun d(tag: String, msg: String) {
            if (LEVEL.DEBUG.ordinal >= level.ordinal)
                android.util.Log.d("$prefix$tag", msg)
        }

        fun i(tag: String, msg: String) {
            if (LEVEL.INFO.ordinal >= level.ordinal)
                android.util.Log.i("$prefix$tag", msg)
        }

        fun w(tag: String, msg: String) {
            if (LEVEL.WARN.ordinal >= level.ordinal)
                android.util.Log.w("$prefix$tag", msg)
        }

        fun e(tag: String, msg: String) {
            if (LEVEL.ERROR.ordinal >= level.ordinal)
                android.util.Log.e("$prefix$tag", msg)
        }
    }

    fun v(msg: String) {
        if (LEVEL.VERBOSE.ordinal >= level.ordinal)
            Log.v(tag, msg)
    }

    fun d(msg: String) {
        if (LEVEL.DEBUG.ordinal >= level.ordinal)
            Log.d(tag, msg)
    }

    fun i(msg: String) {
        if (LEVEL.INFO.ordinal >= level.ordinal)
            Log.i(tag, msg)
    }

    fun w(msg: String) {
        if (LEVEL.WARN.ordinal >= level.ordinal)
            Log.w(tag, msg)
    }

    fun e(msg: String) {
        if (LEVEL.ERROR.ordinal >= level.ordinal)
            Log.e(tag, msg)
    }
}