package cn.com.zdez.qrrestaurant.utils;

import java.util.Comparator;

import cn.com.zdez.qrrestaurant.vo.DishVo;

/**
 * Created by LuoHanLin on 13-12-18.
 * 排序对比类，按照 ordered 被点排行
 */
public class DishOrderedComparator implements Comparator<DishVo> {
    @Override
    public int compare(DishVo dishVo, DishVo dishVo2) {
        return dishVo.getDish().getDish_ordered() < dishVo2.getDish().getDish_ordered() ? -1 : dishVo.getDish().getDish_ordered() == dishVo2.getDish().getDish_ordered() ? 0 : -1;
    }
}
