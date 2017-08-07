package com.hfut.parking.nav;

import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public class Nav extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nav);
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
