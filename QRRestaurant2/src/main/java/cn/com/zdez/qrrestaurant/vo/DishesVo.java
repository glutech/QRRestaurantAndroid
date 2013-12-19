package cn.com.zdez.qrrestaurant.vo;

import java.util.List;

import cn.com.zdez.qrrestaurant.model.Category;
import cn.com.zdez.qrrestaurant.model.Dish;

public class DishesVo {
	String rest_id;
	String rest_name;
	List<Dish> dishlist;
	List<Category> catlist;
	
	public String getRest_id() {
		return rest_id;
	}
	public void setRest_id(String rest_id) {
		this.rest_id = rest_id;
	}
	public String getRest_name() {
		return rest_name;
	}
	public void setRest_name(String rest_name) {
		this.rest_name = rest_name;
	}
	public List<Dish> getDishlist() {
		return dishlist;
	}
	public void setDishlist(List<Dish> dishlist) {
		this.dishlist = dishlist;
	}
	public List<Category> getCatlist() {
		return catlist;
	}
	public void setCatlist(List<Category> catlist) {
		this.catlist = catlist;
	}
}
