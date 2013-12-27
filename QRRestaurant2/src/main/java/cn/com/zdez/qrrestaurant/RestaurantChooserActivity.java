package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class RestaurantChooserActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    /**
     * 在预订的时候要进行餐厅的选择，首先像服务器请求餐厅列表
     * TODO: 加入餐厅的选择条件（定位周边，搜索关键字，推荐列表，用户查看过的历史餐厅列表）
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private static String TAG = RestaurantChooserActivity.class.getSimpleName();
    private boolean mToggleIndeterminate = false;
    private TextView tvAlert;
    private TextView tvLoadInfo;
    public static List<Restaurant> restaurantList;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_restaurant_chooser);

        tvAlert = (TextView) findViewById(R.id.tv_rests_alert);
        tvLoadInfo = (TextView) findViewById(R.id.tv_onloading);

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
            actionBar.setTitle(getResources().getString(R.string.title_activity_restaurant_chooser) + "(未连接)");
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

    /**
     * 设置 actionbar 和下拉列表
     */
    private void setTheList() {
        // Set up the action bar to show a dropdown list.
//        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        getMenuInflater().inflate(R.menu.restaurant_chooser, menu);
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
                Intent upIntent = new Intent(this, QRRMainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        ListView lvRestaurant;
        RestaurantListAdapter listAdapter;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_chooser, container, false);

            lvRestaurant = (ListView) rootView.findViewById(R.id.lv_restaurants_list);

            // TODO: 在此分离餐厅列表，附近（按城市、地区，区分）的和推荐的
            // 目前全部一样
            listAdapter = new RestaurantListAdapter(getActivity(), R.id.lv_restaurants_list, restaurantList);
            lvRestaurant.setAdapter(listAdapter);

            // 列表点击，跳转餐厅详情
            lvRestaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent detailIntent = new Intent();
                    detailIntent.setClass(getActivity(), RestaurantDishesListActivity.class);
//                    detailIntent.putExtra("rid", restaurantList.get(i).getRest_id());
                    detailIntent.putExtra("tid", "2");
                    startActivity(detailIntent);
                }
            });

            // Old list samples
//            ListView resList = (ListView) rootView.findViewById(R.id.section_list_view);
//            final ArrayList<String> list = new ArrayList<String>();
//            String tempStr;
//            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
//                tempStr = getString(R.string.title_section1);
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//                tempStr = getString(R.string.title_section2);
//            } else
//                tempStr = getString(R.string.title_section3);
//
//            for (int i = 0; i < 30; i++) {
//                list.add(tempStr + "_" + i);
//            }
//
//            resList.setAdapter(new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, list));
//
//            resList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent intent = new Intent();
//                    intent.putExtra("tid", "2");
//                    intent.setClass(getActivity(), RestaurantDetailActivity.class);
//                    startActivity(intent);
//                }
//            });

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
