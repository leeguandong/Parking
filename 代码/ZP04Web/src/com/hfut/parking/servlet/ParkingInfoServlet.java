package com.hfut.parking.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hfut.parking.dao.ParkingInfoDao;
import com.hfut.parking.domian.ParkingInfo;

public class ParkingInfoServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// 从数据库获取车位数据
			ArrayList<ParkingInfo> list = ParkingInfoDao.getParkingInfo();

			// 将list中的数据封装成一个jsonArray对象
			JSONArray jsonArray = new JSONArray();
			for (ParkingInfo parkinginfo : list) {
				JSONObject newsJson = new JSONObject();
				newsJson.put("id", parkinginfo.getId());
				newsJson.put("imgurl", parkinginfo.getImgurl());
				newsJson.put("describtion", parkinginfo.getDescribtion());
				newsJson.put("lat", parkinginfo.getLat());
				newsJson.put("lon", parkinginfo.getLon());
				newsJson.put("name", parkinginfo.getName());
				newsJson.put("totlenumb", parkinginfo.getTotlenumb());
				newsJson.put("charge", parkinginfo.getCharge());
				jsonArray.put(newsJson);
				//System.out.println(jsonArray);
			}

			// 将jsonArray对象作为一个json对象，用来返回给客户端
			JSONObject allNewsJson = new JSONObject();
			allNewsJson.put("data", jsonArray);
			//System.out.println(allNewsJson);
			response.getOutputStream().write(
					allNewsJson.toString().getBytes("gbk"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
