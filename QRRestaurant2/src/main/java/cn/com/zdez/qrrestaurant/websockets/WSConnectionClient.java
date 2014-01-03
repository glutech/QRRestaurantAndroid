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
    public static WSConnectionClient instance;
    public static WebSocketConnection connection;

    private WSConnectionClient() {
        connection = new WebSocketConnection();
    }

    public static WSConnectionClient getInstance(){
        if(null == instance){
            instance = new WSConnectionClient();
        }
        return  instance;
    }


    public WebSocketConnection connect(String tid, String uid, WebSocketHandler wsHandler) {
        String absUrlWithParams = Constants.WEBSOCKET_BASE_URL + Constants.ORDERING_MODULE_WS_URL + "?tid=" + tid + "&uid=" + uid;

        MyLog.d(TAG, "Generated URL: " + absUrlWithParams);
        try {
            connection.connect(absUrlWithParams, wsHandler);
        } catch (WebSocketException e) {
            e.printStackTrace();
            MyLog.e(TAG, "Got wrong at connect to ws server...");
        }

        return connection;
    }
}
