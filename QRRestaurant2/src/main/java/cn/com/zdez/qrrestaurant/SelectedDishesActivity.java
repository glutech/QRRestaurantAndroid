package cn.com.zdez.qrrestaurant;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
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

import java.util.List;

import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.layouts.DishesSelectedAdapter;
import cn.com.zdez.qrrestaurant.layouts.SubmitResultConfirmDialog;
import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * 用于两个场景，都是用来显示已选择的菜单和继续添加菜品的
 * 当 intent 中含有桌号 t_id 的时候说明是在现场点餐，需要协同点餐操作
 * 而当 intent 中含有餐厅号,不含有 r_id 的时候，不需要协同点餐，提交操作也不一样
 */
public class SelectedDishesActivity extends ActionBarActivity {

    ActionBar actionBar;
    public static RestaurantWaitressGirl girl;
    private static Runnable runnable;
    public static Handler handler = new Handler();
    private static TextView tvOrderMsg;
    private static Runnable runReloadTheList;
    private static Runnable runTheSubmitResultDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_dishes);

        actionBar = getSupportActionBar();
        girl = RestaurantWaitressGirl.getInstance();

        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    tvOrderMsg.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.title_activity_selected_dishes));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selected_dishes, menu);
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
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ListView lvSelectedDishes;
        //        private TextView tvSelectedCount;
        private TextView tvTotalPrice;
        private Button btnSubmmitSelected;
        private TextView tvTotalSelect;
        private List<Dish> selectedDishes;
        private Dialog resultConfirmDialog;


        public PlaceholderFragment() {
            selectedDishes = girl.getSelectedDishList();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_selected_dishes, container, false);
            lvSelectedDishes = (ListView) rootView.findViewById(R.id.lv_selected_dishes);
//            tvSelectedCount = (TextView) rootView.findViewById(R.id.tv_item_seleted_count);
            tvTotalPrice = (TextView) rootView.findViewById(R.id.tv_selected_total_price);
            btnSubmmitSelected = (Button) rootView.findViewById(R.id.btn_selected_list_submit);
            tvTotalSelect = (TextView) rootView.findViewById(R.id.tv_selected_list_total);

            tvOrderMsg = (TextView) rootView.findViewById(R.id.tv_order_msg_selectedlist);


            final DishesSelectedAdapter seletedAdapter = new DishesSelectedAdapter(getActivity(), R.id.lv_selected_dishes, selectedDishes, girl);
            tvTotalSelect.setText("已选择" + girl.totalSelection());
            tvTotalPrice.setText("总价：￥" + (girl.totalSelectionPrice() > 0 ? girl.totalSelectionPrice() : 0));

            lvSelectedDishes.setAdapter(seletedAdapter);

            /**
             * 当 ws 消息影响点菜结果的时候需要将结果使用线程更新列表和统计数据
             */
            runReloadTheList = new Runnable() {
                @Override
                public void run() {
                    selectedDishes.clear();
                    selectedDishes.addAll(girl.getSelectedDishList());
                    seletedAdapter.notifyDataSetChanged();
                    tvTotalSelect.setText("已选择" + girl.totalSelection());
                    tvTotalPrice.setText("总价：￥" + (girl.totalSelectionPrice() > 0 ? girl.totalSelectionPrice() : 0));
                }
            };

            /**
             * 显示提交后服务器返回的结果，等待用户最终确认，这个步骤和显示中服务员手拿确认单的给用户确认相配合
             */
            runTheSubmitResultDialog = new Runnable() {
                @Override
                public void run() {
                    resultConfirmDialog = new SubmitResultConfirmDialog(getActivity(), R.style.submitResultDialog, R.layout.submit_result_confirm_dialog_layout);
                    resultConfirmDialog.show();
                }
            };


            // 修改一下按钮的提示，改为预订
            if (!girl.isLiveOrder) {
                btnSubmmitSelected.setText("预订");
            }else{
                girl.wsMsgHandler.setInSelectedList(tvOrderMsg, handler, runnable, runReloadTheList, runTheSubmitResultDialog);
            }

            btnSubmmitSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 如果是现场点餐的话，就预提交菜单
                    if (girl.isLiveOrder) {
                        String submitMsg = "SUBMIT " + QRRestaurantApplication.accountManager.mUserId;
                        girl.wsConnection.sendTextMessage(submitMsg);
                    } else {
                        resultConfirmDialog = new SubmitResultConfirmDialog(getActivity(), R.style.submitResultDialog, R.layout.submit_result_confirm_dialog_layout);
                        resultConfirmDialog.show();
                    }
                }
            });

            return rootView;
        }

    }

}
