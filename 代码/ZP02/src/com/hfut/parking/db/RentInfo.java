package com.hfut.parking.db;

/*
 * 车位信息
 */
public class RentInfo {

	public String des;
	public String id;
	public String imgurl;
	public String lat;
	public String lon;
	public String name;
	public String rent;

	@Override
	public String toString() {
		return "ParkingData [des=" + des + ", id=" + id + ", imgurl=" + imgurl
				+ ", lat=" + lat + ", lon=" + lon + ", name=" + name
				+ ", rent=" + rent + "]";
	}

}
