package com.hfut.parking.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
 * listview的本地数据库缓存
 */
public class ParkingOpenHelper extends SQLiteOpenHelper {

	public ParkingOpenHelper(Context context) {
		super(context, "Parkingassisant3", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table rentinfo (_id integer ,name varchar(200),imgurl varchar(200),"
				+ "rent varchar(200),des varchar(200))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
