package com.hfut.parking.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hfut.parking.service.UserService;

/**
 * User的servlet层
 * 
 */
public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");// 请求编码(Post)
		response.setContentType("text/html;charset=utf-8");// 响应编码

		// 依赖UserService层
		UserService userservice = new UserService();
		// 接受传递过来的用户名和密码参数
		String username = request.getParameter("name");
		String password = request.getParameter("pass");
		// 调用Service的业务层方法(判断登录是否成功)
		OutputStream os = response.getOutputStream();
		try {
			String loginRes = userservice.isLogin(username, password);
			os.write(loginRes.getBytes("utf-8"));
			System.out.println(loginRes);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		os.flush();
		os.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);

	}

	public void init() throws ServletException {
		// Put your code here
	}

}
