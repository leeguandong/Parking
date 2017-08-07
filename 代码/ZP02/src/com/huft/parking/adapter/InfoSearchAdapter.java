package com.huft.parking.adapter;

import java.util.ArrayList;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.global.GlobalConstant;
import com.hfut.zp02.R;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfoSearchAdapter extends BaseAdapter {

	private ArrayList<ParkingData> list;
	private Context context;

	public InfoSearchAdapter(Context context, ArrayList<ParkingData> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = View.inflate(context, R.layout.list_info_item, null);
		}

		SmartImageView info_iv_pic = (SmartImageView) view.findViewById(R.id.info_iv_pic);
		TextView info_tv_name = (TextView) view.findViewById(R.id.info_tv_name);
	//	TextView info_tv_des=(TextView) view.findViewById(R.id.info_tv_des);
		TextView info_tv_totlenumb = (TextView) view
				.findViewById(R.id.info_tv_totlenumb);
		TextView info_tv_charge=(TextView) view.findViewById(R.id.info_tv_charge);
		

		// 3.获取postion位置条目对应的list集合中的车位数据
		ParkingData parkingData = list.get(position);
		// 4.将数据设置给这些子控件做显示
		info_iv_pic.setImageUrl(GlobalConstant.SERVER_URL+"/parkingassistant"+parkingData.imgurl);
		info_tv_name.setText(parkingData.name);
	//	info_tv_des.setText(parkingData.describtion);
		info_tv_totlenumb.setText("车位剩余数量:"+parkingData.totlenumb);
		info_tv_charge.setText(parkingData.charge);

		return view;
	
	}

}
