package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.zdez.qrrestaurant.helper.MakeUpRobot;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.layouts.RestaurantListAdapter;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.MyLog;

public class RestaurantSimpleChooserActivity extends ActionBarActivity {

    /**
     * TODO: 实现复杂的餐厅选择功能，请使用 RestaurantChooserActivity
     * 在预订的时候要进行餐厅的选择，首先像服务器请求餐厅列表
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static String TAG = RestaurantSimpleChooserActivity.class.getSimpleName();
    private boolean mToggleIndeterminate = false;
    private TextView tvAlert;
    private TextView tvLoadInfo;
    public List<Restaurant> restaurantList;
    ActionBar actionBar;
    ListView lvRestaurant;
    RestaurantListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_restaurant_simple_chooser);

        tvAlert = (TextView) findViewById(R.id.tv_rest_cs_alert);
        tvLoadInfo = (TextView) findViewById(R.id.tv_rest_cs_onloading);
        lvRestaurant = (ListView) findViewById(R.id.lv_rest_cs_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar = getSupportActionBar();


        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadRestaurantList();
    }

    /**
     * 从服务请求餐厅列表，加载列表，生成 dropdownlist
     */
    private void loadRestaurantList() {
        if (!ConnectivityUtil.isOnline(this) && !MyLog.DEBUG) {
            // 提示网络连接错误
            MyLog.d(TAG, "没有连接网络...");
            actionBar.setTitle(getResources().getString(R.string.title_activity_restaurant_simple_chooser) + "(未连接)");
            tvAlert.setText(R.string.msg_alert_network_disconnect);
            tvLoadInfo.setVisibility(View.GONE);
            tvAlert.setVisibility(View.VISIBLE);
        } else {
            // 网络连接正常
            // 旋转吧，菊花
            mToggleIndeterminate = !mToggleIndeterminate;
            setSupportProgressBarIndeterminate(true);
            setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
            RequestParams params = new RequestParams();
            QRRHTTPClient.post(Constants.GETRESTAURANT_LIST_ALL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(Throwable error, String content) {
                    MyLog.e(TAG, "Failure on request all restaurant list...");
                    // 服务器不响应
                    // 如果在测试阶段，允许使用瞎编的数据代替那个不争气的服务器所提供的数据
                    if (MyLog.DEBUG) {
                        restaurantList = MakeUpRobot.makeUpRestaurantList();
                        mToggleIndeterminate = !mToggleIndeterminate;
                        setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
                        tvLoadInfo.setVisibility(View.GONE);
                        setTheList();
                    } else {
                        // 不在测试阶段，服务器不响应的话，给用户提示信息
                        actionBar.setTitle(getResources().getString(R.string.title_activity_restaurant_chooser) + "(未连接)");
                        tvAlert.setText(getResources().getString(R.string.msg_alert_server_down));
                        tvLoadInfo.setVisibility(View.GONE);
                        tvAlert.setVisibility(View.VISIBLE);
                    }

                    super.onFailure(error, content);
                }

                @Override
                public void onSuccess(String content) {
                    MyLog.v(TAG, "成功请求到餐厅列表json...It's: " + content);
                    Gson gson = new Gson();
                    Restaurant[] rests = null;
                    try {
                        // JSON反序列化
                        rests = gson.fromJson(content, Restaurant[].class);
                    } catch (Exception e) {
                        MyLog.e(TAG, "Op Serialization exception...");
                        e.printStackTrace();
                    }

                    if (rests != null && rests.length > 0) {
                        restaurantList = new ArrayList<Restaurant>(Arrays.asList(rests));
                    } else {
                        Log.d(TAG, "Got null rests or rests has not entry, It's :" + rests);
                    }
//                    try {
//                        MyLog.d(TAG, "-------------This is huge--------------------");
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    // 取消加载信息
                    mToggleIndeterminate = !mToggleIndeterminate;
                    setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
                    tvLoadInfo.setVisibility(View.GONE);

                    // 得到数据后开始加载 list
                    // TODO: 显示成功加载的信息，或者在列表显示完成之后显示
                    setTheList();

                    super.onSuccess(content);
                }
            });
        }
    }

    private void setTheList() {
        // 目前全部一样
        listAdapter = new RestaurantListAdapter(RestaurantSimpleChooserActivity.this, R.id.lv_rest_cs_list, restaurantList);
        lvRestaurant.setAdapter(listAdapter);

        // 列表点击，跳转餐厅详情
        lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent();
                detailIntent.setClass(RestaurantSimpleChooserActivity.this, RestaurantDishesListActivity.class);
                detailIntent.putExtra("rid", restaurantList.get(i).getRest_id());
                startActivity(detailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.restaurant_simple_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
