package cn.com.zdez.qrrestaurant.account;

/**
 * Created by LuoHanLin on 13-12-13.
 * 用于管理用户与服务器之间的验证信息，授权操作等等
 */
public class AccountManager {
    //
    private String mAccessTocken = "";
    public String mUserName = "";
    public long mUserId;

    public AccountManager(String mAccessTocken, String mUserName, long mUserId){
        this.mAccessTocken = mAccessTocken;
        this.mUserName = mUserName;
        this.mUserId = mUserId;
    }



}
