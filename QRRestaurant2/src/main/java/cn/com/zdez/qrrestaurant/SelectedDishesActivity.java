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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.layouts.DishesSelectedAdapter;
import cn.com.zdez.qrrestaurant.model.Dish;

public class SelectedDishesActivity extends ActionBarActivity {

    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_dishes);

        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.title_activity_selected_dishes) + ":" + RestaurantWaitressGirl.getSelectedDishList().size());}


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
        private TextView tvSelectedCount;
        private TextView tvTotalPrivce;
        private Button btnSubmmitSelected;
        private Button btnContinueAdd;
        private List<Dish> selectedDishes;

        public PlaceholderFragment() {
            selectedDishes = RestaurantWaitressGirl.getSelectedDishList();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_selected_dishes, container, false);
            lvSelectedDishes = (ListView) rootView.findViewById(R.id.lv_selected_dishes);
            tvSelectedCount = (TextView) rootView.findViewById(R.id.tv_item_seleted_count);
            tvTotalPrivce = (TextView) rootView.findViewById(R.id.tv_selected_total_price);
            btnContinueAdd = (Button) rootView.findViewById(R.id.btn_continue_add);
            btnSubmmitSelected = (Button) rootView.findViewById(R.id.btn_selected_submit);

            DishesSelectedAdapter seletedAdapter = new DishesSelectedAdapter(getActivity(), R.id.lv_selected_dishes, selectedDishes);

            lvSelectedDishes.setAdapter(seletedAdapter);

            return rootView;
        }
    }

}
