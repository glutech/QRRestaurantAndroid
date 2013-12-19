package cn.com.zdez.qrrestaurant.utils;

import java.util.Comparator;

import cn.com.zdez.qrrestaurant.vo.DishVo;

/**
 * Created by LuoHanLin on 13-12-18.
 * 按照 recommended 推荐指数排序对比类
 */
public class DishRecommendedComparator implements Comparator<DishVo> {
    @Override
    public int compare(DishVo dishVo, DishVo dishVo2) {
        return dishVo.getDish().getDish_recommend() < dishVo2.getDish().getDish_recommend() ? -1 : dishVo.getDish().getDish_recommend() == dishVo2.getDish().getDish_recommend() ? 0 : 1;
    }
}
