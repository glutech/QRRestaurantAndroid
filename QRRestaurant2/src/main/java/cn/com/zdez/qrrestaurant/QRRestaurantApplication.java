package cn.com.zdez.qrrestaurant;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;

import cn.com.zdez.qrrestaurant.account.AccountManager;
import cn.com.zdez.qrrestaurant.helper.RestaurantWaitressGirl;
import cn.com.zdez.qrrestaurant.model.Customer;
import cn.com.zdez.qrrestaurant.utils.MyLog;
import cn.com.zdez.qrrestaurant.utils.QRPreferences;
import cn.com.zdez.qrrestaurant.utils.SignupType;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class QRRestaurantApplication extends Application {

    public static String TAG = QRRestaurantApplication.class.getSimpleName();
    public static final String PACKAGE_NAME = "cn.com.zdez.qrrestaurant";
    public static boolean isSignupWithAccount = false; // 是否使用用户名注册登录账户
    public static boolean isSignupWithDevice = false; // 是否使用 device id 注册登录
    public static long userId;
    public static SharedPreferences mPrefs;
    public static AccountManager accountManager;
    public static RestaurantWaitressGirl girl;

    // 服务起没有写生成 accessTocken 的逻辑，所以在客户端这里先假装模仿一个，just in case
    public static String fakeAccessTocken = "TASJDLLJGOOF9090";

    @Override
    public void onCreate() {
        super.onCreate();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        getLoginInfoFromPreference();
    }

    /**
     * 从程序配置文件中取得用户注册的一些信息
     */
    private void getLoginInfoFromPreference() {
        isSignupWithDevice = mPrefs.getBoolean(QRPreferences.P_ISSIGNUP_WITH_DEVICE, false);
        isSignupWithAccount = mPrefs.getBoolean(QRPreferences.P_ISSIGNIP_WITH_ACCOUNT, false);
    }

    /**
     * 返回程序的注册类型
     *
     * @return
     */
    public static SignupType getSignupType() {
        if (isSignupWithAccount) {
            // 优先确认是否使用用户名登录
            return SignupType.SIGNUP_WITH_ACCOUNT;
        } else if (isSignupWithAccount) {
            return SignupType.SIGNUP_WITH_DEVICE_ID;
        } else {
            return SignupType.SIGNUP_NONE;
        }
    }

    /**
     * 在用户使用device id到服务器登录/注册之后，需要将返回的用户信息存储到程序持久层中，之后使用
     *
     * @param c
     * @return
     */
    public static boolean signinLocalWithDeviceID(Customer c) {
        accountManager = new AccountManager(fakeAccessTocken, c.getCutomer_name(), c.getCustomer_id());
        return QRPreferences.saveUserInToPrefs(mPrefs, c, fakeAccessTocken);
    }

    /**
     * 取用户 id
     * @return
     */
    public static long getUserID(){
        return QRPreferences.getUserID(mPrefs);
    }

    /**
     * 打开程序经过验证后得知用户之前使用device id或者account 信息登录过，可以直接使用之前存储的用户信息程序所需的account manager 类
     */
    public static void createAccountManager() {
        accountManager = QRPreferences.getCustomerInfoFromPrefs(mPrefs);
    }


    public static void setGirl(RestaurantWaitressGirl rwg){
        girl = rwg;
    }

    public static RestaurantWaitressGirl getGirl(){
        return girl;
    }

}
