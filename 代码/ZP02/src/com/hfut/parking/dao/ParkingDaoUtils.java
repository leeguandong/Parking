package com.hfut.parking.dao;

import java.util.ArrayList;

import com.hfut.parking.db.ParkingData;
import com.hfut.parking.db.RentInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/*
 * 把数据缓存到本地数据库中
 */
public class ParkingDaoUtils {
	private ParkingOpenHelper parkingOpenHelper;

	public ParkingDaoUtils(Context context) {
		// 创建一个帮助类
		parkingOpenHelper = new ParkingOpenHelper(context);
	}

	// 你每次登陆的时候都会缓存一些以往的数据，但是请求服务器之后获取到一些新数据，所以就要删除以前的数据，来存放新数据
	public void delete() {

		// 通过数据库对象获取一个数据库操作对象
		SQLiteDatabase db = parkingOpenHelper.getReadableDatabase();
		db.delete("rentinfo", null, null);
		db.close();
	}

	// 向数据库中添加数据
	public void saveRentInfo(ArrayList<RentInfo> arrayList) {

		// 通过数据库对象获取一个数据库操作对象
		SQLiteDatabase db = parkingOpenHelper.getReadableDatabase();
		for (RentInfo rentInfo : arrayList) {
			ContentValues values = new ContentValues();
			values.put("_id", rentInfo.id);
			values.put("name", rentInfo.name);
			values.put("imgurl", rentInfo.imgurl);
			values.put("rent", rentInfo.rent);
			values.put("des", rentInfo.des);

			db.insert("rentinfo", null, values);
		}
		db.close();
	}

	// 从数据库中获取缓存的车位数据
	public ArrayList<RentInfo> getRentInfos() {
		ArrayList<RentInfo> list = new ArrayList<RentInfo>();
		// 通过帮助类对象获取一个数据库操作对象
		SQLiteDatabase db = parkingOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from rentinfo", null);// 获取查询数据

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				RentInfo rentInfo = new RentInfo();
				rentInfo.id = cursor.getString(0);
				rentInfo.name = cursor.getString(1);
				rentInfo.imgurl = cursor.getString(2);
				rentInfo.rent = cursor.getString(3);
				rentInfo.des = cursor.getString(4);
				
				list.add(rentInfo);
			}
		}
		db.close();
		cursor.close();

		return list;
	}
}
