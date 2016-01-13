package com.scyb.aisbroadcast.ais.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.scyb.aisbroadcast.ais.service.IAisMsgForecastService;
import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.LocationInfo;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.service.ILocationInfoService;
import com.scyb.aisbroadcast.bd.service.impl.LocationInfoServiceImpl;
import com.scyb.aisbroadcast.common.util.ConvertUtil;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/4 Time:10:53
 */
public class AisMsgForecastServiceImpl implements IAisMsgForecastService {

	private ConvertUtil convertUtil = new ConvertUtil();
	private ILocationInfoService locationInfoServiceImpl = new LocationInfoServiceImpl();

	@Override
	public String generalForecastMsg(GeneralForecast generalForecast) {
		StringBuffer sb = new StringBuffer();
		//ROUTINE FORECAST IN YONG XING ISLAND
		sb.append("WAVE HIGH:");
		sb.append(generalForecast.getWaveHigh()).append("M,WATER TEMPERATURE:");
		sb.append(generalForecast.getWaterTemperature()).append(",WAVE HIGH:");
		sb.append(generalForecast.getTideHighTime()).append("UTC ");
		sb.append(generalForecast.getTideHigh()).append("CM,WAVE LOW:");
		sb.append(generalForecast.getTideLowTime()).append("UTC ");
		sb.append(generalForecast.getTideLow()).append("CM,MSA");
		return sb.toString();
	}
	
	@Override
	public List<String> numericalForecastMsg(NumericalForecast numericalForecast, int model) {
		StringBuffer sb = new StringBuffer();
		List<String> numericalForecastList = new ArrayList<String>();
		String[] windSpeed = numericalForecast.getWindSpeedList().split(",");
		String[] windDirection = numericalForecast.getWindDirectionList().split(",");
		String[] waterSpeed = numericalForecast.getWaterSpeedList().split(",");
		String[] waterDirection = numericalForecast.getWaterDirectionList().split(",");
		String[] waveHigh = numericalForecast.getWaveHighList().split(",");
		String[] waveDirection = numericalForecast.getWaveDirectionList().split(",");
		LocationInfo locationInfo = locationInfoServiceImpl.getLocationInfoService(model);
		List<String> latitudeList = new ArrayList<String>();
		List<String> longitudeList = new ArrayList<String>();
		String[] latlon = locationInfo.getLatlon().split(",");
		for(int j=0;j<latlon.length;j++) {
			if(j%2 == 0  ) {
				latitudeList.add(latlon[j]);
			}else {
				longitudeList.add(latlon[j]);
			}
		}
		int index = numericalForecast.getWindSpeedList().split(",").length;
		for (int i = 0; i < index; i++) {
//			sb.append("NUMERICAL FORECAST IN N/E ");
			sb.append(numericalForecast.getForecastTime()).append("UTC ");
			sb.append(convertUtil.decConvertDfm(Integer.parseInt(latitudeList.get(i)))).append("N ");
			sb.append(convertUtil.decConvertDfm(Integer.parseInt(longitudeList.get(i)))).append("E ");
			if (windSpeed.length > i) {
				sb.append("FENGSU:").append(windSpeed[i]).append("M/S,");
			}
			if (windDirection.length > i) {
				sb.append("FENGXIANG:").append(windDirection[i]).append(" ");
			}
			if (waterSpeed.length > i) {
				sb.append("LIUSU:").append(waterSpeed[i]).append("M/S,");
			}
			if (waterDirection.length > i) {
				sb.append("LIUXIANG:").append(waterDirection[i]).append(",");
			}
			if (waveHigh.length > i) {
				sb.append("LANGGAO:").append(waveHigh[i]).append("M,");
			}
			if (waveDirection.length > i) {
				sb.append("LANGXIANG:").append(waveDirection[i]).append(" ");
			}
			sb.append("MSA");
			numericalForecastList.add(sb.toString());
			sb.delete(0, sb.length());
		}
		return numericalForecastList;
	}
}
