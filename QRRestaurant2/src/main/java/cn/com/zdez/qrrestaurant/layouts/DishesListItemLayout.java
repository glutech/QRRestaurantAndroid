package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import cn.com.zdez.qrrestaurant.DishDetialActivity;
import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.utils.MyLog;

/**
 * Created by LuoHanLin on 13-12-5.
 */
public class DishesListItemLayout extends RelativeLayout {

    private Context context;
    private TextView tvTitle;
    private TextView tvBrief;
    private TextView tvExtra;
    private SmartImageView imgCover;
    private ImageView imgRate;
    private static String TAG = DishesListItemLayout.class.getSimpleName();

    public DishesListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLayout(Dish dish) {
        findView();
        tvTitle.setText(dish.getDish_name());
        // Set background for display the selected dishes

        if (RestaurantWaitressGirl.isSelected(dish.getDish_id())) {
            MyLog.d(TAG, dish.getDish_name() + " got selection:" + RestaurantWaitressGirl.isSelected(dish.getDish_id()));
            setBackgroundColor(getResources().getColor(R.color.blue_light_t));
        }
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_dish_title);
        imgCover = (SmartImageView) findViewById(R.id.img_dish_cover);
        imgCover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toDishDetail = new Intent();
                toDishDetail.setClass(context, DishDetialActivity.class);
                context.startActivity(toDishDetail);
            }
        });
    }

}
