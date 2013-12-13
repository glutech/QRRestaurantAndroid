package cn.com.zdez.qrrestaurant.utils;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public final class Constants {

    // --- 程序中常用的常量

    public static final String PACKAGE_NAME = "cn.com.zdez.qrrestaurant";

    public static final boolean DEBUG = true;

    public static final String HELP_URL = "http://zdez.com.cn/help.html";


    // HTTP URL, Websocket url
    public static final String HOST = "192.168.1.118";
    public static final String PORT = "8080";
    public static final String WEB_APP_NAME = "qr_restaurant";
    public static final String HTTP_BASE_URL = "http://" + HOST + ":" + PORT + "/" + WEB_APP_NAME;
    public static final String WEBSOCKET_BASE_URL = "ws://" + HOST + ":" + PORT + "/" + WEB_APP_NAME;

    public static final String LOGIN_WITH_DEVICE_ID_URL = "/user/login_with_device";
    public static final String LOGIN_WITH_ACCOUNT_URL = "/user/login_with_account";
    public static final String ORDERING_MODULE_WS_URL = "/wsservlet/WSOrderWSServlet";





}
