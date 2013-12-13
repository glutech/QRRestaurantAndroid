package cn.com.zdez.qrrestaurant.model;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class Customer {
    private long customer_id;
    private String customer_name;
    private String customer_pwd;
    private String customer_deviceid;

    public Customer() {
        customer_id = 0;
        customer_name = "0";
        customer_pwd = "0";
        customer_deviceid = "0";

    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getCutomer_name() {
        return customer_name;
    }

    public void setCutomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_pwd() {
        return customer_pwd;
    }

    public void setCustomer_pwd(String customer_pwd) {
        this.customer_pwd = customer_pwd;
    }

    public String getCustomer_deviceid() {
        return customer_deviceid;
    }

    public void setCustomer_deviceid(String customer_deviceid) {
        this.customer_deviceid = customer_deviceid;
    }
}
