package com.hfut.parking.domian;

/*
 * 车位信息实体类
 */
public class ParkingInfo {

	public String describtion;
	public int id;
	public String imgurl;
	public String lat;
	public String lon;
	public String name;
	public int totlenumb;
	public String charge;
	
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getDescribtion() {
		return describtion;
	}
	public void setDescribtion(String describtion) {
		this.describtion = describtion;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTotlenumb() {
		return totlenumb;
	}
	public void setTotlenumb(int totlenumb) {
		this.totlenumb = totlenumb;
	}

	
}
