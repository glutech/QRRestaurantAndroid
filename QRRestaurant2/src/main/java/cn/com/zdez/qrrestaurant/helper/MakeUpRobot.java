package cn.com.zdez.qrrestaurant.helper;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Restaurant;
import cn.com.zdez.qrrestaurant.vo.DishesVo;

/**
 * Created by LuoHanLin on 13-12-20.
 */
public class MakeUpRobot {

    public static DishesVo getDishVo(){
        DishesVo dishesVo = new DishesVo();
        dishesVo.setRest_id("2");
        dishesVo.setRest_name("Robot餐厅");
        List<Category> cats = new ArrayList<Category>();
        String[] catStrs = {"RobotLikes", "RobotHates", "RobotShits"};
        for(int i = 0; i < 3; i++){
            Category cat = new Category();
            cat.setCat_id(i);
            cat.setCat_name(catStrs[i]);
            cat.setRest_id(2);
            cats.add(cat);
        }
        dishesVo.setCatlist(cats);

        List<Dish> dishes = new ArrayList<Dish>();
        int tempId = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 10; j++){
                Dish dish = new Dish();
                dish.setRest_id(2);
                dish.setCat_id(i);
                dish.setDish_desc(catStrs[i] + "Said this is just for kidding...");
                dish.setDish_name(catStrs[i] + "的菜菜" + j);
                dish.setDish_ordered(i*j);
                dish.setDish_recommend((i+j)*j);
                dish.setDish_pic("http://werther.tk/favicon.png");
                dish.setDish_price((i+j)+(i*j)-4);
                dish.setDish_id(tempId);
                dishes.add(dish);
                tempId ++;
            }
        }
        dishesVo.setDishlist(dishes);

        return  dishesVo;
    }

    public static List<Restaurant> makeUpRestaurantList(){
        List<Restaurant> list = new ArrayList<Restaurant>();
        for(int i = 1; i < 20; i++){
            Restaurant res = new Restaurant();
            res.setRest_id(i);
            res.setRest_addr("官渡区，民航路-格林威治29号");
            res.setRest_desc("御膳房，就是专门准备皇帝、皇后食桌的厨房。分布于紫荆城内大大小小的宫院里，都有各自的膳房。设有荤局、素局、挂炉局、点心局、饭局等五局。");
            res.setRest_name("御膳房" + i + "号宫");
            res.setRest_tel("1001011");
            res.setRest_type("皇室专享");
            res.setRest_upid(1001011);
            list.add(res);
        }

        return list;
    }

}
