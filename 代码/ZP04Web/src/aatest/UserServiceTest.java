package aatest;


import java.sql.SQLException;

import org.junit.Test;

import com.hfut.parking.service.UserService;

public class UserServiceTest {

	@Test
	public void test() throws SQLException {
		UserService us=new UserService();
		//us.isLogin("tutu","1234");
		us.isRegister("aaa","1234");
		
		
	}

}
