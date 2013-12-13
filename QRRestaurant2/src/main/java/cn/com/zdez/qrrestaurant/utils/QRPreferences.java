package cn.com.zdez.qrrestaurant.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.com.zdez.qrrestaurant.account.AccountManager;
import cn.com.zdez.qrrestaurant.model.Customer;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class QRPreferences {

    public Context context;
    public static final String TAG = QRPreferences.class.getSimpleName();
    public static final String P_CURRENT_VERSION = "cv";
    public static final String P_CURRENT_VERSION_NAME = "cvname";
    public static final String P_FIRST_LAUNCHE = "fltime";
    public static final String P_ISSIGNUP_WITH_DEVICE = "isswd";
    public static final String P_ISSIGNIP_WITH_ACCOUNT = "isswa";
    public static final String P_CUSTOMER_NAME = "customer_name";
    public static final String P_CUSTOMER_ID = "customer_id";
    public static final String P_CUSTOMER_PSW = "customer_psw";
    public static final String P_ACCESS_TOCKEN = "access_tocken";


    public static void setPreferenceDefaults(SharedPreferences prefs, Editor editor) {
        // 设置初始值
        if (!prefs.contains(P_FIRST_LAUNCHE)) {

        } else {
            MyLog.d(TAG, "Already launched before...");
        }
    }

    public static boolean saveUserInToPrefs(SharedPreferences prefs, Customer c, String accessTocken){
        Editor editor = prefs.edit();
        PreferencesSpec.setPreference(prefs, editor, P_CURRENT_VERSION_NAME, c.getCutomer_name());
        PreferencesSpec.setPreference(prefs, editor, P_CUSTOMER_ID, String.valueOf(c.getCustomer_id()));
        PreferencesSpec.setPreference(prefs, editor, P_CUSTOMER_PSW, c.getCustomer_pwd());
        PreferencesSpec.setPreference(prefs, editor, P_ISSIGNUP_WITH_DEVICE, true);
        if(editor.commit()){
            return true;
        }else{
            return false;
        }
    }

    public static AccountManager getCustomerInfoFromPrefs(SharedPreferences mPrefs) {
        String userName = mPrefs.getString(P_CUSTOMER_NAME, "暂时没有用户名");
        String accessTocken = mPrefs.getString(P_ACCESS_TOCKEN, "blank access tocken");
        String userIdStr = mPrefs.getString(P_CUSTOMER_ID, "blank user id");
        long userId = -1;
        try{
            userId = Long.parseLong(userIdStr);
        }catch (Exception e){
            MyLog.e(TAG, "Got a user id that can't parse to long...");
            e.printStackTrace();
        }

        return new AccountManager(accessTocken,userName, userId);
    }
}
