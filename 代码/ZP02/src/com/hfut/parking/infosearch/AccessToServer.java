package com.hfut.parking.infosearch;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.utils.StreamUtils;

public class AccessToServer {
	private String url;
	String result = "";

	public AccessToServer(String url) {
		this.url = url;
	}

	ArrayList<ParkingData> arrayList = new ArrayList<ParkingData>();

	public ArrayList<ParkingData> doGet(String[] keys, String[] values) {
		HttpClient hc = new DefaultHttpClient();
		String urlStr = url;
		if (keys != null && values != null) {// 把输入进来的键值对都放到url中去
			urlStr += "?";
			for (int i = 0; i < keys.length; i++) {
				urlStr += keys[i] + "=" + values[i];
				if (i != keys.length - 1) {
					urlStr += "&";
				}
			}
		}
		System.out.println(urlStr);

		HttpGet hg = new HttpGet(urlStr);

		try {
			HttpResponse response = hc.execute(hg);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) // 等于200的话，则成功。
			{
				HttpEntity het = response.getEntity();
				InputStream is = het.getContent();
				String result = StreamUtils.streamToString(is);
				
				System.out.println(result);
				
				// 拿到json数据之后，解析获取的车位数据到List集合中
				JSONObject root_json = new JSONObject(result);// 将一个字符串封装成一个json对象。
				JSONArray jsonArray = root_json.getJSONArray("data");// 获取root_json中的data作为jsonArray对象
				
				System.out.println(jsonArray.length());

				for (int i = 0; i < jsonArray.length(); i++) {// 循环遍历jsonArray
					JSONObject news_json = jsonArray.getJSONObject(i);// 获取一条车位的json

					ParkingData parkingData = new ParkingData();

					parkingData.id = news_json.getInt("id");
					parkingData.imgurl = news_json.getString("imgurl");
					parkingData.lat = news_json.getString("lat");
					parkingData.lon = news_json.getString("lon");
					parkingData.name = news_json.getString("name");
					parkingData.totlenumb = news_json.getString("totlenumb");
					parkingData.charge=news_json.getString("charge");
				
					arrayList.add(parkingData);
					System.out.println(arrayList);
				}
				// BufferedReader br = new BufferedReader(
				// new InputStreamReader(is));
				// String readLine = null;
				// while ((readLine = br.readLine()) != null) {
				// result = result + readLine;
				// }
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
}
