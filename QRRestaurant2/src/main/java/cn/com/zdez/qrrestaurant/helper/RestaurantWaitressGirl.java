package cn.com.zdez.qrrestaurant.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.utils.DishOrderedComparator;
import cn.com.zdez.qrrestaurant.utils.DishRecommendedComparator;
import cn.com.zdez.qrrestaurant.vo.DishesVo;

/**
 * Created by LuoHanLin on 13-12-18.
 */
public class RestaurantWaitressGirl {

    private static DishesVo dishesVo;
    public Map<String, List<Dish>> catedDishMap = new HashMap<String, List<Dish>>();
    public static RestaurantWaitressGirl instance;
    public Map<Long, Category> catMap = new HashMap<Long, Category>();
    public String[] categoryNameArray;

    // 点菜过程中的点选记录
    public static HashMap<Long, Integer> selection = new HashMap<Long, Integer>();
    public static int totalSelection = 0;

    private RestaurantWaitressGirl(DishesVo dishesVo) {
        this.dishesVo = dishesVo;
        mapingCategory();
        makeCategory(); // 在生成对象的时候处理排序、分类操作
        makeCategoryNameList();
    }


    public static RestaurantWaitressGirl getInstance(DishesVo dishesVo) {
        if (null == instance) {
            instance = new RestaurantWaitressGirl(dishesVo);
        }
        return instance;
    }

    /**
     * 按类别和推荐、排行整理菜品,map索引是类别名称，索引到的值是 dish 的 list
     */
    private void makeCategory() {
        // 按recommended 指数生成推荐列表
        List<Dish> recommendList = new ArrayList<Dish>();
        List<Dish> orderedList = new ArrayList<Dish>();

        recommendList.addAll(dishesVo.getDishlist());
        orderedList.addAll(dishesVo.getDishlist());

        Collections.sort(recommendList, new DishRecommendedComparator());
        catedDishMap.put("特色推荐", recommendList);
        Collections.sort(orderedList, new DishOrderedComparator());
        catedDishMap.put("点菜排行", orderedList);


        // 按类别区分
        for (Dish dish : dishesVo.getDishlist()) {
            if (!catedDishMap.containsKey(catMap.get(dish.getCat_id()).getCat_name())) {
                // 没有这个类别，新建 list，put in
                List<Dish> dishList = new ArrayList<Dish>();
                dishList.add(dish);
                catedDishMap.put(catMap.get(dish.getCat_id()).getCat_name(), dishList);
            } else {
                // 有这个类别,直接添加到这个类别里面的 list 里就 ok，因为在循环里不会重复
                catedDishMap.get(catMap.get(dish.getCat_id()).getCat_name()).add(dish);
            }
        }
    }

    /**
     * catedDishMap 中的 cateName 是无序的，为便于顺序显示，生成专用的分类名称数组
     */
    private void makeCategoryNameList() {
        categoryNameArray = new String[catMap.size() + 2]; // 两个动态生成的分类
        categoryNameArray[0] = "特色推荐";
        categoryNameArray[1] = "点菜排行";
        Iterator iter = catMap.entrySet().iterator();
        int i = 2;
        while (iter.hasNext()) {
            Map.Entry<Long, Category> entry = (Map.Entry<Long, Category>) iter.next();
            categoryNameArray[i] = entry.getValue().getCat_name();
            i++;
        }
    }

    /**
     * 因为服务返回的 catelist 是没有索引的 list，不便于查找搜索，所以需要使用 map 创建索引
     */
    private void mapingCategory() {
        for (Category cat : dishesVo.getCatlist()) {
            if (!catMap.containsKey(cat.getCat_id())) {
                catMap.put(cat.getCat_id(), cat);
            }
        }
    }

    public static void addNewSelection(Long did) {
        if(!selection.containsKey(did)){
            selection.put(did, 1);
        }else{
            selection.put(did, selection.get(did) + 1);
        }

        totalSelection++;
    }

    public static boolean isSelected(Long did) {
        if (selection.get(did) == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 减去菜品，有一个的话删除这个菜，返回0；有多个的话，减一，返回剩下的数目
     * 本来就没有的话，返回-1
     * @param did
     */
    public static int removeSelection(Long did) {
        if (selection.containsKey(did)) {
            if(selection.get(did) == 1){
                selection.remove(did);
                totalSelection--;
                return 0;
            }else{
                selection.put(did, selection.get(did) - 1);
                totalSelection--;
                return selection.get(did);
            }
        }

        return -1;
    }

    public static void clearAllSelection() {
        selection = new HashMap<Long, Integer>();
        totalSelection = 0;
    }

    public static int totalSelection(){
        return totalSelection;
    }
}
