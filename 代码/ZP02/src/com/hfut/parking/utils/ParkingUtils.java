package com.hfut.parking.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.hfut.parking.dao.ParkingDaoUtils;
import com.hfut.parking.db.ParkingData;
import com.hfut.parking.db.RentInfo;
import com.hfut.parking.global.GlobalConstant;

/*
 * 请求数据库端的数据并且得到数据库返回的json数组并解析
 */

public class ParkingUtils {

	// "http://172.21.125.1:8080/ZP04Web/servlet/ParkingInfoServlet"
	public static String newsPath_url = GlobalConstant.SERVER_URL
			+ "/ZP04Web/servlet/RentServlet";

	// 封装新闻的假数据到list中返回
	public static ArrayList<RentInfo> getAllNewsForNetWork(Context context) {
		ArrayList<RentInfo> arrayList = new ArrayList<RentInfo>();
		try {
			// 1.请求服务器获取新闻数据
			// 获取一个url对象，通过url对象得到一个urlconnnection对象
			URL url = new URL(newsPath_url);
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

				// System.out.println(result);
				// 2.解析获取的车位数据到List集合中。
				JSONObject root_json = new JSONObject(result);// 将一个字符串封装成一个json对象。
				JSONArray jsonArray = root_json.getJSONArray("data");// 获取root_json中的newss作为jsonArray对象

				// System.out.println(jsonArray.length());

				for (int i = 0; i < jsonArray.length(); i++) {// 循环遍历jsonArray
					JSONObject news_json = jsonArray.getJSONObject(i);// 获取一条车位的json

					RentInfo rentInfo = new RentInfo();

					rentInfo.id = news_json.getString("id");
					rentInfo.imgurl = news_json.getString("imgurl");
					rentInfo.name = news_json.getString("name");
					rentInfo.rent = news_json.getString("rent");
					rentInfo.des=news_json.getString("des");

					arrayList.add(rentInfo);
				}

				// 3.将数据缓存到数据库中
				new ParkingDaoUtils(context).delete();// 要把之前的数据给删掉，然后存新请求的来的数据
				new ParkingDaoUtils(context).saveRentInfo(arrayList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;

	}

	// 这是一个先从本地缓存中去数据的方法
	public static ArrayList<RentInfo> getAllNewsForDatabase(Context context) {
		return new ParkingDaoUtils(context).getRentInfos();
	}

}
