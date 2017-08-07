package com.hfut.parking.service;

import java.sql.SQLException;

import com.hfut.parking.dao.UserDao;
import com.hfut.parking.domian.User;

/**
 * User的业务逻辑层 依赖UserDao *
 */
public class UserService {
	private UserDao userdao = new UserDao();

	// 判断是否登录成功并返回给LoginServlet
	public String isLogin(String username, String password) throws SQLException {
		String result = "登录是否成功";

		String sql = "select name,password from " + "users";
		System.out.println("url = " + sql);
		// DBManager db = new DBManager();
		// ResultSet rst = db.query(sql);

		/*
		 * try { while (rst.next()) { // 选择Name这列数据 String userpass =
		 * rst.getString("password"); // 输出结果
		 * System.out.println(rst.getString("name") + "\t" + userpass); } }
		 * catch (SQLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		// rst.close();
		// conn.close();

		if (userdao.findByUsername(username).getUsername() != null) {
			if (userdao.findByUsername(username).getUsername().equals(username)
					&& userdao.findByUsername(username).getPassword()
							.equals(password)) {
				result = "登录成功！";
			} else {
				result = "登录失败！";
			}

		} else {
			result = "登录失败！";
		}
		System.out.println(result);
		return result;

	}

	// 判断是否注册成功并返回给RegisterServlet
	public String isRegister(String username, String password)
			throws SQLException {
		String result = "注册是否成功";
		// String sql = "select name,password from " + "users";
		// System.out.println("url = " + sql);
		if (userdao.findByUsername(username).getUsername() != null
				|| username.trim().length() == 0
				|| password.trim().length() == 0) {
			result = "用户名已存在或用户名和密码不能为空，注册失败！";
		} else {
			result = "注册成功！";
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			userdao.addUser(user);
		}
		System.out.println(result);
		return result;

	}
}
