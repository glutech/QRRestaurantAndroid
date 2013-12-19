package cn.com.zdez.qrrestaurant.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Restaurant;

/**
 * Created by LuoHanLin on 13-12-16.
 */
public class ScanResultVo {
    Restaurant rest;
    ArrayList<DishVo> dishes;

    public Restaurant getRest() {
        return rest;
    }

    public void setRest(Restaurant rest) {
        this.rest = rest;
    }

    public ArrayList<DishVo> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<DishVo> dishes) {
        this.dishes = dishes;
    }

}
