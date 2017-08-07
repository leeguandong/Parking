package aatest;

import java.io.Closeable;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.hfut.parking.database.DBManager;

public class RegistServletTest {

	@Test
	public void test() {
		String name = "小王";
		String pass = "2222";

		String path = "http://192.168.56.1:8080/ZP04Web/servlet/RegistServlet?name="
				+ name + "&pass=" + pass;

		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			System.out.println("请求成功");
			System.out.println(conn.getResponseCode());
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				String text = Utils.getTextFromStream(is);
				System.out.println(text);
				/*
				 * Message msg = handler.obtainMessage(); msg.obj = text;
				 * handler.sendMessage(msg);
				 */
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
