package cn.com.zdez.qrrestaurant.websockets;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.WSMsgType;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * 专门异步处理 wsconnection 服务器传过来的消息
 * Created by LuoHanLin on 13-12-27.
 */
public class OrderMsgWSHandler extends WebSocketHandler {
    private TextView tvOrderMessage;
    private static String TAG = "OrderMsgWSHandler";
    private Handler handler;
    private Runnable runnable;
    private int TIME;

    public OrderMsgWSHandler(TextView tv, Handler handler, Runnable runnable, int TIME) {
        super();
        this.tvOrderMessage = tv;
        this.handler = handler;
        this.runnable = runnable;
        this.TIME = TIME;
    }

    @Override
    public void onClose(int code, String reason) {
        super.onClose(code, reason);
    }

    @Override
    public void onTextMessage(String payload) {
        super.onTextMessage(payload);
        // 处理服务器传过来的消息
        // 消息种类：
        // add did, delete did, join uid, leave uid
        MyLog.d(TAG, "GOT TEXTMSG FROM WS SERVER: " + payload);
        String[] msgs = payload.split(" ");
        String msgHead = msgs[0];
        String msgBy = "";
        String msgBody = "";
        String dishName = "";

        // TODO: 验证 msgBody 是否是数字
        switch (WSMsgType.toMsgType(msgHead.toUpperCase())) {
            case ADD:
                // 用户添加菜品
                msgBy = msgs[1];
                msgBody = msgs[2];
                // 有菜品 id 取得菜品名称 TODO: try catch it
                dishName = RestaurantWaitressGirl.dishMap.get(Long.parseLong(msgBody)).getDish_name();
                tvOrderMessage.setText("++小伙伴@" + msgBy + "增加了[" + dishName + "]");
                tvOrderMessage.setVisibility(View.VISIBLE);
                handler.postDelayed(runnable, TIME);
                break;
            case DELETE:
                // 用户删除菜品
                msgBy = msgs[1];
                msgBody = msgs[2];
                // 有菜品 id 取得菜品名称 TODO: try catch it
                dishName = RestaurantWaitressGirl.dishMap.get(Long.parseLong(msgBody)).getDish_name();
                tvOrderMessage.setText("--小伙伴@" + msgBy + "删除了[" + dishName + "]");
                tvOrderMessage.setVisibility(View.VISIBLE);
                handler.postDelayed(runnable, TIME);
                break;
            case JOIN:
                msgBy = msgs[1];
                tvOrderMessage.setText("!!小伙伴@" + msgBy + "加入到点菜队伍了");
                tvOrderMessage.setVisibility(View.VISIBLE);
                handler.postDelayed(runnable, TIME);
                break;
            case LEAVE:
                msgBy = msgs[1];
                tvOrderMessage.setText("??小伙伴@" + msgBy + "离开点菜队伍了");
                tvOrderMessage.setVisibility(View.VISIBLE);
                handler.postDelayed(runnable, TIME);
                break;
            case NOTSURE:
                // 没有定义的消息类型
                MyLog.d("WS点菜模块", "乱七八糟，不知道这时什么消息： " + payload);
                break;
        }
    }

    @Override
    public void onRawTextMessage(byte[] payload) {
        super.onRawTextMessage(payload);
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
        super.onBinaryMessage(payload);
    }

    @Override
    public void onOpen() {
        super.onOpen();
    }
}
