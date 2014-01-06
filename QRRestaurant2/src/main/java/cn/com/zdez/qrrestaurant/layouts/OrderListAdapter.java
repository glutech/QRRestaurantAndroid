package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.widget.ArrayAdapter;

import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 14-1-6.
 */
public class OrderListAdapter extends ArrayAdapter<Dish>{
    private static String TAG = OrderListAdapter.class.getSimpleName();
    private Context context;


    public OrderListAdapter(Context context, int resource, Dish[] objects) {
        super(context, resource, objects);
    }
}
