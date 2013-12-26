package cn.com.zdez.qrrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.http.QRRHTTPClient;
import cn.com.zdez.qrrestaurant.model.Customer;
import cn.com.zdez.qrrestaurant.utils.Constants;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.ToastUtil;

public class WelcomeActivity extends ActionBarActivity {
    private static String TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getSupportActionBar().hide();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        // 下面的就是延时和耗时的操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 程序初始化操作，检查用户登录信息，device_id 登录或者是 userName 登录
                // 如果已经登录过的话，直接跳转，如果没有登录过的话，开启线程上传device_id
                switch (QRRestaurantApplication.getSignupType()) {
                    case SIGNUP_NONE:
                        // 没有任何注册信息，后台使用  device_id 注册
                        loginWithDeviceId();
                        break;
                    case SIGNUP_WITH_DEVICE_ID:
                        // 已经使用 device_id 注册过，则可以开始正常使用
                        MyLog.d(TAG, "已经使用 device id 注册过。。。");
                        QRRestaurantApplication.createAccountManager();
                        break;
                    case SIGNUP_WITH_ACCOUNT:
                        // 用户使用自己的信息注册过，可以使用一些高级的功能
                        MyLog.d(TAG, "已经使用 用户名 注册过。。。");
                        QRRestaurantApplication.createAccountManager();
                        break;
                    default:
                        // 意外的选项，如同 SIGN_UP_NONE 操作
                        loginWithDeviceId();
                        break;
                }

                Intent toMainActivity = new Intent();
                toMainActivity.setClass(WelcomeActivity.this,
                        QRRMainActivity.class);
                startActivity(toMainActivity);
                finish();

            }
        }, 3000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            return rootView;
        }
    }

    private void loginWithDeviceId() {
        String android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        RequestParams params = new RequestParams();
        params.put("device_id", android_id);

        QRRHTTPClient.post(Constants.LOGIN_WITH_DEVICE_ID_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(String content) {
                Gson gson = new Gson();
                Customer c = null;
                MyLog.d(TAG, "Got the response for login with deviceid: " + content);
                try{
                    c = gson.fromJson(content, Customer.class);
                }catch (com.google.gson.JsonSyntaxException e){
                    e.printStackTrace();
                }

                if(null != c){
                    // 成功使用 device id 在服务器登录该程序, 返回成功的用户信息之后，开始将这些用户信息注册（保存）在本地
                    // TODO: 应该在返回 customer 的时候返回程序的 accessTocken,然后在客户端记住，之后请求就可以使用
                    if(QRRestaurantApplication.signinLocalWithDeviceID(c)){
                        // 使用 device id 登录完全结束
                        Intent toMainActivity = new Intent();
                        toMainActivity.setClass(WelcomeActivity.this, QRRMainActivity.class);
                        startActivity(toMainActivity);
                    }else{
                        // 在本地保存用户信息的时候出错
                        ToastUtil.showShortToast(WelcomeActivity.this, "网络连接错误，程序不可用");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                MyLog.d(TAG, "Failure at signup with device id, the statuscode: " + String.valueOf(statusCode) + " and the content: " + content);
            }
        });
    }

}
