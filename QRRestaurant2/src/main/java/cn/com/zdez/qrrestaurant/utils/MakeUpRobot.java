package cn.com.zdez.qrrestaurant.utils;

import java.util.ArrayList;

import cn.com.zdez.qrrestaurant.entities.Dish;

/**
 * Created by LuoHanLin on 13-12-6.
 * 仅仅用来伪造一些测试数据
 */
public class MakeUpRobot {

    private static ArrayList<Dish> dishes = new ArrayList<Dish>();

    public static ArrayList<Dish> getDishes(){
        for(int i = 0; i <= 100; i++){
            Dish d = new Dish(String.valueOf(i), "1", "Delicious Chicken Recipe"+i, "Chicken");
            dishes.add(d);
        }

        return dishes;
    }
}
