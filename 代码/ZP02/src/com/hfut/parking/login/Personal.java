package com.hfut.parking.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hfut.weather.WeatherHelp;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

public class Personal extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);

		// 天气预报
		Button WeahterButton = (Button) findViewById(R.id.btn_weather);
		WeahterButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Personal.this, WeatherHelp.class);
				startActivity(intent);
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
