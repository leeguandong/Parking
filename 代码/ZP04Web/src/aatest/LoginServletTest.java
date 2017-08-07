package aatest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

public class LoginServletTest {

	@Test
	public void test() throws IOException {
		String name = "admin";
		String pass = "admin";

		String path = "http://192.168.56.1:8080/ZP04Web/servlet/LoginServlet?name="
				+ name + "&pass=" + pass;

		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(50000);
			conn.setReadTimeout(50000);
			System.out.println("«Î«Û≥…π¶");
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
