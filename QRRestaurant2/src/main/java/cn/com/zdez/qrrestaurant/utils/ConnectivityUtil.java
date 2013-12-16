package cn.com.zdez.qrrestaurant.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class ConnectivityUtil {
    private static String TAG = ConnectivityUtil.class.getSimpleName();

    public static boolean isOnline(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        State cellurState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        MyLog.d(TAG, "State of wifi:" + wifiState + "and mobile:"
                + cellurState);

        if (wifiState == State.CONNECTED || cellurState == State.CONNECTED) {
            MyLog.d(TAG, "Got network connected");
            return true;

        }
        return false;
    }
}
