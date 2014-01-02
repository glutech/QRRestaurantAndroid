package cn.com.zdez.qrrestaurant.utils;

/**
 * Created by LuoHanLin on 13-12-27.
 */
public enum WSMsgType {
    ADD,
    DELETE,
    JOIN,
    LEAVE,
    SYNC_DATA,
    NOTSURE;

    public static WSMsgType toMsgType(String msgHead){
        try{
            return valueOf(msgHead);
        }catch (Exception e){
            return NOTSURE;
        }
    }
}


