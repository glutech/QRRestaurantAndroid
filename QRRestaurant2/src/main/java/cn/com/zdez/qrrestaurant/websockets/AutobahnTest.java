package cn.com.zdez.qrrestaurant.websockets;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * Created by LuoHanLin on 13-12-11.
 */
public class AutobahnTest {
    private static final String TAG = AutobahnTest.class.getSimpleName();
    private static final String WSURL = "ws://192.168.1.118:8080/WebsocketServerDemo/wsservlet/WSServletDemo";
    private static final WebSocketConnection mConnection = new WebSocketConnection();

    public static void start() {

        try {
            mConnection.connect(WSURL, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + WSURL);
                    mConnection.sendTextMessage("Hello, world!");
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got a Incoming Message: " + payload);
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
