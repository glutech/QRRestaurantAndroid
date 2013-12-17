package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.entities.Dish;
import cn.com.zdez.qrrestaurant.vo.DishVo;

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

    public void setLayout(DishVo dish, boolean isSelected) {
        findView();
        tvTitle.setText(dish.getDish().getDish_name());
        // Set background for display the selected dishes

        if(isSelected){
            setBackgroundColor(getResources().getColor(R.color.blue_light_t));
        }
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_dish_title);
        imgCover = (SmartImageView) findViewById(R.id.img_dish_cover);
    }

}
