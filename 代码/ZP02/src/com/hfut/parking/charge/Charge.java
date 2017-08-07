package com.hfut.parking.charge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hfut.zp02.R;
import com.libs.zxing.CaptureActivity;
import com.umeng.analytics.MobclickAgent;

public class Charge extends Activity {

	private Button btn_scan;
	private Button btn_fee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge);

		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_fee = (Button) findViewById(R.id.btn_fee);

		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Charge.this, CaptureActivity.class);
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
