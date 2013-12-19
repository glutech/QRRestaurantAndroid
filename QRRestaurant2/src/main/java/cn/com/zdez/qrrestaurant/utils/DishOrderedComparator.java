package cn.com.zdez.qrrestaurant.utils;

import java.util.Comparator;

import cn.com.zdez.qrrestaurant.model.Dish;

/**
 * Created by LuoHanLin on 13-12-18.
 * 排序对比类，按照 ordered 被点排行
 */
public class DishOrderedComparator implements Comparator<Dish> {
    @Override
    public int compare(Dish dish, Dish dish2) {
        return dish.getDish_ordered() < dish2.getDish_ordered() ? 1 : dish.getDish_ordered() == dish2.getDish_ordered() ? 0 : -1;
    }
}
