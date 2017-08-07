package com.hfut.parking.login;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfut.parking.global.GlobalConstant;
import com.hfut.parking.utils.Utils;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

@SuppressLint({ "HandlerLeak", "ShowToast" })
public class Register extends Activity {
	// 注册按钮
	private Button regbtn;
	EditText etUName, etUPass, reetUPass;
	String name, pass, repass, msgtext;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(Register.this, (String) msg.obj, 0).show();
			msgtext = (String) msg.obj;
			// 注册成功，页面跳转
			if (msgtext.equals("注册成功！")) {
				Intent intent = new Intent(Register.this, Login.class);
				startActivity(intent);
			} else {
				Toast.makeText(Register.this, "注册失败", Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_register);
		regbtn = (Button) findViewById(R.id.btn_register);// 注册

		etUName = (EditText) findViewById(R.id.et_name);
		etUPass = (EditText) findViewById(R.id.et_password);
		reetUPass = (EditText) findViewById(R.id.et_sure_password);

		regbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				name = etUName.getText().toString().trim();
				pass = etUPass.getText().toString().trim();
				repass = reetUPass.getText().toString().trim();
				System.out.println(name + pass);

				if (repass.equals(pass) && TextUtils.isEmpty(pass)) {
					login(name, pass);
				} else {
					Toast.makeText(Register.this, "两次输入密码不一致", 2000).show();
				}

				System.out.println("提交数据：" + name + pass);

			}

		});

	}

	public void login(final String name, final String pass) {

		// boolean flag = false;

		Thread t = new Thread() {
			@Override
			public void run() {
				// 提交的数据需要经过url编码，英文和数字编码后不变

				/*
				 * String path =
				 * "http://192.168.56.1:8080/ZP04Web/servlet/RegistServlet?name="
				 * + URLEncoder.encode(name, "GBK") + "&pass=" + pass;
				 */

				try {
					String path = GlobalConstant.SERVER_URL
							+ "/ZP04Web/servlet/RegistServlet?name="
							+ URLEncoder.encode(name, "GBK") + "&pass=" + pass;
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