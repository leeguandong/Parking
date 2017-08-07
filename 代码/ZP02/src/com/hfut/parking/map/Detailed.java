package com.hfut.parking.map;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hfut.parking.db.JsonToarray;
import com.hfut.parking.db.ParkingInfo;
import com.hfut.parking.global.GlobalConstant;
import com.hfut.parking.utils.Utils;
import com.hfut.zp02.R;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Detailed extends Activity implements OnClickListener {

	public String id;// 车位ID
	String number;// 剩余车位数量
	String createtime;// 更新时间
	String username;// 预订者姓名
	String usertel;// 预订者联系方式
	String parkingname;// 车位名称
	Button btncancel;// 取消
	Button btnorder;// 预定车位
	EditText et_name;// 预订者姓名
	EditText et_tel;// 预订者联系方式
	String msgtext;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(Detailed.this, (String) msg.obj, 0).show();
			msgtext = (String) msg.obj;
			// 预订成功，页面跳转
			if (msgtext.equals("预订成功！")) {
				Intent intent = new Intent(Detailed.this, SearchParking.class);
				startActivity(intent);
			} else {
				onBackPressed();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed);
		JsonToarray.toArray();// 获取服务器传递的数据
		for (ParkingInfo info : ParkingInfo.infos) {
			System.out.println(info);
		}
		detailDate(ParkingInfo.infos);// 动态更新详情页信息
		// 对预定和取消进行监听设置
		btncancel = (Button) findViewById(R.id.btnOrder);// 预定按钮
		btnorder = (Button) findViewById(R.id.btnCancel);// 取消按钮

		et_name = (EditText) findViewById(R.id.et_name);// 联系人姓名
		et_tel = (EditText) findViewById(R.id.et_tel);// 联系人电话

		btncancel.setOnClickListener((OnClickListener) Detailed.this);
		btnorder.setOnClickListener((OnClickListener) Detailed.this);
	}

	// 接受传递过来的数据
	@SuppressLint("SimpleDateFormat")
	private void detailDate(List<ParkingInfo> infos) {
		// 获取详情页
		ImageView img_detail = (ImageView) findViewById(R.id.img_deatil);// 图片
		TextView tv_name = (TextView) findViewById(R.id.tv_name);// 名称
		TextView tv_describe = (TextView) findViewById(R.id.tvdetail_describe);// 描述信息
		TextView tv_restparking = (TextView) findViewById(R.id.tv_restparking);// 剩余车位
		TextView tv_upatetime = (TextView) findViewById(R.id.tv_upatetime);// 更新时间
		// EditText etusername = (EditText) findViewById(R.id.et_name);// 预定者姓名

		// 获取Intent中的Bundle对象
		Bundle bundle = this.getIntent().getExtras();
		// 获取Bundle中的数据，注意类型和key
		String ID = bundle.getString("ID");

		System.out.println(ID);
		for (ParkingInfo info : infos) {
			if (ID.equals(info.getId())) {
				// img_detail.setScaleType(ScaleType.FIT_XY);// 基于控件大小填充图片
				SearchParking.utils = new BitmapUtils(
						Detailed.this.getApplicationContext());
				SearchParking.utils.display(img_detail,
						GlobalConstant.SERVER_URL+"/parkingassistant" + info.getImgurl());
				// 车位名称
				tv_name.setText(info.getName());
				// 车位描述
				tv_describe.setText(info.getDes());
				// 剩余车位
				tv_restparking.setText(info.getTotle());
				// 更新时间
				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				tv_upatetime.setText(dateFormat.format(now));

				id = ID;// 车位id
				number = tv_restparking.getText().toString();// 剩余车位数量
				createtime = tv_upatetime.getText().toString();// 更新时间
				parkingname = tv_name.getText().toString();// 车位名称

			} else {
				System.out.println("结果不匹配");
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnOrder:
			username = et_name.getText().toString().trim();// 预订者姓名
			usertel = et_tel.getText().toString().trim();// 预订者联系方式
			System.out.println("预定车位");
			System.out.println(id + "," + number + "," + createtime + ","
					+ username + "," + usertel + "," + parkingname);
			// 提交数据
			submitOrder(id, number, createtime, username, usertel, parkingname);
			break;
		case R.id.btnCancel:
			System.out.println("取消");
			onBackPressed();

			break;
		}

	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param id车位ID
	 * @param number车位剩余数量
	 * @param createtime创建时间
	 * @param username预订者姓名
	 * @param usertel预订者号码
	 * @param parkingname停车位名称F
	 */
	private void submitOrder(final String id, final String number,
			final String createtime, final String username,
			final String usertel, final String parkingname) {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String path = GlobalConstant.SERVER_URL+"/ZP04Web/servlet/OrdersServlet?id="
							+ id
							+ "&number="
							+ number
							+ "&createtime="
							+ URLEncoder.encode(createtime, "GBK")
							+ "&username="
							+ URLEncoder.encode(username, "GBK")
							+ "&usertel="
							+ usertel
							+ "&parkingname="
							+ URLEncoder.encode(parkingname, "GBK");
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					System.out.println("请求成功");
					System.out.println(conn.getResponseCode());

					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String text = Utils.getTextFromStream(is);

						System.out.println("读取回应信息:" + text);

						Message msg = handler.obtainMessage();
						msg.obj = text;
						handler.sendMessage(msg);

						/*
						 * if (text.equals("登录成功！")) { flag = true; } else {
						 * flag = false; }
						 */

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();

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
