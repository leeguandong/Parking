package com.hfut.parking.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hfut.parking.service.OrdersService;

public class OrdersServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");// 请求编码(Post)
		response.setContentType("text/html；charset=utf-8");// 响应编码

		// 依赖UserService层
		OrdersService orderservice = new OrdersService();
		// 接收车位id
		int id = Integer.parseInt(request.getParameter("id"));
		// 接收number
		int number = Integer.parseInt(request.getParameter("number"));
		// 接收createtime
		String createtime = request.getParameter("createtime");
		createtime = new String(createtime.getBytes("ISO-8859-1"), "GBK");
		// 接受传递过来的用户名
		String username = request.getParameter("username");
		username = new String(username.getBytes("ISO-8859-1"), "GBK");
		// 接收用户联系方式
		String usertel = request.getParameter("usertel");
		// usertel = new String(usertel.getBytes("ISO-8859-1"), "GBK");
		// 接收车位名称
		String parkingname = request.getParameter("parkingname");
		parkingname = new String(parkingname.getBytes("ISO-8859-1"), "GBK");
		// 接收车位的唯一标识ID
		// int id = Integer.parseInt(request.getParameter("id"));
		System.out.println(id + "," + number + "," + createtime + ","
				+ username + "," + usertel + "," + parkingname);
		// 调用Service的业务层方法(判断登录是否成功)
		OutputStream os = response.getOutputStream();
		String orderRes = null;
		try {
			orderRes = orderservice.isOrder(id, number, createtime, username,
					usertel, parkingname);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		os.write(orderRes.getBytes("utf-8"));
		os.flush();
		os.close();
		System.out.println(orderRes);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
