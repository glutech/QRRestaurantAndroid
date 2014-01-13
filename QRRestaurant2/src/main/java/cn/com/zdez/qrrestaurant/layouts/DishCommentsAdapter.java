package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.model.Comment;

/**
 * Created by LuoHanLin on 14-1-9.
 */
public class DishCommentsAdapter extends ArrayAdapter<Comment> {

    private static String TAG = DishCommentsAdapter.class.getSimpleName();
    private Context context;

    public DishCommentsAdapter(Context context, int resource, Comment[] objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishCommentsListItemLayout v;

        v = (DishCommentsListItemLayout) View.inflate(context, R.layout.list_item_dish_comment, null);
        v.setLayout(getItem(position));

        return v;
    }
}
