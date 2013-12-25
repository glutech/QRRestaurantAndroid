package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.qrrestaurant.account.AccountManager;
import cn.com.zdez.qrrestaurant.helper.MakeUpRobot;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.layouts.DishesListAdapter;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.vo.DishesVo;

/**
 * 餐厅菜品展示界面，用于扫描之后开始点菜，也可以在餐厅详细信息界面手动点击"点菜"选项进入（用于用户远程预约）
 * 主要用于扫描跳转，扫描跳转后需要使用扫描的到的 tableId 到服务器得到餐厅信息和餐桌状态，这时需要联判断
 * 扫描跳转会附带餐桌编号， tableId， 如果有这个 tableId 则说明是来自餐桌点餐，则需要实时点餐模块配合完成
 * 如果没有 tableId, (tableId=null), 则说明来自预约点餐
 */
public class RestaurantDishesListActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    private boolean mToggleIndeterminate = false;

    private static Long mTableId;
    private static Long mRestaurantId;
    private View mProgressView;
    ActionBar actionBar = null;
    private AccountManager accountMagager;
    private static DishesVo dishesVo = null;


    private TextView tvAlert;
    private TextView tvLoadInfo;
    public static RestaurantWaitressGirl rh;
    public static TextView tvOrderMssage;

    public static Button btnSelectedCounter;

    public static int TIME = 800;
    public static Handler handler = new Handler();
    public static Runnable runnable;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private static String TAG = RestaurantDishesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_restaurant_dishes_list);

        tvAlert = (TextView) findViewById(R.id.tv_alert_dishlist);
        tvLoadInfo = (TextView) findViewById(R.id.tv_onloading);
        tvOrderMssage = (TextView) findViewById(R.id.tv_order_message);
        btnSelectedCounter = (Button) findViewById(R.id.btn_selected_counter);

        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 配置任务
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    MyLog.d(TAG, "Got the runnable work finally");
                    tvOrderMssage.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        // 账户管理
        accountMagager = QRRestaurantApplication.accountManager;


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);


