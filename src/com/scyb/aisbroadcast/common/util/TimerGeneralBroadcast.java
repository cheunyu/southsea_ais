package com.scyb.aisbroadcast.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.service.IGeneralForecastService;
import com.scyb.aisbroadcast.bd.service.INumericalForecastService;
import com.scyb.aisbroadcast.bd.service.impl.GeneralForecastServiceImpl;
import com.scyb.aisbroadcast.bd.service.impl.NumericalForecastServiceImpl;
import com.scyb.aisbroadcast.common.bo.SystemConfig;
import com.sun.javafx.runtime.SystemProperties;

public class TimerGeneralBroadcast {

	private Logger log = Logger.getLogger(this.getClass());
	private Timer timer;
	private SqlHelper sqlHelper = new SqlHelper();
	private IGeneralForecastService generalForecastServiceImpl = new GeneralForecastServiceImpl();
	private INumericalForecastService numericalForecastServiceImpl = new NumericalForecastServiceImpl();

	public TimerGeneralBroadcast() {
		timer = new Timer();
		timer.schedule(new GeneralForecastTask(), 1000, 1000 * 1);
	}

	public class GeneralForecastTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000 * 60 * SystemConfig.getGeneralTime());
				this.gfMessageTask();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void gfMessageTask() {
			String sql = "select * from general_forecast where auto_model = '1' order by create_time asc";
			String[] param = {};
			ArrayList<Object> rsList = sqlHelper.executeQuery(sql, param);
			Calendar cal = Calendar.getInstance();
			Calendar dataCal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			GeneralForecast gf = new GeneralForecast();
			for (int i = 0; i < rsList.size(); i++) {
				Object[] objects = (Object[]) rsList.get(i);
				gf.setGuid((String) objects[0]);
				gf.setWaveHigh((String) objects[1]);
				gf.setWaterTemperature((String) objects[2]);
				gf.setTideHighTime((String) objects[3]);
				gf.setTideHigh((String) objects[4]);
				gf.setTideLowTime((String) objects[5]);
				gf.setTideLow((String) objects[6]);
				gf.setBdMsg((String) objects[7]);
				gf.setCreateTime((String) objects[8]);
				gf.setAutoModel((String) objects[9]);
				gf.setBroadModel((String) objects[10]);
				try {
					dataCal.setTime(sdf.parse(gf.getCreateTime()));
					if(cal.after(dataCal)) {
						log.info(gf.getCreateTime());
					}else {
						break;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			generalForecastServiceImpl.sendGeneralForcastService(gf, 8);
		}
	}

	public static void main(String[] args) {
		new TimerGeneralBroadcast();
	}
}
