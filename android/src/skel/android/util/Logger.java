package skel.android.util;

import android.util.Log;

/**
 * Wrapper for android.util.Log
 *
 * @author Alexey Dudarev
 */
public class Logger {

    private final String tag;

    private Logger(String tag) {
        this.tag = tag;
    }

    public static Logger getLogger(String tag) {
        return new Logger(tag);
    }

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }


    // Auto-generated delegates, don't modify!!!

    public int d(String msg) {
        return Log.d(tag, msg);
    }

    public int d(String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    public int e(String msg) {
        return Log.e(tag, msg);
    }

    public int e(String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    public int i(String msg) {
        return Log.i(tag, msg);
    }

    public int i(String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    public int v(String msg) {
        return Log.v(tag, msg);
    }

    public int v(String msg, Throwable tr) {
        return Log.v(tag, msg, tr);
    }

    public int w(String msg) {
        return Log.w(tag, msg);
    }

    public int w(String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public int w(Throwable tr) {
        return Log.w(tag, tr);
    }

    public static int wtf(String tag, String msg) {
        return Log.wtf(tag, msg);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        return Log.wtf(tag, msg, tr);
    }

    public static int wtf(String tag, Throwable tr) {
        return Log.wtf(tag, tr);
    }
}
