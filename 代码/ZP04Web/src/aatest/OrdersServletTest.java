package aatest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class OrdersServletTest {

	@Test
	public void test() {

		String id = "1";
		String number = "21";
		String createtime = "2016年11月24日23:08";
		String username = "小张";
		String usertel = "15255147713";
		String parkingname = "图书馆停车位";

		String path = "http://192.168.56.1:8080/ZP04Web/servlet/OrdersServlet?id="
				+ id
				+ "&number="
				+ number
				+ "&createtime="
				+ createtime
				+ "&username="
				+ username
				+ "&usertel="
				+ usertel
				+ "&parkingname=" + parkingname;

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
