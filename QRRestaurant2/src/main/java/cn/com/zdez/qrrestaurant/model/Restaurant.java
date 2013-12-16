package cn.com.zdez.qrrestaurant.model;

/**
 * Created by LuoHanLin on 13-12-16.
 */
public class Restaurant {
    private long rest_id;
    private String rest_name;
    private String rest_desc;
    private String rest_type;
    private String rest_addr;
    private String rest_tel;
    private long rest_upid;

    public long getRest_id() {
        return rest_id;
    }

    public void setRest_id(long rest_id) {
        this.rest_id = rest_id;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_desc() {
        return rest_desc;
    }

    public void setRest_desc(String rest_desc) {
        this.rest_desc = rest_desc;
    }

    public String getRest_type() {
        return rest_type;
    }

    public void setRest_type(String rest_type) {
        this.rest_type = rest_type;
    }

    public String getRest_addr() {
        return rest_addr;
    }

    public void setRest_addr(String rest_addr) {
        this.rest_addr = rest_addr;
    }

    public String getRest_tel() {
        return rest_tel;
    }

    public void setRest_tel(String rest_tel) {
        this.rest_tel = rest_tel;
    }

    public long getRest_upid() {
        return rest_upid;
    }

    public void setRest_upid(long rest_upid) {
        this.rest_upid = rest_upid;
    }
}
