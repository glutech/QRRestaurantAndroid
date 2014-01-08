package cn.com.zdez.qrrestaurant.utils;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public final class Constants {

    // --- 程序中常用的常量

    public static final String PACKAGE_NAME = "cn.com.zdez.qrrestaurant";

    public static final boolean DEBUG = true;

    public static final String HELP_URL = "http://zdez.com.cn/help.html";

    // Zdez QRRestaurant QR prefix
    public static final String PRE_QR_URL = "http://www.zdez.cn.qrrestaurant/";


    // HTTP URL, Websocket url
    public static final String HOST = "192.168.1.103";
    public static final String PORT = "8080";
    public static final String WEB_APP_NAME = "qr_restaurant";
    public static final String HTTP_BASE_URL = "http://" + HOST + ":" + PORT + "/" + WEB_APP_NAME;
    public static final String WEBSOCKET_BASE_URL = "ws://" + HOST + ":" + PORT + "/" + WEB_APP_NAME;

    public static final String LOGIN_WITH_DEVICE_ID_URL = "/user/login_with_device";
    public static final String LOGIN_WITH_ACCOUNT_URL = "/user/login_with_account";
    public static final String ORDERING_MODULE_WS_URL = "/wsservlet/WSOrderWSServlet";
    public static final String GETRESTAURANT_LIST_ALL = "/book/get_rest_list";
    public static final String SUBMIT_ORDER_CONRIM = "/order/submit_order";
    public static final String GET_MENU_VO = "/history/get_order";

    public static final String TABLE_TAKE_POST_URL = "/tables/take"; // 废弃
    public static final String SCAN_TO_ORDER = "/order/scan";
    public static final String CHOOSE_TO_ORDER = "/book/get_dishes";
    public static final String GET_RESTAURANT_DETAIL = "/book/get_rest";
    public static final String GET_DISH_DETAIL = "/order/get_comment";

}
