package com.hfut.parking.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParkingInfo implements Serializable {
	private static final long serialVersionUID = -4263635775790063504L;
	private String des;
	private String id;
	private String imgurl;
	private String lat;
	private String lon;
	private String name;
	private String totle;

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTotle() {
		return totle;
	}

	public void setTotle(String totle) {
		this.totle = totle;
	}




	/***
	 * 用来存储服务器端请求的数据集合
	 */
	public static List<ParkingInfo> infos = new ArrayList<ParkingInfo>();

	public ParkingInfo(String des, String id, String imgurl, String lat,
			String lon, String name, String totle) {
		super();
		this.des = des;
		this.id = id;
		this.imgurl = imgurl;
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.totle = totle;
	}
}