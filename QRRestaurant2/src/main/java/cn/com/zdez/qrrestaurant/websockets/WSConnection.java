package cn.com.zdez.qrrestaurant.websockets;

import android.content.Context;
import android.util.Log;

import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by LuoHanLin on 13-12-12.
 */
public class WSConnection {
    private static final String TAG = WSConnection.class.getSimpleName();
    private static final String BASE_WSURL = "ws://192.168.199.112:8080/qr_restaurant/wsservlet/WSOrderWSServlet";

    public Context context;
    private static WSConnection instance;
    public static WebSocketConnection mConnection;

    private WSConnection(Context context) {
        mConnection = new WebSocketConnection();
        this.context = context;
    }

    public static WSConnection getInstance(Context context) {
        if (instance == null) {
            instance = new WSConnection(context);
        }

        return instance;
    }

    public void connect(long tid, long uid) {
        final String wsurl = Constants.WEBSOCKET_BASE_URL + Constants.ORDERING_MODULE_WS_URL + "?tid=" + String.valueOf(tid) + "&uid=" + String.valueOf(uid);
        try {
            mConnection.connect(wsurl, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsurl);
                    ToastUtil.showShortToast(context, "连接上点餐服务器，可以开始开始点餐");
                    mConnection.sendTextMessage("Hello, world!");
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got a Incoming Message: " + payload);
                    ToastUtil.showShortToast(context, "收到消息：" + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost...");
                }
            });
        } catch (WebSocketException e) {

            Log.d(TAG, e.toString());
        }
    }
}
