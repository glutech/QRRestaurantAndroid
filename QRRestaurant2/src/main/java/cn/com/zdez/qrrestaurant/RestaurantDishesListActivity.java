package cn.com.zdez.qrrestaurant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.com.zdez.qrrestaurant.account.AccountManager;
import cn.com.zdez.qrrestaurant.entities.DishesSelectList;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.DishesByCategorySectionPagerAdapter;
import cn.com.zdez.qrrestaurant.utils.DishesListAdapter;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;
import cn.com.zdez.qrrestaurant.vo.DishVo;
import cn.com.zdez.qrrestaurant.vo.ScanResultVo;

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

    private static String mTableId;
    private static String mRestaurantId;
    private View mProgressView;
    ActionBar actionBar = null;
    private AccountManager accountMagager;
    private static ScanResultVo scanResult = null;
    private static ListView dishesListView;
    private static DishesListAdapter dishesListAdapter;
    private static List<DishVo> dishList = new ArrayList<DishVo>();

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



        // Set up the action bar.
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 账户管理
        accountMagager = QRRestaurantApplication.accountManager;

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);


//        // 显示正在加载的界面
//        showProgress(true);

        // 加载餐厅信息和菜品
        loadRestaurantInfo();

        actionBar.setTitle("餐桌号：" + mTableId);

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setFragmentPagerTitle(List<Category> cats) {
        mSectionsPagerAdapter.setCats(cats);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
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
     * 根据 activity 跳转所携带的信息决定当前 activity 所处的上下文（是扫描点餐还是选择点餐）
     * 从而加载数据
     */
    private void loadRestaurantInfo() {
        Intent intent = getIntent();

        if (!ConnectivityUtil.isOnline(this)) {
            // 提示网络连接错误
            MyLog.d(TAG, "没有联网...");
            ToastUtil.showShortToast(this, "网络连接错误，请检查网络环境！");
        } else {
            // 请求餐厅所有信息
            // 如果过 intent 过来的信息中含有 table id 则开始使用 table id 请求餐厅信息，准备点餐的数据
            if (intent.hasExtra("tid")) {

                // 转圈圈
                mToggleIndeterminate = !mToggleIndeterminate;
                setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);
                setSupportProgressBarIndeterminate(true);

                mTableId = intent.getStringExtra("tid");
                RequestParams params = new RequestParams();
                params.put("t_id", mTableId);
                QRRHTTPClient.post(Constants.TABLE_TAKE_POST_URL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        MyLog.d(TAG, "Start to post tid to get restaurant info...");
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(String content) {
                        MyLog.d(TAG, "Success get rest info: " + content);
                        // 成功从服务器请求到餐厅信息 json 数据
                        Gson gson = new Gson();

                        scanResult = gson.fromJson(content, ScanResultVo.class);
                        if (null != scanResult) {
                            // json 数据反序列化成功，得到扫描结果实体，开始填充菜品列表详细数据
                            MyLog.d(TAG, "Parse json out, restName: " + scanResult.getRest().getRest_name() + " and the dish: " + scanResult.getDishes().get(0).getDish().getDish_name());
                        }

                        // 取得餐厅数据之后，首先要修改 tab pager 的参数，在取得数据之前使用默认的
                        List<Category> cats = new ArrayList<Category>();
                        for (DishVo dishVo : scanResult.getDishes()) {
                            cats.add(dishVo.getCat());
                        }

                        mToggleIndeterminate = !mToggleIndeterminate;
                        setSupportProgressBarIndeterminateVisibility(mToggleIndeterminate);

                        setFragmentPagerTitle(cats);

                        // 然后更新 list
                        dishList.clear();
                        dishList.addAll(scanResult.getDishes());
                        dishesListAdapter.notifyDataSetChanged();


                        super.onSuccess(content);

                    }
                });
            } else if (intent.hasExtra("rid")) {
                // 如果 intent 过来的信息中含有 restaurant id，意味着是点选餐厅进行点餐，直接使用 rid请求餐厅信息
                // TODO: 使用餐厅 id 请求餐厅信息，准备预订点餐的信息
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant_plate_list, menu);
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
                // 由 Table ID 是否存在来决定 parentActivity 是那个
                Intent upIntent;
                if(mTableId != null){
                    upIntent = new Intent(this, QRRMainActivity.class);
                }else{
                    upIntent = new Intent(this, RestaurantDetailActivity.class);
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Category> cats;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setCats(List<Category> cats) {
            this.cats = cats;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return cats.size() + 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.pages_default_title1).toUpperCase(l);
                case 1:
                    return getString(R.string.pages_default_title2).toUpperCase(l);
                default:
                    return cats.get(position - 2).getCat_name();

            }
//            return null;
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


        // 测试用的 websocket connection
//        WebSocketConnection mConnection;


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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_dishes_list, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            dishesListView = (ListView) rootView.findViewById(R.id.plates_list_view);
            dishesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            final Button btnSelectedCounter = (Button) rootView.findViewById(R.id.btn_selected_counter);
            btnSelectedCounter.setText("已点：" + DishesSelectList.getCounter());


            // 测试用，在此模拟在某张餐桌上点餐的操作
            // 首先，建立到服务器餐桌上的 ws 连接
//            WSConnection.getInstance(getActivity()).connect(mTableId);
//            mConnection = WSConnection.getInstance(getActivity()).mConnection;

//            final ArrayList<String> list = new ArrayList<String>();
//            String tempStr = "";
//            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
//                tempStr = getString(R.string.pages_title1);
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//                tempStr = getString(R.string.pages_title2);
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
//                tempStr = getString(R.string.pages_title3);
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
//                tempStr = getString(R.string.pages_title4);
//            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
//                tempStr = getString(R.string.pages_title5);
//            }
//
//            for (int i = 0; i < 30; i++) {
//                list.add(tempStr + "_" + i);
//            }

            // 按分类更新当前 pager 的列表
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    // 推荐菜品
                    MyLog.d(TAG, "Switch to pager 1");
                    break;
                case 2:
                    // 点击排行菜品
                    MyLog.d(TAG, "Switch to pager 2");
                    break;
                default:
                    // 剩下的是按照菜品分类
                    MyLog.d(TAG, "Switch to pager" + getArguments().getInt(ARG_SECTION_NUMBER));

                    break;
            }

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


            dishesListAdapter = new DishesListAdapter(getActivity(), R.id.plates_list_view, dishList);
            dishesListView.setAdapter(dishesListAdapter);

