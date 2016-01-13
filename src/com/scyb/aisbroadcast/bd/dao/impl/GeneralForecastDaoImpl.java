package com.scyb.aisbroadcast.bd.dao.impl;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.dao.IGeneralForecastDao;
import com.scyb.aisbroadcast.common.util.JdbcUtils;
import com.scyb.aisbroadcast.common.util.SqlHelper;

import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/30
 * Time:16:27
 */
public class GeneralForecastDaoImpl implements IGeneralForecastDao {

    private Logger log = Logger.getLogger(this.getClass());
    private SqlHelper sqlHelper = new SqlHelper();

    @Override
    public void saveGeneralForecast(GeneralForecast gf) {
    	String sql = "insert into GENERAL_FORECAST values (?,?,?,?,?,?,?,?,?,?,?)";
    	String[] parameters = {UUID.randomUUID().toString(), gf.getWaveHigh(), gf.getWaterTemperature(), gf.getTideHighTime(), gf.getTideHigh(), gf.getTideLowTime(), gf.getTideLow(), gf.getBdMsg(), gf.getCreateTime(), gf.getAutoModel(), gf.getBroadModel()};
    	sqlHelper.executeUpdate(sql, parameters);
    }

	@Override
	public void deleteGeneralForecast() {
		// TODO Auto-generated method stub
		String sql = "delete from GENERAL_FORECAST where auto_model = '1'";
    	String[] parameters = {};
    	sqlHelper.executeUpdate(sql, parameters);
	}
}
