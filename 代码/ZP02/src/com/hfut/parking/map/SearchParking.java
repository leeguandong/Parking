package com.hfut.parking.map;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.hfut.parking.booking.Booking;
import com.hfut.parking.charge.Charge;
import com.hfut.parking.db.JsonToarray;
import com.hfut.parking.db.ParkingInfo;
import com.hfut.parking.global.GlobalConstant;
import com.hfut.parking.infosearch.InfoSearch;
import com.hfut.parking.login.Login;
import com.hfut.parking.map.MyOrientationListener.OnOrientationListener;
import com.hfut.parking.nav.Nav;
import com.hfut.parking.rent.Rent;
import com.hfut.zp02.R;
import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;

public class SearchParking extends Activity {
	// 导航相关
	private static final String APP_FOLDER_NAME = "BNSDKDemo-ParkingNav";
	private String mSDCardPath = null;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";

	private MapView mMapView;
	private BaiduMap mBaiduMap;// baidumap这个对象主要实现的是与地图有关的参数，而这个mlocationclient这个对象主要实现是有客户端有关的，比如说多长时间回叫一次，而前者主要是实现地图的模式等等。

	public Activity mActivity;// 解析图片
	public static BitmapUtils utils;

	private Context context;
	private LocationClient mLocationClient;
	private MylocationListener mlocationListener;
	private boolean isFirsth = true;
	private double mLatitude;
	private double mlongtitude;

	// 自定义图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;

	// 覆盖物相关
	private BitmapDescriptor mMarker;
	private RelativeLayout mMarkerLy;

	// 创建自己的箭头定位
	private BitmapDescriptor bitmapDescriptor;

	private LatLng mLastLocationData;// 存最后一次定位成功之后的地址
	private LatLng mDestLocationData;

