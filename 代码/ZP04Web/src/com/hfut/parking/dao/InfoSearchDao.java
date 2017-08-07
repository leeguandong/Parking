package com.hfut.parking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.hfut.parking.domian.ParkingInfo;
/*
 * 查询的数据库连接操作，这里的查询并没有用数组去接查询后得到的结果集，而是得到json数据后，去拿元数据的列数(元数据就是描述数据的数据，所以可以通过它去拿列数)
 * 拿到列数之后在每个数据后面加一个*，然后在servlet中把这个result给返回了，到了客户端之后，去解析这个json数据，json数据解析很宽泛，就是每逢一个*即用一个
 * 回车代替，这样的话，每7条信息，都分成一个tablerow，对全部查询结果进行除以7操作，得到一个recrod，在向每个里面填充数据，这个查询方法不好，要改进，用的简单适配器
 */
public class InfoSearchDao {

	private String url = "jdbc:mysql://localhost:3306/parkingassisant?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	private String user = "root";
	private String psd = "123456";
	private Connection conn;
	private java.sql.PreparedStatement ps;
	private ResultSet rss;

	public Connection getConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, psd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	public ArrayList<ParkingInfo> query(String[] args) {
		String sql="select * from parking_urban where name like ? or describtion like ?";
		conn = getConn();
		try {
			ps = conn.prepareStatement(sql);
			if (args != null) {// 对传过来的占位符进行赋值
				for (int i = 0; i < args.length; i++) {
					ps.setString(i + 1, args[i]);
				}

				rss = ps.executeQuery();
				System.out.println(rss);
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
						System.out.println(arrayList);
					}
					return arrayList;
				}
				
//				ResultSetMetaData rsmd = rs.getMetaData();
//				int count = rsmd.getColumnCount();// 得到数据列的数量
//				while (rs.next()) { // 这个是把执行sql语句之后选择出来的记录，next命令时向下移动，游标，对移动到的记录操作，得到了他的列数，则每过一列加一个*号，之后再把*号转化成//
//					for (int i = 0; i < count; i++) {
//						result += rs.getString(i + 1) + "*";
//					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
