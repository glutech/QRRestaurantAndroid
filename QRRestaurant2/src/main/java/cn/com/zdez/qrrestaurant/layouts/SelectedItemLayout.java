package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.QRRestaurantApplication;
import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;

/**
 * Created by LuoHanLin on 13-12-23.
 */
public class SelectedItemLayout extends RelativeLayout {

    private RestaurantWaitressGirl theGirl;
    private Context context;
    private TextView tvName;
    private TextView tvCount;
    private TextView tvPrice;
    private ImageView btnAddOne;
    private ImageView btnDeletedOne;
    private View viewAddArea;

    public SelectedItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLayout(Dish dish, RestaurantWaitressGirl girl) {
        findView(dish.getDish_id());

        theGirl = girl;
        // render data to view
        tvName.setText(dish.getDish_name());
        tvCount.setText("x" + RestaurantWaitressGirl.selection.get(dish.getDish_id()).toString());
        tvPrice.setText(String.valueOf(dish.getDish_price()));
    }

    private void findView(final long did) {
        viewAddArea = findViewById(R.id.view_add_area);
        tvName = (TextView) findViewById(R.id.tv_dish_title);
        tvCount = (TextView) findViewById(R.id.tv_item_seleted_count);
        tvPrice = (TextView) findViewById(R.id.tv_dish_price);
        btnAddOne = (ImageView) findViewById(R.id.btn_add_one);
        btnDeletedOne = (ImageView) findViewById(R.id.btn_delete_one);


//        viewAddArea.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                btnAddOne.setVisibility(VISIBLE);
//                btnDeletedOne.setVisibility(VISIBLE);
//            }
//        });

        btnAddOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                theGirl.addNewSelection(did);
                tvCount.setText("x" + RestaurantWaitressGirl.selection.get(did).toString());
                theGirl.wsConnection.sendTextMessage("ADD " + String.valueOf(QRRestaurantApplication.accountManager.mUserId) + " " + did);
            }
        });
        btnDeletedOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(theGirl.removeSelection(did) <= 0){
                    // 点击之前剩1，减少之后就没有这个菜的选项了，
                    //TODO: 给个提示,并且直接在列表中删除，而不是显示数目为 0
                    ToastUtil.showShortToast(context, "这个菜品被取消了");
                    tvCount.setText("x0");
                }else{
                    tvCount.setText("x" + theGirl.selection.get(did).toString());
                }
                theGirl.wsConnection.sendTextMessage("DELETE " + String.valueOf(QRRestaurantApplication.accountManager.mUserId) + " " + did);

            }
        });
    }
}
