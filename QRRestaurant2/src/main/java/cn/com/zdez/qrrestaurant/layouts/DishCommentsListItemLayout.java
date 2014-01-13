package cn.com.zdez.qrrestaurant.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.model.Comment;

/**
 * Created by LuoHanLin on 14-1-9.
 */
public class DishCommentsListItemLayout extends RelativeLayout {

    private Context context;

    private TextView tvCContent;
    private TextView tvCDate;
    private RatingBar rbCRate;

    public DishCommentsListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setLayout(Comment comment){
        findView();

        tvCContent.setText(comment.getComment_content());
        tvCDate.setText(comment.getComment_date().toString());
        // TODO: set the start number as comment get after server implement this function
        rbCRate.setNumStars(5);

    }

    public void findView(){
        tvCContent = (TextView) findViewById(R.id.tv_dish_comment_content);
        tvCDate = (TextView) findViewById(R.id.tv_dish_comment_date);
        rbCRate = (RatingBar) findViewById(R.id.rb_dish_comment_rate);
    }

}
