package com.hfut.parking.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRouteGuideManager.CustomizedLayerItem;
import com.baidu.navisdk.adapter.BNRouteGuideManager.OnNavigationListener;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

public class BNGuideActivity extends Activity {

	private BNRoutePlanNode mBNRoutePlanNode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createHandler();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		}
		View view = BNRouteGuideManager.getInstance().onCreate(this,
				new OnNavigationListener() {

					@Override
					public void onNaviGuideEnd() {
						finish();
					}

					@Override
					public void notifyOtherAction(int actionType, int arg1,
							int arg2, Object obj) {

					}
				});

		if (view != null) {
			setContentView(view);
		}

		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				mBNRoutePlanNode = (BNRoutePlanNode) bundle
						.getSerializable(SearchParking.ROUTE_PLAN_NODE);
			}
		}
	}

	@Override
	protected void onResume() {
		BNRouteGuideManager.getInstance().onResume();
		super.onResume();

		MobclickAgent.onResume(this);
		hd.sendEmptyMessageDelayed(MSG_SHOW, 5000);
	}

	protected void onPause() {
		super.onPause();
		BNRouteGuideManager.getInstance().onPause();
		MobclickAgent.onPause(this);
	};

	@Override
	protected void onDestroy() {
		BNRouteGuideManager.getInstance().onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		BNRouteGuideManager.getInstance().onStop();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		BNRouteGuideManager.getInstance().onBackPressed(false);
	}

	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		BNRouteGuideManager.getInstance().onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	};

	private void addCustomizedLayerItems() {
		List<CustomizedLayerItem> items = new ArrayList<CustomizedLayerItem>();
		CustomizedLayerItem item1 = null;
		if (mBNRoutePlanNode != null) {
			item1 = new CustomizedLayerItem(mBNRoutePlanNode.getLongitude(),
					mBNRoutePlanNode.getLatitude(),
					mBNRoutePlanNode.getCoordinateType(), getResources()
							.getDrawable(R.drawable.ic_launcher),
					CustomizedLayerItem.ALIGN_CENTER);
			items.add(item1);

			BNRouteGuideManager.getInstance().setCustomizedLayerItems(items);
		}
		BNRouteGuideManager.getInstance().showCustomizedLayer(true);
	}

	private static final int MSG_SHOW = 1;
	private static final int MSG_HIDE = 2;
	private Handler hd = null;

	private void createHandler() {
		if (hd == null) {
			hd = new Handler(getMainLooper()) {
				public void handleMessage(android.os.Message msg) {
					if (msg.what == MSG_SHOW) {
						addCustomizedLayerItems();
					} else if (msg.what == MSG_HIDE) {
						BNRouteGuideManager.getInstance().showCustomizedLayer(
								false);
					}
				};
			};
		}
	}

}