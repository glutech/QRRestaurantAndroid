package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 14-1-7.
 */
public class DishBriefItem extends RelativeLayout {

    private Context context;
    private TextView tvDishName;
    private TextView tvDishPrice;
    private TextView tvDishCount;

    public DishBriefItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLayout(Dish dish){

    }

    public void findView(){

    }
}
