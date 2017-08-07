package com.hfut.parking.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hfut.parking.database.DBManager;
import com.hfut.parking.domian.Orders;
import com.mysql.jdbc.Statement;

/**
 * 车位预定依赖于订单信息表
 */
public class OrdersDao {
	/**
	 * 查询parking_info表中车位剩余数量是否>=1
	 * 
	 * @return totlenumb-+
	 * @throws SQLException
	 */
	public boolean isExitstparking(int id) throws SQLException {
		boolean flag = false;
		String sql = "select * from parking_info where " + "id" + "=" + id;
		System.out.println("查询是否有空余车位：" + sql);
		DBManager db = new DBManager();
		Statement stmt = db.getStatement();
		// rst = stmt.executeQuery(sql);
		ResultSet rst = stmt.executeQuery(sql);

		while (rst.next()) {

			int i = rst.getInt("totlenumb");
			System.out.println("获得车位数量：" + rst.getString("totlenumb"));

			if (i >= 1) {
				flag = true;
			} else {
				flag = false;
			}

		}

		rst.close();
		System.out.println(flag);
		return flag;
	}

	/**
	 * 添加预定信息到数据库预订信息表orders中
	 * 
	 * @param orders
	 */
	public void addOrders(Orders orders) {
		// Orders addorders = new Orders();

		DBManager db = new DBManager();
		int number = orders.getNumber();
		String createtime = orders.getCreatetime();
		int state = orders.getState();
		String username = orders.getUsername();
		String usertel = orders.getUsertel();
		int parkingid = orders.getParkingid();// 车位ID
		String parkingname = orders.getParkingname();

		String sql = "Insert into "
				+ "orders(number,creatTime,state,user_name,user_tel,parking_id,parking_name)"
				+ " values (" + number + "," + "'" + createtime + "'," + state
				+ "," + "'" + username + "'," + "'" + usertel + "',"
				+ parkingid + ",'" + parkingname + "')";
		
		System.out.println("sql = " + sql);
		int executeResult = db.update(sql);
		System.out.println(executeResult);
		
	}
	
	public void update(int id) throws SQLException{
		 String sql = "update parking_info set totlenumb=totlenumb-1 where "+ "id" + "=" + id;
		 System.out.println("当前车位数量：" + sql);	
		 DBManager db = new DBManager();
			Statement stmt = db.getStatement();
			stmt.executeUpdate(sql);
		}
		
}
