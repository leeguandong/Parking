package com.hfut.parking.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hfut.parking.database.DBManager;
import com.hfut.parking.domian.User;

public class UserDao {
	private String path = "110";

	/**
	 * 按用户名称查询用户信息
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public User findByUsername(String username) throws SQLException {
		User user = new User();
		String sqlfindbyuser = "select name,password from " + "users"
				+ " where " + DBManager.COLUMN_USERNAME + " = " + "'"
				+ username + "'";
		System.out.println("url = " + sqlfindbyuser);
		DBManager db = new DBManager();
		// db.getStatement();
		ResultSet rst = db.query(sqlfindbyuser);
		// ArrayList<HashMap<String, Object>> al = new DBManager()
		// .getDatabaseContents();
		while (rst.next()) {
			user.setUsername(rst.getString(DBManager.COLUMN_USERNAME));
			user.setPassword(rst.getString(DBManager.COLUMN_PASSWORD));
			// System.out.println(rst.getString(DBManager.COLUMN_USERNAME
			// + DBManager.COLUMN_PASSWORD));
		}
		System.out.println(user.getUsername() + user.getPassword());
		return user;
	}

	/**
	 * 添加用户信息（注册）
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		User adduser = new User();
		// adduser.setUsername(username)
		DBManager db = new DBManager();
		String username = user.getUsername();
		String password = user.getPassword();
		String sql = "Insert into " + "users(name,password)" + " values ("
				+ "'" + username + "'," + "'" + password + "')";
		System.out.println("sql = " + sql);
		int executeResult = db.update(sql);
		System.out.println(executeResult);
	}
	/*
	 * // 判断用户名是否存在 public boolean isUsernameExsited(String name) { boolean
	 * isExisted = true; DBManager db = new DBManager(); String sql =
	 * "select * from " + "users" + " where " + DBManager.COLUMN_USERNAME +
	 * " = " + "'" + name + "'"; System.out.println(sql); ResultSet rst =
	 * db.query(sql); System.out.println(rst); try { rst.next(); String username
	 * = rst.getString(DBManager.COLUMN_USERNAME); System.out.println(username);
	 * isExisted = true; } catch (SQLException e) { isExisted = false;
	 * e.printStackTrace(); }
	 * 
	 * return isExisted; }
	 */
}
