package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 13-12-23.
 */
public class DishesSelectedAdapter extends ArrayAdapter<Dish> {

    private static String TAG = DishesSelectedAdapter.class.getSimpleName();
    private Context context;
    private RestaurantWaitressGirl girl;

    public DishesSelectedAdapter(Context context, int resource, RestaurantWaitressGirl girl) {
        super(context, resource, girl.getSelectedDishList());
        this.context = context;
        this.girl = girl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectedItemLayout v;

        v = (SelectedItemLayout) View.inflate(context, R.layout.list_item_for_selected, null);

        v.setLayout(getItem(position), girl);

        return v;
    }
}
