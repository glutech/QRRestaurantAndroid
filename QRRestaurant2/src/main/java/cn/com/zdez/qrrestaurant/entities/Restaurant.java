package cn.com.zdez.qrrestaurant.entities;

/**
 * Created by LuoHanLin on 13-12-2.
 * 餐馆实体
 * 餐馆名称，餐馆地址，餐馆评分
 */
public class Restaurant {

    private String rId;
    private String rName;
    private int rRate;

    public Restaurant(String rName) {
        this.rName = rName;
        this.rRate = 4;
    }

    public Restaurant(String rId, String rName) {
        this.rId = rId;
        this.rName = rName;
    }

    public Restaurant() {
        this.rName = "暂时没有填写餐馆名称";
        this.rRate = 4;
    }

    public String getrName() {
        return rName;
    }

    public int getrRate() {
        return rRate;
    }

    public void rate(int rate) {
        this.rRate = rate;
    }

    public void resetRName(String name) {
        this.rName = name;
    }
}
