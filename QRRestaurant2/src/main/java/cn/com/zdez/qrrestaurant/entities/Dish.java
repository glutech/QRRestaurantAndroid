package cn.com.zdez.qrrestaurant.entities;

/**
 * Created by LuoHanLin on 13-12-2.
 * 菜品实体
 * 菜品ID，所属餐厅 ID，名称，描述，评分，图片地址（包括存储/缓存地址和网络地址）
 * 菜品所属类别名称，菜品标签
 */
public class Dish {

    private String dID;
    private String dName;
    private String rID;
    private String dDescription;
    private int dRate;
    private String dCoverPath;
    private String dCateName;
    private String[] dTags;
    private boolean isSelectedInList = false; // Only used in layout render

    public Dish(String dID, String rID, String dName, String dCateName) {
        this.dID = dID;
        this.rID = rID;
        this.dName = dName;
        this.dCateName = dCateName;
    }

    public String getdID() {
        return dID;
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public String getdDescription() {
        return dDescription;
    }

    public void setdDescription(String dDescription) {
        this.dDescription = dDescription;
    }

    public int getdRate() {
        return dRate;
    }

    public void setdRate(int dRate) {
        this.dRate = dRate;
    }

    public String getdCoverPath() {
        return dCoverPath;
    }

    public void setdCoverPath(String dCoverPath) {
        this.dCoverPath = dCoverPath;
    }

    public String getdCateName() {
        return dCateName;
    }

    public void setdCateName(String dCateName) {
        this.dCateName = dCateName;
    }

    public String[] getdTags() {
        return dTags;
    }

    public void setdTags(String[] dTags) {
        this.dTags = dTags;
    }


    public boolean isSelectedInList() {
        return isSelectedInList;
    }

    public void setSelectedInList(boolean isSelectedInList) {
        this.isSelectedInList = isSelectedInList;
    }

}
