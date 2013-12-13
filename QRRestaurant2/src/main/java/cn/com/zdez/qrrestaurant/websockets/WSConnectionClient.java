package cn.com.zdez.qrrestaurant.websockets;

import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class WSConnectionClient {

    private static String TAG = WSConnectionClient.class.getSimpleName();
    private static WebSocketConnection connection = new WebSocketConnection();

    public static void connect(String relativeUrl, String tid, String uid, WebSocketHandler wsHandler) {
        String absUrlWithParams = Constants.WEBSOCKET_BASE_URL + relativeUrl + "?tid=" + tid + "&uid=" + uid;

        try {
            connection.connect(absUrlWithParams, wsHandler);
        } catch (WebSocketException e) {
            e.printStackTrace();
            MyLog.e(TAG, "Got wrong at connect to ws server...");
        }
    }
}
