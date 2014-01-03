package cn.com.zdez.qrrestaurant.websockets;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.Map;

import cn.com.zdez.qrrestaurant.RestaurantDishesListActivity;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.layouts.DishesSelectedAdapter;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.WSMsgType;
import cn.com.zdez.qrrestaurant.vo.DishesMapBuilderForGson;
import de.tavendo.autobahn.WebSocketHandler;

/**
 * 专门异步处理 wsconnection 服务器传过来的消息
 * Created by LuoHanLin on 13-12-27.
 */
public class OrderMsgWSHandler extends WebSocketHandler {
    private RestaurantWaitressGirl girl;
    private Button btnSelector;
    private TextView tvOrderMessage;
    private static String TAG = "OrderMsgWSHandler";
    private Handler handler;
    private Runnable runnable;
    private int TIME;
    public static boolean isInSelectedListActivity = false;
    private Handler handlerInSelectedList;
    private Runnable runnableInSelectedList;
    private TextView tvOMSGInSelectedList;
    private Runnable reloadSelectedList;

    public OrderMsgWSHandler(RestaurantWaitressGirl girl, Button btnSelectedCounter, TextView tvOrderMessage, Handler handler, Runnable runnable, int time) {
        super();
        this.girl = girl;
        this.btnSelector = btnSelectedCounter;
        this.tvOrderMessage = tvOrderMessage;
        this.handler = handler;
        this.runnable = runnable;
        this.TIME = time;
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
        String msg = "";

        // TODO: 验证 msgBody 是否是数字
        switch (WSMsgType.toMsgType(msgHead.toUpperCase())) {
            case ADD:
                // 用户添加菜品
                msgBy = msgs[1];
                msgBody = msgs[2];
                // 有菜品 id 取得菜品名称 TODO: try catch it
                dishName = RestaurantWaitressGirl.dishMap.get(Long.parseLong(msgBody)).getDish_name();

                girl.addNewSelection(Long.parseLong(msgBody));
                msg = "++小伙伴@" + msgBy + "增加了[" + dishName + "]";
                showMsg(msg);

                break;
            case DELETE:
                // 用户删除菜品
                msgBy = msgs[1];
                msgBody = msgs[2];
                // 有菜品 id 取得菜品名称 TODO: try catch it
                dishName = RestaurantWaitressGirl.dishMap.get(Long.parseLong(msgBody)).getDish_name();

                // 之前又被点选过，所以现在去除
                int left = girl.removeSelection(Long.parseLong(msgBody));

                // 自定义 TextView,具体点选提示
                if (left > 0) {
                    msg = "--小伙伴@" + msgBy + "将" + dishName + "的数量减少1";
                } else {
                    msg = "--小伙伴@" + msgBy + "取消" + dishName;
                }
                showMsg(msg);
                break;
            case JOIN:
                msgBy = msgs[1];
                msg = "!!小伙伴@" + msgBy + "加入到点菜队伍了";
                showMsg(msg);
                break;
            case LEAVE:
                msgBy = msgs[1];
                msg = "??小伙伴@" + msgBy + "离开点菜队伍了";
                showMsg(msg);
                break;
            case SYNC_DATA:
                msgBy = msgs[1];
                msgBody = msgs[2];
                girl.addNewSelection(Long.parseLong(msgBody));
                MyLog.d(TAG, "Got a data sync, add " + msgBody + " by " + msgBy);
                refreshView();
                break;
            case SUBMIT_RESULT:
                String resultBody = msgs[1];
                Gson gson = new Gson();
                Map<Long, Integer> dishesOrderMap = gson.fromJson(resultBody, DishesMapBuilderForGson.class).getMap();
                showTheSubmitResultDialog(dishesOrderMap);
                break;
            case NOTSURE:
                // 没有定义的消息类型
                MyLog.d("WS点菜模块", "其他消息： " + payload);
                break;
        }
    }

    /**
     * 统一地向用户界面显示提示消息，在里面区分在菜品列表界面还是已选界面
     * @param msg
     */
    private void showMsg(String msg){
        if(isInSelectedListActivity){
            tvOMSGInSelectedList.setText(msg);
            tvOMSGInSelectedList.setVisibility(View.VISIBLE);
            handlerInSelectedList.postDelayed(runnableInSelectedList, TIME);
            handlerInSelectedList.post(this.reloadSelectedList);
        }else{
            tvOrderMessage.setText(msg);
            tvOrderMessage.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, TIME);
            btnSelector.setText("已点：" + girl.totalSelection());
            RestaurantDishesListActivity.valideTheCurrentListView();
        }

    }

    private void showTheSubmitResultDialog(Map<Long, Integer> dishesOrderMap){
        Iterator it = dishesOrderMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Long, Integer> en = (Map.Entry<Long, Integer>) it.next();
            long did = en.getKey();
            int count = en.getValue();
            MyLog.d(TAG, "The result order of the submit: did=" + did + " by count=" + count);
        }
    }


    /**
     * 有新的数据同步过来，只刷新列表，不进行提示
     */
    private void refreshView(){
        if(isInSelectedListActivity){
            handlerInSelectedList.post(this.reloadSelectedList);
        }else{
            btnSelector.setText("已点：" + girl.totalSelection());
            RestaurantDishesListActivity.valideTheCurrentListView();
        }

    }

    public void setInSelectedList(TextView tv, Handler handler, Runnable runnable, Runnable reloadList){
        isInSelectedListActivity = true;
        this.tvOMSGInSelectedList = tv;
        this.handlerInSelectedList = handler;
        this.runnableInSelectedList = runnable;
        this.reloadSelectedList = reloadList;
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
