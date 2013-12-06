package cn.com.zdez.qrrestaurant.entities;

import java.util.ArrayList;

/**
 * Created by LuoHanLin on 13-12-1.
 */
public class DishesSelectList {

    public static ArrayList<String> plateList = new ArrayList<String>();

    public static void add(String p){
        plateList.add(p);
    }

    public static int getCounter(){
        return plateList.size();
    }

    public static void remove(String d){
        if(plateList.contains(d)){
            plateList.remove(d);
        }
    }
}
