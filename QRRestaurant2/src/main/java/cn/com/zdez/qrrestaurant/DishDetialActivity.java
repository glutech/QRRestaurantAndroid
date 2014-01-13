package cn.com.zdez.qrrestaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.zdez.qrrestaurant.helper.MakeUpRobot;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.layouts.DishCommentsAdapter;
import cn.com.zdez.qrrestaurant.layouts.DishCommentsListItemLayout;
import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Comment;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.ConnectivityUtil;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.ListViewUtil;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;
import cn.com.zdez.qrrestaurant.vo.DishVo;

public class DishDetialActivity extends ActionBarActivity {

    private static String TAG = DishDetialActivity.class.getSimpleName();
    private boolean mToggleIndeterminate = false;
    private static Long mDishID;
    private TextView tvAlert;
    private TextView tvLoadInfo;
    ActionBar actionBar;
    private static boolean submitting = false;
    private static ProgressDialog progressDialog;
    private static boolean isCommentable = true; // 是否可以评论

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_dish_detial);

        tvAlert = (TextView) findViewById(R.id.tv_dish_detail_alert);
        tvLoadInfo = (TextView) findViewById(R.id.tv_dish_detail_onloading);

        actionBar = getSupportActionBar();

        Intent intent = getIntent();
        mDishID = intent.getLongExtra("did", -1);
        if (intent.hasExtra("menu_id")) {
            isCommentable = true;
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // 添加返回箭头
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dish_detial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                    // This activity is not part of the application's task, so create a new task
//                    // with a synthesized back stack.
//                    TaskStackBuilder.create(this)
//                            // If there are ancestor activities, they should be added here.
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    // This activity is part of the application's task, so simply
//                    // navigate up to the hierarchical parent activity.
//                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        TextView tvDishName;
        TextView tvDishPrice;
        TextView tvDishDesc;
        TextView tvDishTags;
        TextView tvDishCat;
        ListView lvDishComments;
        Button btnSubmitComment;
        RatingBar rbDishRate;
        EditText etDishCommentContent;
        ProgressBar pbRetriveComments;
        View vShowComments;
        View vComments;
        ImageView imgCommentsArrow;
        boolean toggleComments = true;
        View viewInputComment;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_dish_detial, container, false);
            tvDishPrice = (TextView) rootView.findViewById(R.id.tv_dish_detail_price);
            tvDishDesc = (TextView) rootView.findViewById(R.id.tv_dish_detail_desc);
            tvDishName = (TextView) rootView.findViewById(R.id.tv_dish_detail_name);
            tvDishCat = (TextView) rootView.findViewById(R.id.tv_dish_detail_cat);
            tvDishTags = (TextView) rootView.findViewById(R.id.tv_dish_detail_tags);
            vShowComments = rootView.findViewById(R.id.view_open_comments);
            imgCommentsArrow = (ImageView) rootView.findViewById(R.id.img_comments_arrow);
            vComments = rootView.findViewById(R.id.v_dish_detail_comments);
            lvDishComments = (ListView) rootView.findViewById(R.id.lv_dish_detail_comments);
            pbRetriveComments = (ProgressBar) rootView.findViewById(R.id.pb_retrive_comments);
            btnSubmitComment = (Button) rootView.findViewById(R.id.btn_submit_dish_comment);
            rbDishRate = (RatingBar) rootView.findViewById(R.id.rb_rate_dish);
            etDishCommentContent = (EditText) rootView.findViewById(R.id.et_dish_comment_content);
            viewInputComment = rootView.findViewById(R.id.view_comment_input);


            Dish dish = RestaurantWaitressGirl.dishMap.get(mDishID);
            Category cat = RestaurantWaitressGirl.catMap.get(dish.getCat_id());

            tvDishName.setText(dish.getDish_name());
            tvDishDesc.setText(dish.getDish_desc());
            tvDishTags.append(dish.getDish_tag());
            tvDishPrice.setText("￥" + dish.getDish_price());
            tvDishCat.append(cat.getCat_name());


            vShowComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleComments) {
                        vComments.setVisibility(View.VISIBLE);
                        imgCommentsArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                        pbRetriveComments.setIndeterminate(true);
                        pbRetriveComments.setVisibility(View.VISIBLE);
                        retriveDishComments();
                    } else {
                        vComments.setVisibility(View.GONE);
                        imgCommentsArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                    }
                    toggleComments = !toggleComments;
                }
            });

            // 如果可以评论，则显示评论窗口，设置评论提交事件
            if (isCommentable) {
                setInputComment(viewInputComment, btnSubmitComment);
            }


            return rootView;
        }

        private void retriveDishComments() {
            RequestParams params = new RequestParams();
            params.put("d_id", mDishID.toString());
            Log.d(TAG, "Start to retrive the comments for dish for did:" + mDishID);
            QRRHTTPClient.post(Constants.GET_DISH_COMMENTS, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);

                    Log.d(TAG, "Got comments:" + content);
                    Gson gson = new Gson();
                    try {
                        Comment[] comments = gson.fromJson(content, Comment[].class);
                        if (comments != null && comments.length > 0) {
                            DishCommentsAdapter adapter = new DishCommentsAdapter(getActivity(), R.id.lv_dish_detail_comments, comments);
                            lvDishComments.setAdapter(adapter);
                            ListViewUtil.setListViewHeightBasedOnChildren(lvDishComments);
                        }
                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }


                    pbRetriveComments.setIndeterminate(false);
                    pbRetriveComments.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);

                    MyLog.d(TAG, "Ok I think it's my fault:" + content);

                    pbRetriveComments.setIndeterminate(false);
                    pbRetriveComments.setVisibility(View.GONE);
                }
            });
        }

        private void setInputComment(View inputView, Button btnSubmitComment) {
            // 提交评论
            btnSubmitComment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //显示ProgressDialog
                    progressDialog = ProgressDialog.show(getActivity(), "正在提交...", "请稍候...", true, false);
                    submitting = true;

                    Comment comment = new Comment();
                    comment.setComment_content(etDishCommentContent.getText().toString());
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    comment.setComment_date(date);
                    comment.setDish_id(mDishID);
                    comment.setCustomer_id(QRRestaurantApplication.getUserID());
                    comment.setDish_rate((int) rbDishRate.getRating());


                    RequestParams params = new RequestParams();
                    MyLog.d(TAG, "Json :" + new Gson().toJson(comment));
                    params.put("comment", new Gson().toJson(comment));
                    QRRHTTPClient.post(Constants.SUBMIT_DISH_COMMENT, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);
                            if (content.equals("true")) {
                                MyLog.d(TAG, "Success submit the comment to dish...");
                                progressDialog.dismiss();
                            } else if (content.equals("false")) {
                                MyLog.d(TAG, "提交评论失败....");
                                progressDialog.dismiss();
                            }
                            isCommentable = false;
                            viewInputComment.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            super.onFailure(error, content);
                            progressDialog.dismiss();

                            ToastUtil.showShortToast(getActivity(), "服务器繁忙，请稍候再试");
                            isCommentable = false;
                            viewInputComment.setVisibility(View.GONE);
                        }
                    });

                    String content = etDishCommentContent.getText().toString();
                    float rate = rbDishRate.getRating();
                    MyLog.d(TAG, "Get the rate of :" + rate + content);
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        if (!submitting) {
            super.onBackPressed();
            return;
        } else {
            progressDialog.dismiss();
            submitting = false;
            return;
        }

    }

}
