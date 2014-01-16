package cn.com.zdez.qrrestaurant.vo;


import cn.com.zdez.qrrestaurant.model.Menu;

public class HistoryVo {
    private long rest_id;
    private String rest_name;
    private Menu me;

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

    public Menu getMe() {
        return me;
    }

    public void setMe(Menu me) {
        this.me = me;
    }
}
