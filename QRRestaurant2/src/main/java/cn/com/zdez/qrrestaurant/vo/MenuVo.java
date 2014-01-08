package cn.com.zdez.qrrestaurant.vo;

import java.util.ArrayList;
import java.util.List;

import cn.com.zdez.qrrestaurant.model.Dish;
import cn.com.zdez.qrrestaurant.model.Menu;

/**
 * 本该命名为 OrderVo，但服务器这样命名的话，为统一逻辑，避免与服务器接驳的时候产生混乱
 * 但这个类代表用户订单，而不是餐厅订单
 */
public class MenuVo {
	Menu menu;
	ArrayList<Dish> dishes;
	
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public ArrayList<Dish> getDishes() {
		return dishes;
	}
	public void setDishes(List<Dish> dishlist) {
		this.dishes = (ArrayList<Dish>) dishlist;
	}
}
