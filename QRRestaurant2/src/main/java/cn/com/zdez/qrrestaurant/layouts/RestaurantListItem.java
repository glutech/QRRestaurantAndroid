package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorTreeAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.model.Restaurant;

/**
 * Created by LuoHanLin on 13-12-24.
 */
public class RestaurantListItem extends RelativeLayout {

    private Context context;
    private TextView tvName;
    private TextView tvBrief;
    private TextView tvAddress;
    private TextView tvAverage;
    private TextView tvLabel;
    private RatingBar rbRate;


    public RestaurantListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLayout(Restaurant res){
        findView();
        tvName.setText(res.getRest_name());
        tvLabel.setText(res.getRest_type());
        tvAddress.setText(res.getRest_addr());
    }

    public void findView(){
        tvName = (TextView) findViewById(R.id.tv_list_res_name);
        tvLabel = (TextView) findViewById(R.id.tv_list_res_label);
        tvAddress = (TextView) findViewById(R.id.tv_list_res_addr);
        rbRate = (RatingBar) findViewById(R.id.rb_list_rest_rate);
    }
}
