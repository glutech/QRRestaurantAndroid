package cn.com.zdez.qrrestaurant.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.com.zdez.qrrestaurant.entities.Restaurant;
import cn.com.zdez.qrrestaurant.utils.DishOrderedComparator;
import cn.com.zdez.qrrestaurant.utils.DishRecommendedComparator;

/**
 * Created by LuoHanLin on 13-12-18.
 */
public class RestaurantHelper {

    private static ScanResultVo scanResultVo;
    public Map<String, ArrayList<DishVo>> catedDishMap = new HashMap<String, ArrayList<DishVo>>();
    public static RestaurantHelper instance;

    private RestaurantHelper(ScanResultVo scanResultVo) {
        this.scanResultVo = scanResultVo;
        makeCategory(); // 在生成对象的时候处理排序、分类操作
    }

    public static RestaurantHelper getInstance(ScanResultVo scanResultVo) {
        if (null == instance) {
            instance = new RestaurantHelper(scanResultVo);
        }

        return instance;
    }

    /**
     * 按类别和推荐、排行整理菜品
     */
    private void makeCategory() {
        Map<String, ArrayList<DishVo>> dishMap = new HashMap<String, ArrayList<DishVo>>();

        // 按recommended 指数生成推荐列表
        Collections.sort(scanResultVo.getDishes(), new DishRecommendedComparator());
        dishMap.put("特色推荐", scanResultVo.getDishes());
        Collections.sort(scanResultVo.getDishes(), new DishOrderedComparator());
        dishMap.put("点菜排行", scanResultVo.getDishes());


        // 按类别区分
        for (DishVo dishVo : scanResultVo.getDishes()) {
            if (!dishMap.containsKey(dishVo.getCat().getCat_name())) {
                // 没有这个类别，新建 list，put in
                ArrayList<DishVo> dishList = new ArrayList<DishVo>();
                dishList.add(dishVo);
                catedDishMap.put(dishVo.getCat().getCat_name(), dishList);
            } else {
                // 有这个类别,直接添加到这个类别里面的 list 里就 ok，因为在循环里不会重复
                catedDishMap.get(dishVo.getCat().getCat_name()).add(dishVo);
            }
        }
    }
}
