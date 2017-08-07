package com.hfut.parking.service;

import java.sql.SQLException;

import com.hfut.parking.dao.OrdersDao;
import com.hfut.parking.domian.Orders;

public class OrdersService {
	// 依赖于OrderDao,该类封装了对数据库进行操作的方法
	OrdersDao orderdao = new OrdersDao();
	// 预订者实例对象
	Orders orders = new Orders();

	// 判断是否预定成功
	public String isOrder(int id, int number, String createtime,
			String username, String usertel, String parkingname)
			throws SQLException {
		String orderRes = "预定是否成功！";
		if (orderdao.isExitstparking(id) && username.trim().length() != 0
				&& usertel.trim().length() != 0) {
			orderRes = "预订成功！";
			orders.setParkingid(id);// 设置车位ID
			orders.setNumber(number);// 车位剩余数目
			orders.setParkingname(parkingname);// 车位名称
			orders.setCreatetime(createtime);// 创建预定时间
			orders.setState(1);// 预定状态为成功
			orders.setUsername(username);// 预订者姓名
			orders.setUsertel(usertel);// 预订者联系方式

			System.out
					.println(orders.getParkingid() + "," + orders.getNumber()
							+ "," + orders.getCreatetime() + ","
							+ orders.getUsername() + "," + orders.getUsertel()
							+ "," + orders.getParkingname());
			orderdao.addOrders(orders);
			orderdao.update(id);
		} else {
			orderRes = "预订失败！预订失败！预订失败！";
		}

		return orderRes;
	}
}
