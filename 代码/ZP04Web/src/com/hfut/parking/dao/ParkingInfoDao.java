package com.hfut.parking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.hfut.parking.domian.ParkingInfo;

/*
 * 封装数据库中对车位信息的操作，主要是拿到全部停车位信息并依赖于Dao层将其json数据返回供客户端解析
 */

public class ParkingInfoDao {

	public static ArrayList<ParkingInfo> getParkingInfo() {

		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;

		try {
			/***** 填写数据库相关信息(请查找数据库详情页) *****/
			String databaseName = "parkingassisant?useUnicode=true&characterEncoding=utf-8&useSSL=false";
			String host = "localhost";
			String port = "3306";
			String username = "root"; // 用户AK
			String password = "123456"; // 用户SK
			String driverName = "com.mysql.jdbc.Driver";
			String dbUrl = "jdbc:mysql://";
			String serverName = host + ":" + port + "/";
			String connName = dbUrl + serverName + databaseName;

			/****** 接着连接并选择数据库名为databaseName的服务器 ******/
			Class.forName(driverName);
			connection = DriverManager.getConnection(connName, username,
					password);
			stmt = connection.createStatement();
			/****** 至此连接已完全建立，就可对当前数据库进行相应的操作了 *****/
			/****** 接下来就可以使用其它标准mysql函数操作进行数据库操作 *****/
			// 创建一个数据库表
			System.out.println("连接成功");
			sql = "select * from parking_urban";
			ResultSet rss = stmt.executeQuery(sql);
			ArrayList<ParkingInfo> arrayList = new ArrayList<ParkingInfo>();
			if (rss != null) {
				while (rss.next()) {
					int id = rss.getInt("id");
					String imgurl = rss.getString("imgurl");
					String des = rss.getString("describtion");
					String lat = rss.getString("lat");
					String lon = rss.getString("lon");
					String name = rss.getString("name");
					int totlenumb = rss.getInt("totlenumb");
					String charge = rss.getString("charge");

					ParkingInfo parkinginfo = new ParkingInfo();
					parkinginfo.setId(id);
					parkinginfo.setDescribtion(des);
					parkinginfo.setImgurl(imgurl);
					parkinginfo.setLat(lat);
					parkinginfo.setLon(lon);
					parkinginfo.setName(name);
					parkinginfo.setTotlenumb(totlenumb);
					parkinginfo.setCharge(charge);
					arrayList.add(parkinginfo);
					//System.out.println(arrayList);
				}
				return arrayList;

			}

			// response.getOutputStream().write((execute+"").getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
