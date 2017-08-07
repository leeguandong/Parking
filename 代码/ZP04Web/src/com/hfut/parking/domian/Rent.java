package com.hfut.parking.domian;

/*
 * 出租车位实体类
 */
public class Rent {

	public String id;
	public String imgurl;
	public String name;
	public String rent;
	public String des;
	public String lon;
	public String lat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

}
