package com.hfut.weather;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class WeatherHelp extends Activity {
	private String result, TAG;
	private Handler handler;
	private TextView tv, tv1, tv2, tv3, tv4, tv5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		initView();
		jump();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (result != null) {
					try {
						JSONObject datajson = new JSONObject(result);// 第一步、将string格式转换回json格式

						JSONArray results = datajson.getJSONArray("results");// 获取results数组
						//
						JSONObject city = results.getJSONObject(0);
						String currentCity = city.getString("currentCity");// 获取city名字
						String pm25 = city.getString("pm25");// 获取pm25
						Log.i(TAG, "城市" + currentCity + " pm25 " + pm25);// 测试城市和pm25
						tv.setText("城市:" + currentCity + "\n" + "pm25:" + pm25);
						JSONArray index = city.getJSONArray("index");// 获取index里面的JSONArray
						// 获取穿衣
						JSONObject cy = index.getJSONObject(0);
						String titlec = cy.getString("title");
						String zsc = cy.getString("zs");
						String tiptc = cy.getString("tipt");
						String desc = cy.getString("des");
						// 获取洗车
						JSONObject xc = index.getJSONObject(1);
						String titlex = xc.getString("title");
						String zsx = xc.getString("zs");
						String tiptx = xc.getString("tipt");
						String desx = xc.getString("des");
						Log.i(TAG, "！！！！！！！！！" + titlec + zsc + tiptc + desc);// 测试穿衣
						tv1.setText(titlec + ":" + zsc + "\n" + tiptc + ":"
								+ desc);
						Log.i(TAG, "！！！！！！！！！" + titlex + zsx + tiptx + desx);
						tv2.setText(titlex + ":" + zsx + "\n" + tiptx + ":"
								+ desx);
						/*
						 * weather_data，未来几天
						 */
						JSONArray weather_data = city
								.getJSONArray("weather_data");
						// 获取今天
						JSONObject today = weather_data.getJSONObject(0);
						String date0 = today.getString("date");
						String dayPictureUrl0 = today
								.getString("dayPictureUrl");
						String nightPictureUrl0 = today
								.getString("nightPictureUrl");
						String weather0 = today.getString("weather");
						String wind0 = today.getString("wind");
						String temperature0 = today.getString("temperature");
						Log.i(TAG, "!!!!!!!!!" + date0 + dayPictureUrl0
								+ nightPictureUrl0 + weather0 + wind0
								+ temperature0);
						tv3.setText("今天:" + date0 + "\n" + "实时:" + weather0
								+ "\n" + "风力：" + wind0 + "\n" + "温度范围:"
								+ temperature0);

						// 获取明天
						JSONObject tomorrow = weather_data.getJSONObject(1);
						String date1 = tomorrow.getString("date");
						String weather1 = tomorrow.getString("weather");
						String wind1 = tomorrow.getString("wind");
						String temperature1 = tomorrow.getString("temperature");
						tv4.setText("明天：" + date1 + "\n" + weather1 + "\n"
								+ "风力：" + wind1 + "\n" + "温度范围：" + temperature1
								+ "\n");

						// 获取后天
						JSONObject after_tomorrow = weather_data
								.getJSONObject(2);
						String date2 = after_tomorrow.getString("date");
						String weather2 = after_tomorrow.getString("weather");
						String wind2 = after_tomorrow.getString("wind");
						String temperature2 = after_tomorrow
								.getString("temperature");
						tv5.setText("后天：" + date2 + "\n" + weather2 + "\n"
								+ "风力：" + wind2 + "\n" + "温度范围：" + temperature2
								+ "\n");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				super.handleMessage(msg);
			}
		};
	}

	private void initView() {
		tv = (TextView) findViewById(R.id.tv);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);

	}

	private String send(String city) {
		String target = TargetUrl.url1 + city + TargetUrl.url2; // 要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(target); // 创建HttpGet对象
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result = EntityUtils.toString(httpResponse.getEntity()).trim(); // 获取返回的字符串
			} else {
				result = "fail";
			}
		} catch (ClientProtocolException e) {
			// TODO: handle exception\
			e.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/***
	 * 请求天气服务器
	 * 
	 * @param v
	 */
	public void jump() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				send("合肥");
				Message m = handler.obtainMessage();
				handler.sendMessage(m);
			}
		}).start();

	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}