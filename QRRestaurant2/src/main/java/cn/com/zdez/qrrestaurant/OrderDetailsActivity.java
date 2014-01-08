package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.layouts.DishBriefListAdapter;
import cn.com.zdez.qrrestaurant.layouts.DishesListAdapter;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.ListViewUtil;
import cn.com.zdez.qrrestaurant.vo.MenuVo;

public class OrderDetailsActivity extends ActionBarActivity {

    private static String TAG = OrderDetailsActivity.class.getSimpleName();
    private String mId;
    private TextView tvRestName;
    private TextView tvOrderStatus;
    private TextView tvRestAddress;
    private TextView tvOrderDate;
    private ListView lvOrderDishes;
    private Intent intent;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvOrderDate = (TextView) findViewById(R.id.tv_order_date);
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvRestName = (TextView) findViewById(R.id.tv_order_rest_name);
        tvRestAddress = (TextView) findViewById(R.id.tv_order_rest_address);
        lvOrderDishes = (ListView) findViewById(R.id.lv_dishes_in_order_detail);

        intent = getIntent();
        mId = intent.getStringExtra("m_id");

        actionBar = getSupportActionBar();

        loadMenuDetail();
        actionBar.setDisplayHomeAsUpEnabled(true);
//
    }

    private void loadMenuDetail() {
        RequestParams params = new RequestParams();
        params.put("m_id", mId);
        QRRHTTPClient.post(Constants.GET_MENU_VO, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                Gson gson = new Gson();
                MenuVo mv = null;
                try {
                    mv = gson.fromJson(content, MenuVo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

                if (null != mv) {
                    tvOrderDate.append(mv.getMenu().getMenu_time().toString());
                    tvRestName.append(String.valueOf(mv.getMenu().getRest_id()));
                    tvOrderStatus.append(String.valueOf(mv.getMenu().getMenu_status()));
                    DishBriefListAdapter adapter = new DishBriefListAdapter(OrderDetailsActivity.this, R.id.lv_dishes_in_order_detail, mv.getDishes());
                    lvOrderDishes.setAdapter(adapter);
                    ListViewUtil.setListViewHeightBasedOnChildren(lvOrderDishes);
                }

            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
