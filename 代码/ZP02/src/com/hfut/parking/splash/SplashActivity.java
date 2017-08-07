package com.hfut.parking.splash;

import com.hfut.parking.map.SearchParking;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
/*
 * 闪屏页
 */

public class SplashActivity extends Activity {
	RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						SearchParking.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 2000);
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
