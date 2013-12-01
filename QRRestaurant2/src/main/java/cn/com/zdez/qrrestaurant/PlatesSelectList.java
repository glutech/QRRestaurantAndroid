package cn.com.zdez.qrrestaurant;

import java.util.ArrayList;

/**
 * Created by LuoHanLin on 13-12-1.
 */
public class PlatesSelectList {

    public static ArrayList<String> plateList = new ArrayList<String>();

    public static void add(String p){
        plateList.add(p);
    }

    public static int getCounter(){
        return plateList.size();
    }
}