//            final ArrayList<Dish> dishes = MakeUpRobot.getDishes();
//            final DishesListAdapter dishesListAdapter = new DishesListAdapter(getActivity(), R.id.plates_list_view, dishes);
//            dishesList.setAdapter(dishesListAdapter);

            dishesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            dishesListView.setItemsCanFocus(false);
            dishesListView.setClickable(true);

            dishesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MyLog.d(TAG, "Item with index " + position + "clicked");
//                    Dish dish = dishes.get(i);

//                    // Set the dish selected if it is not selected before
//                    if (dishes.get(i).isSelectedInList()) {
//                        dish.setSelectedInList(true);
//                        view.setBackgroundColor(getResources().getColor(R.color.blue));
//                    } else {
//                        //Other wise it should be unselected
//                        dish.setSelectedInList(false);
//                    }

//                    dishes.set(i, dish);
//                    dishesListAdapter.notifyDataSetChanged();


                    dishesListAdapter.addNewSelection(position, !dishesListAdapter.isSelected(position));
                    String selectItem = dishList.get(position).getDish().getDish_name();

                    if (!dishesListAdapter.isSelected(position)) {
                        Toast.makeText(getActivity(), "取消" + selectItem, Toast.LENGTH_SHORT).show();
                        DishesSelectList.remove(selectItem);
//                        mConnection.sendTextMessage("delete " + String.valueOf(position));
                        btnSelectedCounter.setText("已点：" + DishesSelectList.getCounter());
                    } else {
                        DishesSelectList.add(selectItem);
                        Toast.makeText(getActivity(), "已将" + selectItem + "放入菜单", Toast.LENGTH_SHORT).show();
//                        mConnection.sendTextMessage("add " + String.valueOf(position));
                        btnSelectedCounter.setText("已点：" + DishesSelectList.getCounter());
                    }
                }
            });

            btnSelectedCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DishesSelectedListActivity.class);
                    startActivity(intent);
                }
            });

            return rootView;
        }

        @Override
        public void onDestroy() {
//            mConnection.disconnect();
            super.onDestroy();
        }
    }

}
