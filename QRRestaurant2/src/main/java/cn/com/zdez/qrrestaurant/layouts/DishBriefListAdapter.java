package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 14-1-8.
 */
public class DishBriefListAdapter extends ArrayAdapter<Dish> {

    private static String TAG = DishBriefListAdapter.class.getSimpleName();
    private Context context;

    public DishBriefListAdapter(Context context, int resource, List<Dish> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishBriefItem v;

        v = (DishBriefItem) View.inflate(context, R.layout.list_item_dishbrief, null);
        v.setLayout(getItem(position));

        return v;
    }
}
