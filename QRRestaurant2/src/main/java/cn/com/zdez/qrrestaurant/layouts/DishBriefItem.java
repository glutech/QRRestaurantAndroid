package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
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
        findView();

//        int count = RestaurantWaitressGirl.getInstance().selection.get(dish.getDish_id());
        tvDishName.setText(dish.getDish_name());
//        tvDishCount.append(String.valueOf(count));
        tvDishPrice.append(String.valueOf(dish.getDish_price()));
    }

    public void findView(){
        tvDishName = (TextView) findViewById(R.id.tv_dishbrief_name);
        tvDishCount = (TextView) findViewById(R.id.tv_dishbrief_count);
        tvDishPrice = (TextView) findViewById(R.id.tv_dishbrief_price);
    }
}
