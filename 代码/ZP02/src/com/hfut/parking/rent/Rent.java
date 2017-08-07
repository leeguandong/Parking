package com.hfut.parking.rent;

import java.util.ArrayList;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.db.RentInfo;
import com.hfut.parking.utils.ParkingUtils;
import com.hfut.zp02.R;
import com.huft.parking.adapter.ParkingAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public class Rent extends Activity {

	private ArrayList<RentInfo> list;
	private Context mContext;
	// 首页的车位列表
	private ListView lvList;
	public BitmapUtils utils;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			ArrayList<RentInfo> allNews = (ArrayList<RentInfo>) msg.obj;

			if (allNews != null && allNews.size() > 0) {
				// 3.创建一个adapter设置给listview
				ParkingAdapter parkingAdapter = new ParkingAdapter(mContext,
						allNews);
				lvList.setAdapter(parkingAdapter);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rent);

		mContext = this;
		lvList = (ListView) findViewById(R.id.lv_rent);

		// 1.先去数据库中获取缓存的车位数据展示到listview
		ArrayList<RentInfo> allnews_database = ParkingUtils
				.getAllNewsForDatabase(mContext);

		if (allnews_database != null && allnews_database.size() > 0) {
			// 创建一个adapter设置给listview
			ParkingAdapter parkingAdapter = new ParkingAdapter(mContext,
					allnews_database);
			lvList.setAdapter(parkingAdapter);
		}

		// 通过网络获取服务器上的车位数据用list封装 ,获取网络数据需要在子线程中做
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 请求网络数据
				ArrayList<RentInfo> parkingData = ParkingUtils
						.getAllNewsForNetWork(mContext);
				// 通过handler将msg发送到主线程去更新Ui
				Message msg = Message.obtain();
				msg.obj = parkingData;
				handler.sendMessage(msg);
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
