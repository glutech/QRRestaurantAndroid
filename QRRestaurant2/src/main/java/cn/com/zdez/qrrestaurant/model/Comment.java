package cn.com.zdez.qrrestaurant.model;

import java.util.Date;

public class Comment {
	private long comment_id;
	private String comment_content;
	private Date comment_date;
	private long dish_id;
	private long customer_id;
	
	public long getComment_id() {
		return comment_id;
	}
	public void setComment_id(long comment_id) {
		this.comment_id = comment_id;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public Date getComment_date() {
		return comment_date;
	}
	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
	}
	public long getDish_id() {
		return dish_id;
	}
	public void setDish_id(long dish_id) {
		this.dish_id = dish_id;
	}
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}
}
