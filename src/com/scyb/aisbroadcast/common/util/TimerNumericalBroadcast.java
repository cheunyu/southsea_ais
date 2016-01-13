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

public class TimerNumericalBroadcast {

	private Logger log = Logger.getLogger(this.getClass());
	private Timer timer;
	private SqlHelper sqlHelper = new SqlHelper();
	private INumericalForecastService numericalForecastServiceImpl = new NumericalForecastServiceImpl();

	public TimerNumericalBroadcast() {
		timer = new Timer();
		timer.schedule(new NumericalForecastTask(), 1000, 1000 * 1);
	}

	public class NumericalForecastTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000 * 60 * SystemConfig.getNumericalTime());
				this.nfMessageTask();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void nfMessageTask() {
			String sql = "select * from numerical_forecast t where t.model = '1' order by t.forecast_time asc";
			String[] pm = {};
			ArrayList<Object> rsList = sqlHelper.executeQuery(sql, pm);
			Calendar cal = Calendar.getInstance();
			Calendar dataCal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			NumericalForecast nf = new NumericalForecast();
			for (int i = 0; i < rsList.size(); i++) {
				Object[] objects = (Object[]) rsList.get(i);
				nf.setForecastTime((String) objects[1]);
				try {
					dataCal.setTime(sdf.parse(nf.getForecastTime()));
					if (cal.after(dataCal)) {
						log.info(nf.getForecastTime());
					} else {
						objects = (Object[]) rsList.get(i - 1);
						nf.setGuid((String) objects[0]);
						nf.setForecastTime((String) objects[1]);
						nf.setWindSpeedList((String) objects[2]);
						nf.setWindDirectionList((String) objects[3]);
						nf.setWaterSpeedList((String) objects[4]);
						nf.setWaterDirectionList((String) objects[5]);
						nf.setWaveHighList((String) objects[6]);
						nf.setWaveDirectionList((String) objects[7]);
						nf.setBdMsg((String) objects[8]);
						nf.setCreateTime((String) objects[9]);
						nf.setModel((String) objects[10]);
						break;
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			numericalForecastServiceImpl.sendNumericalForcastService(nf, 1, 8);
		}
	}

	public static void main(String[] args) {
		new TimerNumericalBroadcast();
	}
}
