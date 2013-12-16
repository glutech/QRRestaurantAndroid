package cn.com.zdez.qrrestaurant.model;

public class Category {
	private long cat_id;
	private String cat_name;
	private long rest_id;
	
	public long getCat_id() {
		return cat_id;
	}
	public void setCat_id(long cat_id) {
		this.cat_id = cat_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public long getRest_id() {
		return rest_id;
	}
	public void setRest_id(long rest_id) {
		this.cat_id = rest_id;
	}
}
