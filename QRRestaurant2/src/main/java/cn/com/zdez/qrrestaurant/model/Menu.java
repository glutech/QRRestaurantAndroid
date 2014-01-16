package cn.com.zdez.qrrestaurant.model;

import java.sql.Timestamp;

/**
 * 本该命名为 Order，因为服务器这样命名，所以为统一逻辑，避免与服务器接驳的时候产生混乱，沿用 Menu
 * 但这个类代表用户订单，而不是餐厅订单
 */
public class Menu {
    private long menu_id;
    private int menu_status;
    private String menu_time;
    private double menu_price;
    private int menu_type;
    private long table_id;
    private long rest_id;

    public long getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(long menu_id) {
        this.menu_id = menu_id;
    }

    public int getMenu_status() {
        return menu_status;
    }

    public void setMenu_status(int menu_status) {
        this.menu_status = menu_status;
    }

    public String getMenu_time() {
        return menu_time;
    }

    public void setMenu_time(String menu_time) {
        this.menu_time = menu_time;
    }

    public double getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(double menu_price) {
        this.menu_price = menu_price;
    }

    public int getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(int menu_type) {
        this.menu_type = menu_type;
    }

    public long getTable_id() {
        return table_id;
    }

    public void setTable_id(long table_id) {
        this.table_id = table_id;
    }

    public long getRest_id() {
        return rest_id;
    }

    public void setRest_id(long rest_id) {
        this.rest_id = rest_id;
    }

}