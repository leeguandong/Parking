package com.hfut.parking.db;

/*
 * 车位信息
 */
public class ParkingData {
	public String describtion;
	public int id;
	public String imgurl;
	public String lat;
	public String lon;
	public String name;
	public String totlenumb;
	public String charge;

	@Override
	public String toString() {
		return "ParkingData [describtion=" + describtion + ", id=" + id
				+ ", imgurl=" + imgurl + ", lat=" + lat + ", lon=" + lon
				+ ", name=" + name + ", totlenumb=" + totlenumb + ", charge="
				+ charge + "]";
	}

}
