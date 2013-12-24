package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import java.util.List;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.model.Restaurant;

/**
 * Created by LuoHanLin on 13-12-24.
 */
public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

    private Context context;

    public RestaurantListAdapter(Context context, int resource, List<Restaurant> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        RestaurantListItem v;

        v = (RestaurantListItem) View.inflate(context, R.layout.list_item_restaurant, null);
        v.setLayout(getItem(position));

        return v;
    }
}
