package cn.com.zdez.qrrestaurant.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.layouts.DishesListItemLayout;
import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 13-12-6.
 */
public class DishesListAdapter extends ArrayAdapter<Dish> {

    private static String TAG = DishesListAdapter.class.getSimpleName();
    private Context context;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    public DishesListAdapter(Context context, int resource, List<Dish> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public void addNewSelection(int position, boolean value){
        mSelection.put(position, value);
        MyLog.d(TAG, "Before add notify");
        notifyDataSetChanged();
        MyLog.d(TAG,"After add notify");
    }

    public boolean isSelected(int position){
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public void removeSelection(int position){
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllSelection(){
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishesListItemLayout v;

        v = (DishesListItemLayout) View.inflate(context, R.layout.list_item_for_dish, null);

        v.setLayout(getItem(position));

        return v;
    }

}