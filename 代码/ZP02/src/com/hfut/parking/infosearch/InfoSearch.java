package com.hfut.parking.infosearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.global.GlobalConstant;
import com.hfut.zp02.R;
import com.huft.parking.adapter.InfoSearchAdapter;
import com.huft.parking.adapter.ParkingAdapter;
import com.umeng.analytics.MobclickAgent;

/*
 * 这部分代码肯定都要在主activity中去写
 */
public class InfoSearch extends Activity {

	private EditText key;
	private Button ParkingQuery;
	private ListView infolist;
	private Context mContext;// 内容

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infosearch);

		key = (EditText) findViewById(R.id.et_infosearch);
		ParkingQuery = (Button) findViewById(R.id.parkingQuery);

		//final ImageView img_url = (ImageView) findViewById(R.id.imgurl);
		mContext = this;
		infolist = (ListView) findViewById(R.id.result);

		ParkingQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Handler myHandler = new Handler() {
					public void handleMessage(Message msg) {
						ArrayList<ParkingData> allNews = (ArrayList<ParkingData>) msg.obj;

						System.out.println(allNews);
						if (allNews != null && allNews.size() > 0) {
							// 3.创建一个adapter设置给listview
							InfoSearchAdapter infoSearchAdapter = new InfoSearchAdapter(mContext,
									allNews);
							infolist.setAdapter(infoSearchAdapter);
						}
					
	/*			
						String[] allItems = result.split("\\*"); // 定义一个数据，用来分割结果
						int record = allItems.length / 7;// 记录数，把记录数放到列表里面
						ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < record; i++) {
							Map<String, Object> item = new HashMap<String, Object>();

							item.put("imgurl", allItems[7 * i]);
							item.put("id", allItems[7 * i + 1]);
							item.put("name", allItems[7 * i + 2]);
							item.put("lon", allItems[7 * i + 3]);
							item.put("lat", allItems[7 * i + 4]);
							item.put("totlenumb", allItems[7 * i + 5]);
							item.put("describtion", allItems[7 * i + 6]);
							items.add(item);
						}

						SimpleAdapter simpleAdapter = new SimpleAdapter(
								InfoSearch.this, items, R.layout.parking_item,
								new String[] { "id", "name", "imgurl", "lon",
										"lat", "totlenumb", "describtion" },
								new int[] { R.id.id, R.id.name, R.id.imgurl,
										R.id.lon, R.id.lat, R.id.totlenumb,
										R.id.describtion });
						System.out.println("IMGURL:" + R.id.imgurl);
						resultlist.setAdapter(simpleAdapter);// 对listview经相关联,这五个参数最后两个要一一对应即可
     */
					}
				};

				new Thread(new Runnable() {
					@Override
					public void run() {
						//"http://172.21.125.1:8080/ZP04Web/servlet/InfoSearchServlet"
						AccessToServer query = new AccessToServer(
								GlobalConstant.SERVER_URL+"/ZP04Web/servlet/InfoSearchServlet");
						ArrayList<ParkingData> result = query.doGet(new String[] { "parking" },
								new String[] { key.getText().toString() });
						Message msg = new Message();
						msg.obj = result;
						myHandler.sendMessage(msg);
					}
				}).start();
			}
		});
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
