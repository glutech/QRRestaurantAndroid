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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.MyLog;

/**
 * 从 intent 中取得餐厅编号，到服务器中取得餐厅的详细信息,与扫描无关，与桌号
 */
public class RestaurantDetailActivity extends ActionBarActivity {

    private static String TAG = RestaurantDetailActivity.class.getSimpleName();
    private static Long mRestId;
    private ActionBar actionBar;
    private TextView tvAlert;
    private TextView tvOnLoad;
    public static Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        tvAlert = (TextView) findViewById(R.id.tv_alert_dishlist);
        tvOnLoad = (TextView) findViewById(R.id.tv_onloading);

        // Set up the action bar.
        actionBar = getSupportActionBar();

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadRestaurantInfo();

    }


    public void loadRestaurantInfo() {
        // 跳转过来的 intent
        Intent intent = getIntent();
        if (intent.hasExtra("rid")) {
            // 能取到rid
            mRestId = intent.getLongExtra("rid", -1);
            RequestParams params = new RequestParams();
            params.put("r_id", mRestId.toString());
            QRRHTTPClient.post(Constants.GET_RESTAURANT_DETAIL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onFailure(Throwable error, String content) {
                    tvOnLoad.setVisibility(View.GONE);
                    tvAlert.setText(getResources().getString(R.string.msg_alert_server_down));
                    tvAlert.setVisibility(View.VISIBLE);
                    super.onFailure(error, content);
                }

                @Override
                public void onSuccess(String content) {
                    tvOnLoad.setVisibility(View.GONE);
                    MyLog.d(TAG, "Success with get restaurant info: " + content);
                    Gson gson = new Gson();

                    try {
                        // TODO: 这里取到的数据理应含有餐厅评论，在服务器实现后修改饭序列实体，并在接下来的操作中完成 list 的显示
                        restaurant = gson.fromJson(content, Restaurant.class);
//                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new PlaceholderFragment())
                            .commit();

                    super.onSuccess(content);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
//                Intent upIntent = new Intent(this, RestaurantDishesListActivity.class);
//                //TODO: 检查这里的代码,这里的返回应该直接 finish
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is not part of the application's task, so create a new task
//                    // with a synthesized back stack.
//                    TaskStackBuilder.from(this)
//                            // If there are ancestor activities, they should be added here.
//                            .addNextIntent(upIntent)
//                            .startActivities();
//                    finish();
//                } else {
//                    // This activity is part of the application's task, so simply
//                    // navigate up to the hierarchical parent activity.
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TextView tvRestName;
        private TextView tvLabel;
        private TextView tvAddr;
        private TextView tvRestDesc;
        private ListView lvRComments;
        private Button btnStartOrder;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
            btnStartOrder = (Button) rootView.findViewById(R.id.btn_start_order);
            tvRestName = (TextView) rootView.findViewById(R.id.tv_rest_info_name);
            tvAddr = (TextView) rootView.findViewById(R.id.tv_rest_info_addr);
            tvLabel = (TextView) rootView.findViewById(R.id.tv_rest_info_label);
            tvRestDesc = (TextView) rootView.findViewById(R.id.tv_rest_info_desc);
            lvRComments = (ListView) rootView.findViewById(R.id.lv_rest_info_comments);

            tvRestName.setText(restaurant.getRest_name());
            tvAddr.setText(restaurant.getRest_addr());
            tvLabel.setText(restaurant.getRest_type());
            tvRestDesc.setText(restaurant.getRest_desc());

            btnStartOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toOrder = new Intent();
                    toOrder.putExtra("tid", "2");
                    toOrder.setClass(getActivity(), RestaurantDishesListActivity.class);
                    startActivity(toOrder);
                }
            });

            return rootView;
        }
    }

}
