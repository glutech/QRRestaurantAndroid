package cn.com.zdez.qrrestaurant.entities;

/**
 * Created by LuoHanLin on 13-12-2.
 * 菜品实体
 * 菜品ID，所属餐厅 ID，名称，描述，评分，图片地址（包括存储/缓存地址和网络地址）
 * 菜品所属类别名称，菜品标签
 */
public class Plate {

    private String pID;
    private String pName;
    private String rID;
    private String pDescription;
    private String pRate;
    private String pCoverPath;
    private String pCateName;
    private String[] pTags;

    public Plate(String pID, String rID, String pName, String pCateName) {
        this.pID = pID;
        this.rID = rID;
        this.pName = pName;
        this.pCateName = pCateName;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getrID() {
        return rID;
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpRate() {
        return pRate;
    }

    public void setpRate(String pRate) {
        this.pRate = pRate;
    }

    public String getpCoverPath() {
        return pCoverPath;
    }

    public void setpCoverPath(String pCoverPath) {
        this.pCoverPath = pCoverPath;
    }

    public String getpCateName() {
        return pCateName;
    }

    public void setpCateName(String pCateName) {
        this.pCateName = pCateName;
    }

    public String[] getpTags() {
        return pTags;
    }

    public void setpTags(String[] pTags) {
        this.pTags = pTags;
    }

}
