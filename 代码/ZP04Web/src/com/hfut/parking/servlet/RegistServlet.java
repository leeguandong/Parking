package com.hfut.parking.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hfut.parking.service.UserService;

public class RegistServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");// 请求编码(Post)
		response.setContentType("text/html；charset=utf-8");// 响应编码

		// 依赖UserService层
		UserService userReservice = new UserService();
		// 接受传递过来的用户名和密码参数
		// String username = new String(request.getParameter("name").getBytes(
		// "ISO8859-1"), "utf-8");
		String username = request.getParameter("name");
		username = new String(username.getBytes("ISO-8859-1"), "GBK");
		String password = request.getParameter("pass");
		System.out.println(username + "," + password);
		// 调用Service的业务层方法(判断登录是否成功)
		OutputStream os = response.getOutputStream();
		String registRes = null;
		try {
			registRes = userReservice.isRegister(username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		os.write(registRes.getBytes("utf-8"));
		os.flush();
		os.close();
		System.out.println(registRes);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
