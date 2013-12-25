package cn.com.zdez.qrrestaurant.utils;

/**
 * Created by LuoHanLin on 13-12-6.
 */
public class MyLog {

    public static final boolean DEBUG = true;

    public static void i(String tag, String str) {
        if (DEBUG) android.util.Log.i(tag, str);
    }

    public static void e(String tag, String str) {
        if (DEBUG) android.util.Log.e(tag, str);
    }

    public static void d(String tag, String str) {
        if (DEBUG) android.util.Log.d(tag, str);
    }

    public static void v(String tag, String str) {
        if (DEBUG) android.util.Log.v(tag, str);
    }

    public static void w(String tag, String str) {
        if (DEBUG) android.util.Log.w(tag, str);
    }
}
