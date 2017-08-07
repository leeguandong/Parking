package com.hfut.parking.db;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;

import com.hfut.parking.global.GlobalConstant;
import com.hfut.parking.utils.StreamUtils;

public class JsonToarray extends Activity {

	public static String Path_url = GlobalConstant.SERVER_URL
			+ "/ZP04Web/servlet/ParkingInfoServlet";

	public static void toArray() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// http://172.21.125.1:8080/parkingassistant/parkinginfo.json

					URL url = new URL(Path_url);
					// 获取一个url对象，通过url对象得到一个urlconnnection对象
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					// 设置连接的方式和超时时间
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(10 * 1000);
					// 获取请求响应码
					int code = connection.getResponseCode();
					if (code == 200) {
						// 获取请求到的流信息
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.streamToString(inputStream);
						// System.out.println("返回数据" + result);

						// 2.解析获取的数据到List集合中。
						JSONObject root_json = new JSONObject(result);// 将一个字符串封装成一个json对象。
						JSONArray jsonArray = root_json.getJSONArray("data");// 获取root_json中的newss作为jsonArray对象

						for (int i = 0; i < jsonArray.length(); i++) {// 循环遍历jsonArray
							JSONObject temp = jsonArray.getJSONObject(i);// 获取一条车位的json

							String des = temp.getString("describtion");
							String id = temp.getString("id");
							String imgurl = temp.getString("imgurl");
							String lat = temp.getString("lat");
							String lon = temp.getString("lon");
							String name = temp.getString("name");
							String totle = temp.getString("totlenumb");
							ParkingInfo.infos.add(new ParkingInfo(des, id,
									imgurl, lat, lon, name, totle));

							System.out.println("车位信息" + ParkingInfo.infos);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
