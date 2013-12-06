package cn.com.zdez.qrrestaurant.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.entities.Dish;
import cn.com.zdez.qrrestaurant.layouts.DishesListItemLayout;

/**
 * Created by LuoHanLin on 13-12-6.
 */
public class DishesListAdapter extends ArrayAdapter<Dish> {

    private Context context;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    public DishesListAdapter(Context context, int resource, List<Dish> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public void addNewSelection(int position, boolean value){
        mSelection.put(position, value);
        notifyDataSetChanged();
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

        v.setLayout(getItem(position), isSelected(position));

        return v;
    }

}