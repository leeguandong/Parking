package com.hfut.parking.map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hfut.zp02.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 实现车位条件搜索功能
 * 
 */
public class IfQuary extends Activity {

	private MapView mapViewLocation;
	private ListView lvLocNear;
	private double mCurrentLantitude;
	private double mCurrentLongitude;
	private BaiduMap mBaiduMap;
	private Marker mCurrentMarker;
	private Context context;
	private MyLocationListener mlocationListener;
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private boolean isFirsth = true;
	private PoiSearch poiSearch;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_ifquary);
		// 声明LocationClient类
		mLocationClient = new LocationClient(getApplicationContext());
		// 注册监听函数
		mLocationClient.registerLocationListener(myListener);

		initView();
		initlocation();
		initPoi();
	}

	/**
	 * 实现搜索周边位置的功能，并显示到地图上，调用的是百度地图上的数据
	 */
	private void initPoi() {
		// 初始化搜索模块
		poiSearch = PoiSearch.newInstance();
		poiSearch.searchNearby(getSearchParams());
		// 注册搜索事件监听,获取到搜索结果的监听器
		poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());
	}

	/**
	 * @return
	 */
	private PoiNearbySearchOption getSearchParams() {
		PoiNearbySearchOption option = new PoiNearbySearchOption();
		option.keyword("停车场");
		// option.sortType(PoiSortType.distance_from_near_to_far);
		option.location(new LatLng(mCurrentLantitude, mCurrentLongitude));

		// Bundle bundle = this.getIntent().getExtras();
		// // 获取Bundle中的数据，注意类型和key
		// String radius = bundle.getString("RADIUS");
		// int a = Integer.parseInt(radius);
		option.radius(100000);
		option.pageCapacity(20);
		return option;
	}

	class PoiSearchResultListener implements OnGetPoiSearchResultListener {
		@Override
		public void onGetPoiDetailResult(PoiDetailResult result) {
		}

		/**
		 * 接受周边地理位置结果
		 * 
		 * @param poiResult
		 */
		@Override
		public void onGetPoiResult(PoiResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(context, "没有搜索到结果", Toast.LENGTH_SHORT).show();
				return;
			}

			PoiOverlay poiOverlay = new PoiOverlay(mBaiduMap) {
				@Override
				public boolean onPoiClick(int index) {
					PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);
					Toast.makeText(context, poiInfo.name + "," + poiInfo.address, Toast.LENGTH_SHORT)
							.show();
					return true;
				}
			};

			mBaiduMap.setOnMarkerClickListener(poiOverlay);
			poiOverlay.setData(result); // 把数据设置给覆盖物
			poiOverlay.addToMap(); // 把所有的数据的变成覆盖添加到BaiduMap
			poiOverlay.zoomToSpan(); // 把所有的搜索结果在一个屏幕内显示出来

			System.out.println("停车场停车场");
		}

		@Override
		public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mapViewLocation = (MapView) findViewById(R.id.mapview_location);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap = mapViewLocation.getMap();
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 初始化定位
	 */
	private void initlocation() {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 坐标类型
		option.setIsNeedAddress(true);// 地址参数，只有设置这个参数为true，才能在后面的toast中得到参数
		option.setOpenGps(true);// 打开gps
		option.setScanSpan(1000);// 一秒返回一次
		mLocationClient.setLocOption(option);
	}

	/**
	 * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListener implements BDLocationListener {
		private BDLocation lastLocation;

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude()).build();//

			mBaiduMap.setMyLocationData(data);

			if (lastLocation != null) {
				if (lastLocation.getLatitude() == location.getLatitude()
						&& lastLocation.getLongitude() == location.getLongitude()) {
					Log.d("map", "same location, skip refresh");
					// mMapView.refresh(); //need this refresh?
					return;
				}
			}

			lastLocation = location;
			mBaiduMap.clear();
			mCurrentLantitude = lastLocation.getLatitude();
			mCurrentLongitude = lastLocation.getLongitude();
			Log.e(">>>>>>>", mCurrentLantitude + "," + mCurrentLongitude);
			LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			LatLng ruitai1pos = new LatLng(lastLocation.getLatitude()-0.002, lastLocation.getLongitude()-0.001);
			LatLng ruitai2pos = new LatLng(lastLocation.getLatitude()+0.001, lastLocation.getLongitude()+0.002);
			
			// // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
			// CoordinateConverter converter = new CoordinateConverter();
			// converter.from(CoordinateConverter.CoordType.COMMON);
			// // 11A待转换坐标
			// converter.coord(llA);
			// LatLng convertLatLng = converter.convert();

			OverlayOptions ooA = new MarkerOptions().position(llA)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked))
					.zIndex(4).draggable(true);
			mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooA);
			
			OverlayOptions ooB = new MarkerOptions().position(ruitai1pos)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marki))
					.zIndex(4).draggable(true);
			mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooB);
			
			OverlayOptions ooC = new MarkerOptions().position(ruitai2pos)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marki))
					.zIndex(4).draggable(true);
			mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooC);

//			MarkerOptions options = new MarkerOptions();
//			BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_marki);
//			options.position(ruitaipos) // 位置
//					.icon(icon) // 图标
//					.draggable(true); // 设置图标可以拖动
//
//			mBaiduMap.addOverlay(options);

			if (isFirsth) {// 是不是第一次打开，若第一次打开，地图显示在中间
				isFirsth = false;
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);
				mBaiduMap.animateMapStatus(msu);

				// // initPoi();
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// initPoi();
				// }
				// }).start();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapViewLocation.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapViewLocation.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapViewLocation.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);// 默认第一次开启的权限
		mLocationClient.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);// 停止定位
		mLocationClient.stop();
	}

}