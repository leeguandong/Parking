package com.hfut.parking.login;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hfut.parking.global.GlobalConstant;
import com.hfut.parking.map.SearchParking;
import com.hfut.parking.utils.Utils;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

public class Login extends Activity implements OnClickListener {
	EditText etUserName, etUserPass;
	CheckBox chk;
	SharedPreferences pref;
	Editor editor;
	// 登陆按钮
	private Button logbtn;
	// 注册按钮
	private Button regbtn;
	// 返回的数据
	private String info;

	String name;
	String pass;

	String msgtext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏  
		setContentView(R.layout.activity_login);

		logbtn = (Button) findViewById(R.id.btnLogin);
		regbtn = (Button) findViewById(R.id.btnRegist);

		chk = (CheckBox) findViewById(R.id.chkSaveName);
		pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = pref.edit();// 申请空间并启动编辑
		name = pref.getString("userName", "");// 把这个name存进来，下次进来就存在了
		if (name.equals("")) {
			chk.setChecked(false);
		} else {
			chk.setChecked(true);
			etUserName.setText(name);
		}

		// 设置按钮监听器
		logbtn.setOnClickListener((OnClickListener) this);
		regbtn.setOnClickListener((OnClickListener) this);
	}

	// public String serverUrl =
	// "http://192.168.155.1/ZP04Web/servlet/LogServlet";
	private ProgressDialog loginProgress;
	public static final int MSG_LOGIN_RESULT = 0;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(Login.this, (String) msg.obj, 0).show();
			msgtext = (String) msg.obj;
			// 登录成功，页面跳转
			if (msgtext.equals("登录成功！")) {
				Intent intent = new Intent(Login.this, Personal.class);
				startActivity(intent);
			}
		}
	};

	public void onClick(View v) {
		etUserName = (EditText) findViewById(R.id.etuserName);
		etUserPass = (EditText) findViewById(R.id.etuserPass);
		final String name = etUserName.getText().toString().trim();// 返回一个字符串并去掉其中名的空格
		final String pass = etUserPass.getText().toString().trim();// 通过这个在editview中得到名字和密码

		switch (v.getId()) {
		case R.id.btnLogin:
			System.out.println("登录");
			login(name, pass);
			break;
		case R.id.btnRegist:
			System.out.println("注册");
			Intent intent = new Intent(Login.this, Register.class);
			startActivity(intent);
			break;
		}
	}

	public void login(final String name, final String pass) {

		// boolean flag = false;

		Thread t = new Thread() {
			@Override
			public void run() {
				// 提交的数据需要经过url编码，英文和数字编码后不变
				@SuppressWarnings("deprecation")
				String path = GlobalConstant.SERVER_URL+"/ZP04Web/servlet/LoginServlet?name="
						+ name + "&pass=" + pass;

				try {
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
		};
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
