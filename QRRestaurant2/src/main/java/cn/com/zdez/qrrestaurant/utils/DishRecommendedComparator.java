package cn.com.zdez.qrrestaurant.utils;

import java.util.Comparator;

import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.vo.DishVo;

/**
 * Created by LuoHanLin on 13-12-18.
 * 按照 recommended 推荐指数排序对比类
 */
public class DishRecommendedComparator implements Comparator<Dish> {
    @Override
    public int compare(Dish dish, Dish dish2) {
        return dish.getDish_recommend() < dish2.getDish_recommend() ? 1 : dish.getDish_recommend() == dish2.getDish_recommend() ? 0 : -1;
    }
}
