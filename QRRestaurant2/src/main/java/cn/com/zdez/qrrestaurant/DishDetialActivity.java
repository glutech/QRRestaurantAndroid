package cn.com.zdez.qrrestaurant;

import android.content.Intent;
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
import android.os.Build;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.helper.MakeUpRobot;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.vo.DishVo;

public class DishDetialActivity extends ActionBarActivity {

    private static String TAG = DishDetialActivity.class.getSimpleName();
    private boolean mToggleIndeterminate = false;
    private static Long mDishID;
    private TextView tvAlert;
    private TextView tvLoadInfo;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_dish_detial);

        tvAlert = (TextView) findViewById(R.id.tv_dish_detail_alert);
        tvLoadInfo = (TextView) findViewById(R.id.tv_dish_detail_onloading);

        actionBar = getSupportActionBar();

        Intent intent = getIntent();
        mDishID = intent.getLongExtra("did", -1);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dish_detial, menu);
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
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is not part of the application's task, so create a new task
//                    // with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // If there are ancestor activities, they should be added here.
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    // This activity is part of the application's task, so simply
//                    // navigate up to the hierarchical parent activity.
//                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        TextView tvDishName;
        TextView tvDishPrice;
        TextView tvDishDesc;
        TextView tvDishTags;
        TextView tvDishCat;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dish_detial, container, false);
            tvDishPrice = (TextView) rootView.findViewById(R.id.tv_dish_detail_price);
            tvDishDesc = (TextView) rootView.findViewById(R.id.tv_dish_detail_desc);
            tvDishName = (TextView) rootView.findViewById(R.id.tv_dish_detail_name);
            tvDishCat = (TextView) rootView.findViewById(R.id.tv_dish_detail_cat);
            tvDishTags = (TextView) rootView.findViewById(R.id.tv_dish_detail_tags);

            Dish dish = RestaurantWaitressGirl.dishMap.get(mDishID);
            Category cat = RestaurantWaitressGirl.catMap.get(dish.getCat_id());

            tvDishName.setText(dish.getDish_name());
            tvDishDesc.setText(dish.getDish_desc());
            tvDishTags.append(dish.getDish_tag());
            tvDishPrice.setText("￥" + dish.getDish_price());
            tvDishCat.append(cat.getCat_name());

            return rootView;
        }
    }

}
