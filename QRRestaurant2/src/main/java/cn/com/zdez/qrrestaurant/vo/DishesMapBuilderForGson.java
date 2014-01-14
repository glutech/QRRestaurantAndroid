package cn.com.zdez.qrrestaurant.vo;

import java.util.Map;

/**
 * 没有特别的用处，仅仅是用来与服务器统一 Gson 的序列化格式
 */
public class DishesMapBuilderForGson {

	Map<Long, Integer> map;

	public Map<Long, Integer> getMap() {
		return map;
	}

	public void setMap(Map<Long, Integer> map) {
		this.map = map;
	}
	
}
