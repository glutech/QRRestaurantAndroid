package cn.com.zdez.qrrestaurant.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.utils.DishOrderedComparator;
import cn.com.zdez.qrrestaurant.utils.DishRecommendedComparator;
import cn.com.zdez.qrrestaurant.vo.DishesVo;
import cn.com.zdez.qrrestaurant.websockets.OrderMsgWSHandler;
import de.tavendo.autobahn.WebSocketConnection;

/**
 * Created by LuoHanLin on 13-12-18.
 */
public class RestaurantWaitressGirl {

    private static DishesVo dishesVo; // 从服务器中取得的数据，需进一步处理
    public Map<String, List<Dish>> catedDishMap = new HashMap<String, List<Dish>>(); // 按照类别存储每一个类别下的 dishlist
    public static RestaurantWaitressGirl instance;
    public static Map<Long, Category> catMap = new HashMap<Long, Category>(); // 按照类别 id 存储每一个类别实体
    public String[] categoryNameArray; // dishesVo 中所有的类别名称数组
    public static Map<Long, Dish> dishMap = new HashMap<Long, Dish>(); // 按照 dish id 存储每一个 dish 实体
    public static long belongRestaurant; // girl 所属餐厅
    public static long serveTable; // girl 现在服务的餐桌

    // 点菜过程中的点选记录
    public static HashMap<Long, Integer> selection = new HashMap<Long, Integer>();
    public static int totalSelection = 0;
    // 服务器返回的结果存储
    public static Map<Long, Integer> resultMap;

    // 协同点菜过程的ws 连接
    public WebSocketConnection wsConnection;
    public OrderMsgWSHandler wsMsgHandler;
    private double totalSelectionPrice;

    private RestaurantWaitressGirl(DishesVo dishesVo, Long tid) {
        this.dishesVo = dishesVo;
        belongRestaurant = Long.parseLong(dishesVo.getRest_id());
        serveTable = tid;
        mapingCategory();
        makeCategory(); // 在生成对象的时候处理排序、分类操作
        makeCategoryNameList();
    }

    public static RestaurantWaitressGirl getInstance(DishesVo dishesVo, Long tid) {
        if (null == instance) {
            instance = new RestaurantWaitressGirl(dishesVo, tid);
        }
        return instance;
    }

    public static RestaurantWaitressGirl getInstance() {
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

            // 分类的同时将 dishes 按照 dishID 存到 dishMap 中
            dishMap.put(dish.getDish_id(), dish);
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

    public void addNewSelection(Long did) {
        if (!selection.containsKey(did)) {
            selection.put(did, 1);
        } else {
            selection.put(did, selection.get(did) + 1);
        }

        totalSelection++;
        totalSelectionPrice += dishMap.get(did).getDish_price();
    }

    public static boolean isSelected(Long did) {
        if (selection.get(did) == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * /**
     *
     * @param did 菜品 id
     * @return 已选择列表中没有这个菜的话返回-1（错误）,成功减少数目并且现有数目大于0则返回当前个数，成功减少并且为0的话返回0
     */
    public int removeSelection(Long did) {
        if (selection.containsKey(did)) {
            if (selection.get(did) == 1) {
                selection.remove(did);
                totalSelection--;
                totalSelectionPrice -= dishMap.get(did).getDish_price();
                return 0;
            } else {
                selection.put(did, selection.get(did) - 1);
                totalSelection--;
                totalSelectionPrice -= dishMap.get(did).getDish_price();
                return selection.get(did);
            }
        }

        return -1;
    }

    public void clearAllSelection() {
        selection = new HashMap<Long, Integer>();
        totalSelection = 0;
        totalSelectionPrice = 0;
    }

    public int totalSelection() {
        return totalSelection;
    }

    public double totalSelectionPrice() {
        return totalSelectionPrice;
    }

    public List<Dish> getSelectedDishList() {
        List<Dish> seletedDishList = new ArrayList<Dish>();

        Iterator iter2 = selection.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry<Long, Integer> entry = (Map.Entry<Long, Integer>) iter2.next();

            seletedDishList.add(dishMap.get(entry.getKey()));
        }
        return seletedDishList;
    }

    /**
     * 获取服务器提交后返回的菜单详情
     *
     * @return
     */
    public List<Dish> getSubmitResultDishList() {
        if (!isSelectionCorrect(resultMap)) {
            // TODO: 如果不一样的话要做点什么？？？
            // TODO: 提醒用户，但依旧使用服务器上的值，或者用户选则重新来一遍或者就依服务器的结果为准
        }
        return getSelectedDishList();
    }


    public void setSubmitResultDishesList(Map<Long, Integer> result) {
        resultMap = new HashMap<Long, Integer>(result);
    }

    /**
     * 比较服务器返回的订单结果与本地的是否一致，对比两个 Map
     *
     * @param resultMap
     * @return
     */
    private boolean isSelectionCorrect(Map<Long, Integer> resultMap) {
        if (selection.size() != resultMap.size()) {
            return false;
        }

        Map<Long, Integer> clientCopyMap = new HashMap<Long, Integer>(selection);

        Iterator serverSideIt = resultMap.entrySet().iterator();

        while (serverSideIt.hasNext()) {
            Map.Entry<Long, Integer> en = (Map.Entry<Long, Integer>) serverSideIt.next();
            if (selection.get(en.getKey()) == null || selection.get(en.getKey()) != en.getValue()) {
                // 本地 Map 不含服务器含有的这个菜
                return false;
            } else {
                // 这个菜相同
                clientCopyMap.remove(en);
            }
        }

        // 相同的都移除，如果一致的话 copy 中应该为空，否则还是不一样
        if (clientCopyMap.size() != 0) {
            return false;
        }

        return true;
    }

}
