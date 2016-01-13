package com.scyb.aisbroadcast.bd.dao.impl;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.LocationInfo;
import com.scyb.aisbroadcast.bd.dao.ILocationInfoDao;
import com.scyb.aisbroadcast.common.util.SqlHelper;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/7 Time:10:38
 */
public class LocationInfoDaoImpl implements ILocationInfoDao {

	private Logger log = Logger.getLogger(this.getClass());
	private SqlHelper sqlHelper = new SqlHelper();

	@Override
	public void saveLocationInfo(LocationInfo locationInfo) {
		String sql = "insert into LOCATION_INFO values (?,?,?,?,?,?)";
		String[] parameters = { UUID.randomUUID().toString(), locationInfo.getMsgNo(), locationInfo.getModel(),
				locationInfo.getLatlon(), locationInfo.getHexMsg(),locationInfo.getCreateTime() };
		sqlHelper.executeUpdate(sql, parameters);
	}

	@Override
	public LocationInfo getLocationInfo(int model) {
		String sql = "select * from location_info where model = " + model + " order by create_time desc";
		String[] param = {};
		ArrayList<Object> rsList = sqlHelper.executeQuery(sql, param);
		LocationInfo li = new LocationInfo();
		for (int i = 0; i < rsList.size(); i++) {
			Object[] objects = (Object[]) rsList.get(i);
			li.setGuid((String) objects[0]);
			li.setMsgNo((String) objects[1]);
			li.setModel((String) objects[2]);
			li.setLatlon((String) objects[3]);
			li.setHexMsg((String) objects[4]);
			li.setCreateTime((String) objects[5]);
		}
		return li;
	}
}
