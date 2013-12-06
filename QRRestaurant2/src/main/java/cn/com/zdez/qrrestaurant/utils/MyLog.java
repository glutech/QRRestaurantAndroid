package cn.com.zdez.qrrestaurant.utils;

/**
 * Created by LuoHanLin on 13-12-6.
 */
public class MyLog {

    static final boolean LOG = true;

    public static void i(String tag, String str) {
        if (LOG) android.util.Log.i(tag, str);
    }

    public static void e(String tag, String str) {
        if (LOG) android.util.Log.e(tag, str);
    }

    public static void d(String tag, String str) {
        if (LOG) android.util.Log.d(tag, str);
    }

    public static void v(String tag, String str) {
        if (LOG) android.util.Log.v(tag, str);
    }

    public static void w(String tag, String str) {
        if (LOG) android.util.Log.w(tag, str);
    }
}
