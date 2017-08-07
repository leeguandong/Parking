package com.huft.parking.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.db.RentInfo;
import com.hfut.parking.global.GlobalConstant;
import com.hfut.zp02.R;
import com.loopj.android.image.SmartImageView;

/*
 * 出租页面listview展示的适配器
 */
public class ParkingAdapter extends BaseAdapter {

	private ArrayList<RentInfo> list;
	private Context context;

	public ParkingAdapter(Context context, ArrayList<RentInfo> list) {
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
			view = View.inflate(context, R.layout.list_parking_item, null);
		}

		SmartImageView list_iv_pic = (SmartImageView) view
				.findViewById(R.id.list_iv_pic);
		TextView list_tv_name = (TextView) view.findViewById(R.id.list_tv_name);
		TextView list_tv_des = (TextView) view.findViewById(R.id.list_tv_des);
		TextView list_tv_rent = (TextView) view.findViewById(R.id.list_tv_rent);

		// 3.获取postion位置条目对应的list集合中的车位数据
		RentInfo rentInfo = list.get(position);
		// 4.将数据设置给这些子控件做显示
		list_iv_pic.setImageUrl(GlobalConstant.SERVER_URL + "/parkingassistant"
				+ rentInfo.imgurl);
		list_tv_name.setText(rentInfo.name);
		list_tv_des.setText(rentInfo.des);
		list_tv_rent.setText(rentInfo.rent);

		return view;

	}

}
