package cn.com.zdez.qrrestaurant.model;

/**
 * Created by LuoHanLin on 13-12-12.
 */
public class Table {
    private long table_id;
    private String table_name;
    private String table_type;
    private int table_status;
    private long rest_id;

    public long getTable_id() {
        return table_id;
    }

    public void setTable_id(long table_id) {
        this.table_id = table_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_type() {
        return table_type;
    }

    public void setTable_type(String table_type) {
        this.table_type = table_type;
    }

    public int getTable_status() {
        return table_status;
    }

    public void setTable_status(int table_status) {
        this.table_status = table_status;
    }

    public long getRest_id() {
        return rest_id;
    }

    public void setRest_id(long rest_id) {
        this.rest_id = rest_id;
    }

}
