package cn.com.zdez.qrrestaurant.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cn.com.zdez.qrrestaurant.utils.Constants;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class QRRHTTPClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String absUrl = Constants.HTTP_BASE_URL + relativeUrl;
        client.get(absUrl, params, responseHandler);
    }

    public static void post(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler){
        String absUrl = Constants.HTTP_BASE_URL + relativeUrl;
        client.post(absUrl, params, responseHandler);
    }

}
