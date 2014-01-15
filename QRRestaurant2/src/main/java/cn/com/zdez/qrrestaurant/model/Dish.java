package cn.com.zdez.qrrestaurant.model;

/**
 * Created by LuoHanLin on 13-12-12.
 */
public class Dish {
    private long dish_id;
    private String dish_name;
    private String dish_desc;
    private String dish_pic;
    private double dish_price;
    private String dish_tag;
    private String dish_status;
    private int dish_recommend;
    private int dish_ordered;
    private long cat_id;
    private long rest_id;
    private int count; // FIXME: 特殊属性，只有在处理订单的时候用到，用于菜品计数

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getDish_id() {
        return dish_id;
    }

    public void setDish_id(long dish_id) {
        this.dish_id = dish_id;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public String getDish_desc() {
        return dish_desc;
    }

    public void setDish_desc(String dish_desc) {
        this.dish_desc = dish_desc;
    }

    public String getDish_pic() {
        return dish_pic;
    }

    public void setDish_pic(String dish_pic) {
        this.dish_pic = dish_pic;
    }

    public double getDish_price() {
        return dish_price;
    }

    public void setDish_price(double dish_price) {
        this.dish_price = dish_price;
    }

    public String getDish_tag() {
        return dish_tag;
    }

    public void setDish_tag(String dish_tag) {
        this.dish_tag = dish_tag;
    }

    public String getDish_status() {
        return dish_status;
    }

    public void setDish_status(String dish_status) {
        this.dish_status = dish_status;
    }

    public int getDish_recommend() {
        return dish_recommend;
    }

    public void setDish_recommend(int dish_recommend) {
        this.dish_recommend = dish_recommend;
    }

    public int getDish_ordered() {
        return dish_ordered;
    }

    public void setDish_ordered(int dish_ordered) {
        this.dish_ordered = dish_ordered;
    }

    public long getCat_id() {
        return cat_id;
    }

    public void setCat_id(long cat_id) {
        this.cat_id = cat_id;
    }

    public long getRest_id() {
        return rest_id;
    }

    public void setRest_id(long rest_id) {
        this.rest_id = rest_id;
    }
}
