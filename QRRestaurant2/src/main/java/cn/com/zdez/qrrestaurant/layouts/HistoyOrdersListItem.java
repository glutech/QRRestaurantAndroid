package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.vo.HistoryVo;

/**
 * TODO: create the history orders layout
 * Created by LuoHanLin on 13-12-27.
 */
public class HistoyOrdersListItem extends RelativeLayout {

    private Context context;
    private TextView tvRestName;
    private TextView tvOrderDate;
    private TextView tvOrderTotalProce;

    public HistoyOrdersListItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
    }

    public void setLayout(HistoryVo his) {
        findView();

        tvRestName.setText(his.getRest_name());
        tvOrderDate.setText(his.getMe().getMenu_time());
        tvOrderTotalProce.setText("￥" + his.getMe().getMenu_price());
    }

    private void findView() {
        tvRestName = (TextView) findViewById(R.id.his_order_rest_name);
        tvOrderDate = (TextView) findViewById(R.id.his_order_date);
        tvOrderTotalProce = (TextView) findViewById(R.id.his_order_price);
    }
}
