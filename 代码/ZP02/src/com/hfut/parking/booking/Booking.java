package com.hfut.parking.booking;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

public class Booking extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking);
		
		TextView tv_upatetime = (TextView) findViewById(R.id.tv_upatetime);
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		tv_upatetime.setText(dateFormat.format(now));
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
