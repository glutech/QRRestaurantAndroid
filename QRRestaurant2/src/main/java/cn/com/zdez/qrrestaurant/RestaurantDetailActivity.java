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
import android.widget.Button;

import cn.com.zdez.qrrestaurant.utils.ToastUtil;

public class RestaurantDetailActivity extends ActionBarActivity {

    private static String mTableId;
    private static String mRestId;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        // Set up the action bar.
        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);

    }


    public void loadRestaurantInfo(){
        Intent intent = getIntent();
        if(intent.hasExtra("tid")){
            ToastUtil.showShortToast(getApplicationContext(), "Got the table id: " + mTableId);
        }else if(intent.hasExtra("rid")){

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Button btnStartOrder;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
            btnStartOrder = (Button) rootView.findViewById(R.id.btn_start_order);
            Button btnTemp = (Button)rootView.findViewById(R.id.btn_temp);
            btnTemp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toDishDetail = new Intent();
                    toDishDetail.putExtra("tid", "2");
                    toDishDetail.setClass(getActivity(), DishDetialActivity.class);
                    startActivity(toDishDetail);
                }
            });

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
