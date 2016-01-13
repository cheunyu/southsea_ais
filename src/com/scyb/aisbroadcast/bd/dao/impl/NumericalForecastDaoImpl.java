package com.scyb.aisbroadcast.bd.dao.impl;

import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.dao.INumericalForecastDao;
import com.scyb.aisbroadcast.common.util.SqlHelper;

import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/4 Time:10:43
 */
public class NumericalForecastDaoImpl implements INumericalForecastDao {

	private Logger log = Logger.getLogger(this.getClass());
	private SqlHelper sqlHelper = new SqlHelper();

	@Override
	public void saveNumericalForecast(NumericalForecast numericalForecast) {
		String sql = "insert into NUMERICAL_FORECAST values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] parameters = { UUID.randomUUID().toString(), numericalForecast.getForecastTime(),
				numericalForecast.getWindSpeedList(), numericalForecast.getWindDirectionList(),
				numericalForecast.getWaterSpeedList(), numericalForecast.getWaterDirectionList(),
				numericalForecast.getWaveHighList(), numericalForecast.getWaveDirectionList(),
				numericalForecast.getBdMsg(), numericalForecast.getCreateTime(), numericalForecast.getModel(),
				numericalForecast.getBoradModel(), numericalForecast.getMsgNo(), numericalForecast.getBdNo() };
		sqlHelper.executeUpdate(sql, parameters);
	}

	@Override
	public void deleteNumericalForecast() {
		// TODO Auto-generated method stub
		String sql = "delete from NUMERICAL_FORECAST where model = '1'";
		String[] parameters = {};
		sqlHelper.executeUpdate(sql, parameters);
	}
}
