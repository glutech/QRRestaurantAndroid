package cn.com.zdez.qrrestaurant.layouts;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.PersonalCenterActivity;
import cn.com.zdez.qrrestaurant.R;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.utils.Constants;

/**
 * 自定义对话框，显示用户初次提交菜单操作后返回（由 ws 返回，存储在 girl 中）的结果，等待用户进行最终确认
 * Created by LuoHanLin on 14-1-6.
 */
public class SubmitResultConfirmDialog extends Dialog {
    private Context context;
    private int layoutRes;
    private Button btnCancel;
    private Button btnConfirm;
    private ListView lvSubmitDishesList;
    private ProgressBar pbComfirm;
    private RestaurantWaitressGirl girl;

    public SubmitResultConfirmDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SubmitResultConfirmDialog(Context context, int layout) {
        super(context, layout);
        this.context = context;
        this.layoutRes = layout;
    }

    public SubmitResultConfirmDialog(Context context, int theme, int layoutRes) {
        super(context, theme);
        this.context = context;
        this.layoutRes = layoutRes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutRes);

        this.girl = RestaurantWaitressGirl.getInstance();

        // 开始从 girl 中取 ws 返回的结果，填充结果，并做好准备最终提交的准备
        btnCancel = (Button) findViewById(R.id.btn_cancel_in_result_dialog);
        btnConfirm = (Button) findViewById(R.id.btn_confirm_in_result_dialog);
        lvSubmitDishesList = (ListView) findViewById(R.id.lv_submit_in_dialog);
        pbComfirm = (ProgressBar) findViewById(R.id.pb_confirm);

        DishesSelectedAdapter adapter = new DishesSelectedAdapter(this.context, R.id.lv_submit_in_dialog, girl.getSubmitResultDishList(), girl);
        lvSubmitDishesList.setAdapter(adapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 用户取消这个提交要做什么？？？
                dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始向服务器发出确定信号，数据已经在服务器上，所以不需要提交菜单数据
                // TODO: 首先，开始转圈
                pbComfirm.setIndeterminate(true);
                pbComfirm.setVisibility(View.VISIBLE);


                RequestParams params = new RequestParams();
                params.put("t_id", String.valueOf(girl.serveTable));
                QRRHTTPClient.post(Constants.SUBMIT_ORDER_CONRIM, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        // TODO: 结束转圈并跳转
                        dismiss();
                        Intent orderResultIntent = new Intent();
                        orderResultIntent.setClass(context, PersonalCenterActivity.class);
                        context.startActivity(orderResultIntent);
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        super.onFailure(error, content);
                        // TODO: 提示失败信息，并做相应的实体销毁处理
                    }
                });
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
