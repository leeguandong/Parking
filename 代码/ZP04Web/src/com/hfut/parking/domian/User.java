package com.hfut.parking.domian;

/**
 * 实体类
 */
public class User {
	private String username;// 用户名
	private String password;// 密码

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + "]";
	}
}
