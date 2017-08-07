package aatest;

import java.sql.SQLException;

import org.junit.Test;

import com.hfut.parking.dao.UserDao;
import com.hfut.parking.domian.User;

public class UserDaoTest {

	@Test
	public void test() throws SQLException {
		UserDao ud = new UserDao();
		User us = ud.findByUsername("admin");
		//System.out.println(ud.("df"));
		
	}
}