	// 跳转页面
	private Button btn_personal;
	private Button InfoSearchButton;
	private Button btn_booking;
	private Button btn_charge;
	private Button btn_naving;
	private Button btn_rent;
	private EditText et_radius;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.new_parking_activity_mian);
		SearchParking.this.context = this;// 进行初始化

		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marki);

		initView();// 初始化地图

		initlocation();// 初始化定位

		// 请求服务器端获取数据,必须的，这样才能在服务器端先把数据请求下来放到parkinginfo的infos集合中
		JsonToarray.toArray();

		onMarkclick();// 为marker添加点击事件

		mapOnclick();// 地图点击事件

		if (initDirs()) {
			initNavi();
		}

		// // 定位按钮
		// Button btn_location = (Button) findViewById(R.id.id_location);
		// btn_location.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// LatLng latLng = new LatLng(mLatitude, mlongtitude);
		// MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		// mBaiduMap.animateMapStatus(msu);
		// }
		// });

		// 个人中心登录
		btn_personal = (Button) findViewById(R.id.btn_personal);
		btn_personal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, Login.class);
				startActivity(intent);
			}
		});

		// 模糊查询跳转页面
		InfoSearchButton = (Button) findViewById(R.id.btn_parkingquery);
		InfoSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, InfoSearch.class);
				startActivity(intent);
			}
		});

		// 车位导航
		btn_naving = (Button) findViewById(R.id.btn_naving);
		btn_naving.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, Nav.class);
				startActivity(intent);
			}
		});

		// 车位预约
		btn_booking = (Button) findViewById(R.id.btn_parkingboking);
		btn_booking.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, Booking.class);
				startActivity(intent);
			}
		});

		// 车位付费
		btn_charge = (Button) findViewById(R.id.btn_charge);
		btn_charge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, Charge.class);
				startActivity(intent);
			}
		});

		// 车位出租
		btn_rent = (Button) findViewById(R.id.btn_renting);
		btn_rent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, Rent.class);
				startActivity(intent);
			}
		});

		// 车位按钮
		Button btn_parking = (Button) findViewById(R.id.id_parking);
		btn_parking.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng latLng = new LatLng(mLatitude, mlongtitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				addOverlays(ParkingInfo.infos);
			}
		});

		// 条件查询
		Button btn_ifquary = (Button) findViewById(R.id.btn_ifquary);
		btn_ifquary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showParkingDialog();
			}
		});

		// 导航按钮
		// View view=View.inflate(context, R.layout.detailed, null);
		Button btn_nav = (Button) findViewById(R.id.btn_nav);
		btn_nav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				routeplanToNavi(true);
			}
		});
	}

	/**
	 * 创建一个展示dialog的方法
	 */
	protected void showParkingDialog() {
		Builder builder = new AlertDialog.Builder(this);
		// 创建对话框对象之后，并不能直接使用，需要在create一下，以便于自己重写dialog界面，而不是采用系统默认的界面
		final AlertDialog dialog = builder.create();
		// 第三个变量不需要挂载，这是在activity中引用，而不是在自定义控件中引用
		final View view = View.inflate(getApplicationContext(), R.layout.dialog_parkingquary, null);
		// 让对话框显示一个自己定义的对话框界面效果
		// dialog.setView(view);
		// 在安卓2.3.3版本中会默认对话框有一个2dp的外边距，所以要屏蔽掉这个外边距，用下面这个方法，后四参数分别上下左右四个边的外边距
		dialog.setView(view);
		dialog.show();

		Button bt_submit = (Button) view.findViewById(R.id.btn_submit);
		Button bt_cancel = (Button) view.findViewById(R.id.btn_cancel);
		et_radius = (EditText) view.findViewById(R.id.et_radius);

		// 拿到文本内容，并且转成字符串
		final String radius = et_radius.getText().toString();

		bt_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SearchParking.this, IfQuary.class);
				// 利用bundle来存取数据
				Bundle bundle = new Bundle();
				bundle.putSerializable("RADIUS", radius);
				System.out.println("搜索范围为："+radius);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);// 拿到sdcard是否可用之后，在sdcard目录之下建一个根目录
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	String authinfo = null;

	@SuppressWarnings("deprecation")
	private void initNavi() {
		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				if (0 == status) {
					authinfo = "key校验成功!";
				} else {
					authinfo = "key校验失败, " + msg;
				}

				SearchParking.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// Toast.makeText(SearchParking.this, authinfo,
						// Toast.LENGTH_LONG).show();
					}
				});
			}

			public void initSuccess() {
				// Toast.makeText(SearchParking.this, "百度导航引擎初始化成功",
				// Toast.LENGTH_SHORT).show();
			}

			public void initStart() {
				// Toast.makeText(SearchParking.this, "百度导航引擎初始化开始",
				// Toast.LENGTH_SHORT).show();
			}

			public void initFailed() {
				Toast.makeText(SearchParking.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
			}

		}, null);

	}

	// 判断sdcard是否可用
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 初始化图层
	 */
	public void addOverlays(List<ParkingInfo> infos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for (ParkingInfo info : infos) {
			// 经纬度
			latLng = new LatLng(Double.parseDouble(info.getLat()), Double.parseDouble(info.getLon()));
			// 图标
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);

			Bundle arg0 = new Bundle();
			arg0.putSerializable("info", info);
			marker.setExtraInfo(arg0);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}

	// 为marker添加点击事件
	private void onMarkclick() {
		initMarker();

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// 从marker中获取info信息
				Bundle bundle = marker.getExtraInfo();
				final ParkingInfo infoUtil = (ParkingInfo) bundle.getSerializable("info");

				// 拿到控件并将控件上的信息显示在界面上
				ImageView iv_img = (ImageView) mMarkerLy.findViewById(R.id.iv_img);
				iv_img.setScaleType(ScaleType.CENTER_CROP);// 基于控件大小填充图片
				utils = new BitmapUtils(context);
				utils.display(iv_img,
						GlobalConstant.SERVER_URL + "/parkingassistant" + infoUtil.getImgurl());

				// 点击图片进入详情页
				iv_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(SearchParking.this, Detailed.class);
						// 利用bundle来存取数据
						Bundle bundle = new Bundle();
						bundle.putSerializable("ID", infoUtil.getId());
						System.out.println(infoUtil.getId());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

				TextView tv_name = (TextView) mMarkerLy.findViewById(R.id.tv_name);
				tv_name.setText(infoUtil.getName());
				TextView tv_description = (TextView) mMarkerLy.findViewById(R.id.tv_description);
				tv_description.setText(infoUtil.getDes());

				// 将布局显示出来
				mMarkerLy.setVisibility(View.VISIBLE);

				// 跟随小气泡
				// infowindow中的布局
				TextView tv = new TextView(SearchParking.this);
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setGravity(Gravity.CENTER);
				tv.setTextColor(android.graphics.Color.WHITE);
				tv.setText(infoUtil.getName());
				bitmapDescriptor = BitmapDescriptorFactory.fromView(tv);

				// infowindow位置
				LatLng latLng = new LatLng(Double.parseDouble(infoUtil.getLat()), Double
						.parseDouble(infoUtil.getLon()));
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);

				mDestLocationData = latLng;// 导航的终点坐标

				// infowindow点击事件
				OnInfoWindowClickListener listener = new OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick() {
						// 隐藏infowindow
						mBaiduMap.hideInfoWindow();

					}
				};
				// 显示infowindow，-47是偏移量，使infowindow向上偏移，不会挡住marker
				InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47, listener);
				mBaiduMap.showInfoWindow(infoWindow);
				return true;
			}
		});
	}

	// 地图点击事件
	private void mapOnclick() {
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mMarkerLy.setVisibility(View.GONE);
				mBaiduMap.hideInfoWindow();
			}

		});
	}

	private void initMarker() {
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marki);
		mMarkerLy = (RelativeLayout) findViewById(R.id.rl_marker);
	}

	private void initlocation() {
		mLocationClient = new LocationClient(this);
		mlocationListener = new MylocationListener();
		mLocationClient.registerLocationListener(mlocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 坐标类型
		option.setIsNeedAddress(true);// 地址参数，只有设置这个参数为true，才能在后面的toast中得到参数
		option.setOpenGps(true);// 打开gps
		option.setScanSpan(1000);// 一秒返回一次
		mLocationClient.setLocOption(option);
		// 初始化图标
		mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		myOrientationListener = new MyOrientationListener(context);

		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			@Override
			public void onOrientationChanged(float x) {
				mCurrentX = x;
			}
		});
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.id_bmapView);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapStatus(msu);

		// 获取mapview中的缩放控件
		// ZoomControls zoomControls = (ZoomControls) mMapView.getChildAt(0);
		// mMapView.removeViewAt(2);
		// 调整缩放控件的位置
		// zoomControls.setPadding(0, 0, 0, 150);
		// View defaultBaiduMapScaleButton = null;
		// for (int i = 0; i <mMapView.getChildCount(); i++) {
		// //遍历百度地图中的所有子View,找到这个扩大和缩放的按钮控件View，然后设置隐藏View即可
		// View child = mMapView.getChildAt(i);
		// if (child instanceof ZoomControls) {
		// defaultBaiduMapScaleButton = child;
		// //该defaultBaiduMapScaleButton子View是指百度地图默认产生的放大和缩小的按钮，得到这个View break;
		// }
		// }
		// defaultBaiduMapScaleButton.setPadding(0, 0, 0, 180);
		//
		//
		// // 隐藏百度的LOGO
		// View child = mMapView.getChildAt(1);
		// if (child != null && (child instanceof ImageView || child instanceof
		// ZoomControls))
		// {
		// child.setVisibility(View.INVISIBLE); }
		// // 不显示地图上比例尺 mMapView.showScaleControl(false);

		mMapView.getChildAt(2).setPadding(0, 0, 6, 423);// 这是控制缩放控件的位置
		mBaiduMap.setCompassPosition(new android.graphics.Point(50, 100));// 修改指南针位置
	}

	private class MylocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation Location) {
			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(Location.getRadius())//
					.latitude(Location.getLatitude())//
					.longitude(Location.getLongitude()).build();//

			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(LocationMode.NORMAL, true,
					mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = Location.getLatitude();
			mlongtitude = Location.getLongitude();

			LatLng latLng = new LatLng(Location.getLatitude(), Location.getLongitude());
			mLastLocationData = latLng;

			if (isFirsth) {// 是不是第一次打开，若第一次打开，地图显示在中间
				isFirsth = false;
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				Toast.makeText(context, Location.getAddrStr(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	// 导航路线实现方法
	private void routeplanToNavi(boolean mock) {
		CoordinateType coType = CoordinateType.BD09LL;// 百度导航api中
														// BNRoutePlanNode.CoordinateType已经支持bd09ll类型的返回值，这样的话就不要对定位返回的数据惊醒一个坐标转化，正常定位返回来的坐标类型一定是bd09ll，直接植入导航即可
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;

		sNode = new BNRoutePlanNode(mLastLocationData.longitude, mLastLocationData.latitude, "我的地点",
				null, coType);
		eNode = new BNRoutePlanNode(mDestLocationData.longitude, mDestLocationData.latitude, "目标地点",
				null, coType);

		System.out.println("起点坐标的经度" + mLastLocationData.longitude);
		System.out.println(mDestLocationData.longitude);

		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);
			BaiduNaviManager.getInstance().launchNavigator(this, list, 1, mock,
					new DemoRoutePlanListener(sNode));
		}
	}

	public class DemoRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			Intent intent = new Intent(SearchParking.this, BNGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@Override
		public void onRoutePlanFailed() {
			Toast.makeText(SearchParking.this, "算路失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);// 默认第一次开启的权限
		if (!mLocationClient.isStarted())
			mLocationClient.start();// 是否开启
		// 开启方向传感器
		myOrientationListener.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);// 停止定位
		mLocationClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();
	}

}
