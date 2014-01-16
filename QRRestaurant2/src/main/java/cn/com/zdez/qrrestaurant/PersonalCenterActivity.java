package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.layouts.HistoryOrdersAdapter;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.ListViewUtil;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.vo.HistoryVo;

public class PersonalCenterActivity extends ActionBarActivity {

    private static String TAG = PersonalCenterActivity.class.getSimpleName();

    private ListView lvHistoryOrderList;
    private TextView tvTmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        lvHistoryOrderList = (ListView) findViewById(R.id.lv_orders_in_pcenter);
        loadData();

    }

    private void loadData() {
        RequestParams params = new RequestParams();
        params.put("c_id", String.valueOf(QRRestaurantApplication.getUserID()));
        QRRHTTPClient.post(Constants.GET_ORDER_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                MyLog.d(TAG, "Got the orders list:" + content);
                try {
                    final HistoryVo[] hists = new Gson().fromJson(content, HistoryVo[].class);
                    if (hists != null && hists.length > 0) {
                        HistoryOrdersAdapter hisAdapter = new HistoryOrdersAdapter(PersonalCenterActivity.this, R.id.lv_orders_in_pcenter, hists);
                        lvHistoryOrderList.setAdapter(hisAdapter);
                        ListViewUtil.setListViewHeightBasedOnChildren(lvHistoryOrderList);

                        lvHistoryOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent orderDetailIntent = new Intent();
                                orderDetailIntent.setClass(PersonalCenterActivity.this, OrderDetailsActivity.class);
                                orderDetailIntent.putExtra("m_id", String.valueOf(hists[i].getMe().getMenu_id()));
                                startActivity(orderDetailIntent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.personal_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