//        // 显示正在加载的界面
//        showProgress(true);

        // 加载餐厅信息和菜品
        loadRestaurantInfo();
        setSelectIntent();

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    /**
     * 根据 activity 跳转所携带的信息决定当前 activity 所处的上下文（是扫描点餐还是选择点餐）
     * 从而加载数据
     */
    private void loadRestaurantInfo() {

        Intent intent = getIntent();
        String url = "";
        String paramKey = "";
        String paramValue = "";

        if (!ConnectivityUtil.isOnline(this) && !MyLog.DEBUG) {
            // 提示网络连接错误
            MyLog.d(TAG, "没有联网...");
            actionBar.setTitle(getResources().getString(R.string.app_name) + "(未连接)");
            tvAlert.setText(getResources().getString(R.string.msg_alert_network_disconnect));
            tvLoadInfo.setVisibility(View.GONE);
            tvAlert.setVisibility(View.VISIBLE);
        } else {
            // 请求餐厅所有信息
            // 如果过 intent 过来的信息中含有 table id 则开始使用 table id 请求餐厅信息，准备点餐的数据
            if (intent.hasExtra("tid")) {
                mTableId = Long.parseLong(intent.getStringExtra("tid"));
                url = Constants.SCAN_TO_ORDER;
                paramKey = "t_id";
                paramValue = mTableId.toString();
            } else if (intent.hasExtra("rid")) {
                // 如果 intent 过来的信息中含有 restaurant id，意味着是点选餐厅进行点餐，直接使用 rid请求餐厅信息
                long rid = intent.getLongExtra("rid", -1);
//                url = Constants.CHOOSE_TO_ORDER;
//                paramKey = "r_id";
//                paramValue = String.valueOf(rid);
                // TODO: 服务器完成接口后回复上面的代码
                // 目前将就使用tid 完成请求和展示
                url = Constants.SCAN_TO_ORDER;
                paramKey = "t_id";
                paramValue = "2";
            }

            retriveData(url, paramKey, paramValue);
        }
    }

    /**
     * 设置好基本元素之后开始请求填充数据
     * 扫描点餐和预订点餐的请求方式不一样，一个知道桌号ID，获取餐厅菜品列表，一个知道餐厅ID，获取餐厅菜品列表
     * 所以URL，和参数都不一样，但结果是一样的
     *
     * @param url
     * @param paramKey
     * @param paramValue
     */
    private void retriveData(String url, String paramKey, String paramValue) {
        // 转圈圈
        mToggleIndeterminate = !mToggleIndeterminate;
        setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
        setSupportProgressBarIndeterminate(true);

        RequestParams params = new RequestParams();
        params.put(paramKey, paramValue);
        QRRHTTPClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                MyLog.d(TAG, "Start to post tid to get restaurant info...");
                super.onStart();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                MyLog.d(TAG, "请求餐厅信息失败 ：" + content);
                // 停止progress
                mToggleIndeterminate = !mToggleIndeterminate;
                setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);

                // 显示错误信息
                // 在不联网或者没有服务器的时候生成假的数据用于演示使用
                if (MyLog.DEBUG) {
                    dishesVo = MakeUpRobot.getDishVo();
                    if (null != dishesVo) {
                        // json 数据反序列化成功，得到扫描结果实体，开始填充菜品列表详细数据
                        MyLog.d(TAG, "Parse json out, restName: " + dishesVo.getRest_name() + " and the dish: " + dishesVo.getDishlist().get(0).getDish_name());
                    }

                    // 取得餐厅数据之后，首先要修改 tab pager 的参数，在取得数据之前使用默认的
                    rh = RestaurantWaitressGirl.getInstance(dishesVo);

                    tvLoadInfo.setVisibility(View.GONE);
                    mToggleIndeterminate = !mToggleIndeterminate;
                    setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);

                    setFragmentPagerTitle();

                    // 设置标题
                    actionBar.setTitle(dishesVo.getRest_name());
                } else {
                    tvAlert.setText(getResources().getString(R.string.msg_alert_server_down));
                    tvLoadInfo.setVisibility(View.GONE);
                    tvAlert.setVisibility(View.VISIBLE);
                }

                super.onFailure(error, content);
            }

            @Override
            public void onSuccess(String content) {
                MyLog.d(TAG, "Success get rest info: " + content);
                // 成功从服务器请求到餐厅信息 json 数据
                Gson gson = new Gson();

                try {
                    dishesVo = gson.fromJson(content, DishesVo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mRestaurantId = Long.parseLong(dishesVo.getRest_id());

                // 取得餐厅数据之后，首先要修改 tab pager 的参数，在取得数据之前使用默认的
                rh = RestaurantWaitressGirl.getInstance(dishesVo);

//                        try {
//                            MyLog.d(TAG, "-------------This is huge--------------------");
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                mToggleIndeterminate = !mToggleIndeterminate;
                setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
                tvLoadInfo.setVisibility(View.GONE);

                setFragmentPagerTitle();

                // 设置标题
                actionBar.setTitle(dishesVo.getRest_name());

                super.onSuccess(content);

            }
        });
    }

    private void setFragmentPagerTitle() {

        // 初始化 pagerAdapter
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MyLog.d(TAG, "ViewPager's page change listener, now selected: " + position);
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return rh.categoryNameArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return rh.categoryNameArray[position];
        }
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

        ListView dishesListView;
        DishesListAdapter dishesListAdapter;
        List<Dish> dishList;
        String tableName;

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

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            tableName = rh.categoryNameArray[getArguments().getInt(ARG_SECTION_NUMBER) - 1];
            MyLog.d(TAG, "Oncreateview while create placeholder fragment and it's section is: " + getArguments().getInt(ARG_SECTION_NUMBER) + " and the cate name is : " + tableName);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_dishes_list, container, false);

            MyLog.d(TAG, "---------------------on Fragment create view----------------------+" + tableName);
            dishList = new ArrayList<Dish>(rh.catedDishMap.get(rh.categoryNameArray[getArguments().getInt(ARG_SECTION_NUMBER) - 1]));
            dishesListAdapter = new DishesListAdapter(getActivity(), R.id.lv_dishes_list, dishList);

            dishesListView = (ListView) rootView.findViewById(R.id.lv_dishes_list);
            dishesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            dishesListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                    Toast.makeText(getActivity(), i + " item checkedstate changed ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }
            });

            dishesListView.setAdapter(dishesListAdapter);

            dishesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            dishesListView.setItemsCanFocus(false);
            dishesListView.setClickable(true);

            // 列表点击事件，处理菜单选择操作
            dishesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MyLog.d(TAG, "Item with index " + position + "clicked");


                    String selectItem = dishList.get(position).getDish_name();

                    if (rh.isSelected(dishList.get(position).getDish_id())) {
                        // 之前又被点选过，所以现在去除
                        int left = rh.removeSelection(dishList.get(position).getDish_id());

                        // 自定义 TextView,具体点选提示
                        if (left > 0) {
                            tvOrderMssage.setText("将" + selectItem + "的数量减少1");
                        } else {
                            tvOrderMssage.setText("取消" + selectItem);
                        }

                        tvOrderMssage.setVisibility(View.VISIBLE);
                        handler.postDelayed(runnable, TIME);

//                        mConnection.sendTextMessage("delete " + String.valueOf(position));
                        // 统计按钮中增加数目
                        btnSelectedCounter.setText("已点：" + rh.totalSelection());
                        dishesListView.invalidateViews();
                    } else {
                        // 之前没被点选过，添加选择
                        rh.addNewSelection(dishList.get(position).getDish_id());
                        tvOrderMssage.setText("选择了" + selectItem);
                        tvOrderMssage.setVisibility(View.VISIBLE);
                        handler.postDelayed(runnable, TIME);
                        btnSelectedCounter.setText("已点：" + rh.totalSelection());
                        dishesListView.invalidateViews();
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onResume() {
            MyLog.d(TAG, "On placeholder fragment resume, and the section number is: " + getArguments().getInt(ARG_SECTION_NUMBER));
            dishesListView.invalidateViews();
            super.onResume();
        }

        @Override
        public void onPause() {
            dishesListView.invalidateViews();
            super.onPause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant_dishes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent restDetailIntent = new Intent();
                restDetailIntent.setClass(RestaurantDishesListActivity.this, RestaurantDetailActivity.class);
                restDetailIntent.putExtra("rid", mRestaurantId);
                startActivity(restDetailIntent);
                return true;
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                // 由 Table ID 是否存在来决定 parentActivity 是那个
                Intent upIntent;
                if (mTableId != null) {
                    upIntent = new Intent(this, QRRMainActivity.class);
                } else {
                    upIntent = new Intent(this, RestaurantChooserActivity.class);
                }

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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    private void setSelectIntent() {
        btnSelectedCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSelectedList = new Intent();
                toSelectedList.setClass(RestaurantDishesListActivity.this, SelectedDishesActivity.class);
                startActivity(toSelectedList);
            }
        });
    }
}
