package cn.com.zdez.qrrestaurant;

import android.content.Intent;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import cn.com.zdez.qrrestaurant.entities.Dish;
import cn.com.zdez.qrrestaurant.entities.DishesSelectList;
import cn.com.zdez.qrrestaurant.utils.DishesListAdapter;
import cn.com.zdez.qrrestaurant.utils.MakeUpRobot;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.wsordermodule.WSConnection;
import de.tavendo.autobahn.WebSocketConnection;

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


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private static String TAG = RestaurantDishesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dishes_list);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 暂时先随便添加一些东西
        Intent intent = getIntent();
        String res_id = intent.getStringExtra("res_id");
        actionBar.setTitle(res_id);

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
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
                Intent upIntent = new Intent(this, RestaurantChooserActivity.class);
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
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.pages_title1).toUpperCase(l);
                case 1:
                    return getString(R.string.pages_title2).toUpperCase(l);
                case 2:
                    return getString(R.string.pages_title3).toUpperCase(l);
                case 3:
                    return getString(R.string.pages_title4).toUpperCase(l);
                case 4:
                    return getString(R.string.pages_title5).toUpperCase(l);

            }
            return null;
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
        WebSocketConnection mConnection;


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
            ListView dishesList = (ListView) rootView.findViewById(R.id.plates_list_view);
            dishesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            final Button btnSelectedCounter = (Button) rootView.findViewById(R.id.btn_selected_counter);
            btnSelectedCounter.setText("已点：" + DishesSelectList.getCounter());


            // 测试用，在此模拟在某张餐桌上点餐的操作
            // 首先，建立到服务器餐桌上的 ws 连接
            long uid = 100;
            long tid = 13;
            WSConnection.getInstance(getActivity()).connect(tid, uid);
            mConnection = WSConnection.getInstance(getActivity()).mConnection;

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

            dishesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

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


            final ArrayList<Dish> dishes = MakeUpRobot.getDishes();
            final DishesListAdapter dishesListAdapter = new DishesListAdapter(getActivity(), R.id.plates_list_view, dishes);
            dishesList.setAdapter(dishesListAdapter);

//            dishesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//            dishesList.setItemsCanFocus(false);
//            dishesList.setClickable(true);

            dishesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    String selectItem = dishes.get(position).getdName();

                    if (!dishesListAdapter.isSelected(position)) {
                        Toast.makeText(getActivity(), "取消" + selectItem, Toast.LENGTH_SHORT).show();
                        DishesSelectList.remove(selectItem);
                        mConnection.sendTextMessage("delete " + String.valueOf(position));
                        btnSelectedCounter.setText("已点：" + DishesSelectList.getCounter());
                    } else {
                        DishesSelectList.add(selectItem);
                        Toast.makeText(getActivity(), "已将" + selectItem + "放入菜单", Toast.LENGTH_SHORT).show();
                        mConnection.sendTextMessage("add " + String.valueOf(position));
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
            mConnection.disconnect();
            super.onDestroy();
        }
    }

}
