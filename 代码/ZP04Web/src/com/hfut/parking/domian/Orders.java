package com.hfut.parking.domian;

/**
 * 车位订单表实体类
 */
public class Orders {
	// private int id;// 订单ID
	private int number;// 预定数量，通常都为1
	private String createtime;// 预定的时间
	private int state;// 预定的状态
	private String username;// 预订者姓名
	private String usertel;// 预订者联系方式
	private int parkingid;// 车位ID
	private String parkingname;// 预定的车位名称

	public String getParkingname() {
		return parkingname;
	}

	public void setParkingname(String parkingname) {
		this.parkingname = parkingname;
	}

	@Override
	public String toString() {
		return "Orders [number=" + number + ", createtime=" + createtime
				+ ", state=" + state + ", username=" + username + ", usertel="
				+ usertel + ", parkingid=" + parkingid + ", parkingname="
				+ parkingname + "]";
	}

	/*
	 * public int getId() { return id; }
	 * 
	 * public void setId(int id) { this.id = id; }
	 */
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsertel() {
		return usertel;
	}

	public void setUsertel(String usertel) {
		this.usertel = usertel;
	}

	public int getParkingid() {
		return parkingid;
	}

	public void setParkingid(int parkingid) {
		this.parkingid = parkingid;
	}
}
