package cn.com.zdez.qrrestaurant.helper;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;
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
}
