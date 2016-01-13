package com.scyb.aisbroadcast.bd.service.impl;

import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.LocationInfo;
import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.service.IAnalyzeComDataService;
import com.scyb.aisbroadcast.bd.service.ILocationInfoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.scyb.aisbroadcast.bd.util.BDComUtil;
import com.scyb.aisbroadcast.bd.util.BDMsgUtil;
import com.scyb.aisbroadcast.common.bo.SystemConfig;

import org.apache.log4j.Logger;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/4 Time:11:13
 */
public class AnalyzeComDataServiceImpl implements IAnalyzeComDataService {

	private Logger log = Logger.getLogger(this.getClass());
	private ILocationInfoService locationInfoServiceImpl = new LocationInfoServiceImpl();
	private BDComUtil bdComUtil = new BDComUtil();
	private BDMsgUtil bdMsgUtil = new BDMsgUtil();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static StringBuffer latlonSb = new StringBuffer();
	private static StringBuffer hexSb = new StringBuffer();

	private static StringBuffer numericalMsgNo = new StringBuffer();
	// 拼接多条语句中的气象数据
	private static List<String> weatherDataList = new ArrayList<String>();
	private static StringBuffer numericalHex = new StringBuffer();
	private static StringBuffer windSpeed = new StringBuffer();
	private static StringBuffer windDirection = new StringBuffer();
	private static StringBuffer waterSpeed = new StringBuffer();
	private static StringBuffer waterDirection = new StringBuffer();
	private static StringBuffer waveSpeed = new StringBuffer();
	private static StringBuffer waveDirection = new StringBuffer();

	@Override
	public GeneralForecast analyzeBdGeneralForecast(byte[] bytes) {
		String dataMsg = new String(bytes);
		String data[] = dataMsg.substring(19, dataMsg.length() - 2).split(",");
		GeneralForecast generalForecast = new GeneralForecast();
		// 播发模式判断
		if (data[2].equals("0")) { // 广播模式
			generalForecast.setBroadModel("8");
		} else {// 点对点模式
			generalForecast.setBroadModel("6");
		}
		generalForecast.setWaveHigh(data[3]);
		generalForecast.setWaterTemperature(data[4]);
		generalForecast.setTideHighTime(data[5]);
		generalForecast.setTideHigh(data[6]);
		generalForecast.setTideLowTime(data[7]);
		generalForecast.setTideLow(data[8]);
		generalForecast.setBdMsg(dataMsg.substring(18, dataMsg.length() - 2));
		generalForecast.setCreateTime(sdf.format(new Date()));
		if (bytes[20] == 'S') {
			// 手动播发模式
			generalForecast.setAutoModel("0");
		} else {
			// 自动播发模式
			generalForecast.setAutoModel("1");
		}
		log.info(generalForecast.toString());
		return generalForecast;
	}

	@Override
	public NumericalForecast analyzeBdNumericalForecast(byte[] bytes) {
		String dataMsg = new String(bytes);
		String data[] = dataMsg.substring(19, dataMsg.length() - 2).split(",");
		NumericalForecast numericalForecast = new NumericalForecast();
		// 手动自动判断
		if ((char) bytes[20] == 'S') {
			numericalForecast.setModel("0");
		} else {
			numericalForecast.setModel("1");
		}
		// 播发模式判断
		if (data[2].equals("0")) { // 广播模式
			numericalForecast.setBoradModel("8");
		} else {// 点对点模式
			numericalForecast.setBoradModel("6");
		}
		// 当前时间点数
		numericalForecast.setBdNo(String.valueOf(bdMsgUtil.convHexToDec(data[4])));
		log.info(dataMsg);
		String a4Data = dataMsg.substring(19, dataMsg.length() - 2);
		int splitIndex = 0;
		int timeBeginIndex = 0;
		// 循环电文数据第19位开始每个字节
		log.info(a4Data);
		for (int j = 0; j < a4Data.length(); j++) {
			// 判断是否为分隔符
			if (a4Data.substring(j, j + 1).equals(",")) {
				splitIndex++;
			}
			// 在第七次出现分隔符的位置即时间数据起始位置
			if (splitIndex == 7) {
				timeBeginIndex = j + 1 + 19;
				if(a4Data.indexOf("N")!=0) {
					timeBeginIndex = timeBeginIndex+1;
				}
				break;
			}
		}
		log.info(timeBeginIndex);
		long time = 0l;
		String timeBin = "";
		// 时间数据中是否包含分隔符标示
		boolean timeFlag = false;
		for (int m = timeBeginIndex; m < timeBeginIndex + 4; m++) {
			timeBin = timeBin + bdComUtil.toFullBinaryString(bytes[m], 8);
			if (bytes[m] == 0x2C) { // 如果时间数据中包含分隔符,气象数据以分隔符分组+1位
				timeFlag = true;
			}
		}
		time = Integer.valueOf(timeBin, 2);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time * 1000);
		numericalForecast.setForecastTime(sdf.format(cal.getTime()));
		// 气象数据起始位
		int dataIndex = 0;
		if (timeFlag) {
			dataIndex = 9;
		} else {
			dataIndex = 8;
		}
		for (int j = dataIndex; j < data.length; j++) {
			weatherDataList.add(data[j]);
		}
		int listSize = weatherDataList.size();
		if (listSize % 6 != 0) {
			for (int i = 0; i < 6 - listSize % 6; i++) {
				weatherDataList.add("");
			}
		}
		numericalHex.append(bdComUtil.getHexAndString(bytes));
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		numericalForecast.setCreateTime(sdf.format(cal1.getTime()));
		// 判断当前电文是一条完整电文的最后一条
		if (bdMsgUtil.convHexToDec(data[5]) == bdMsgUtil.convHexToDec(data[6])) {
			log.info("数值预报接收完整");
			numericalMsgNo.append(String.valueOf(bdMsgUtil.getMsgNo(bytes)));
			numericalForecast.setBdMsg(numericalHex.toString());
			numericalHex.delete(0, numericalHex.length());
			// 北斗电文序号
			numericalForecast.setMsgNo(numericalMsgNo.toString());
			numericalMsgNo.delete(0, numericalMsgNo.length());
			for (int k = 0; k < weatherDataList.size() / 6; k++) {
				windSpeed.append(weatherDataList.get(k * 6 + 0));
				windDirection.append(weatherDataList.get(k * 6 + 1));
				waterSpeed.append(weatherDataList.get(k * 6 + 2));
				waterDirection.append(weatherDataList.get(k * 6 + 3));
				waveSpeed.append(weatherDataList.get(k * 6 + 4));
				waveDirection.append(weatherDataList.get(k * 6 + 5));
				if (k != weatherDataList.size() / 6 - 1) {
					windSpeed.append(",");
					windDirection.append(",");
					waterSpeed.append(",");
					waterDirection.append(",");
					waveSpeed.append(",");
					waveDirection.append(",");
				}
			}
			weatherDataList.clear();
			numericalForecast.setWindSpeedList(windSpeed.toString());
			numericalForecast.setWindDirectionList(windDirection.toString());
			numericalForecast.setWaterSpeedList(waterSpeed.toString());
			numericalForecast.setWaterDirectionList(waterDirection.toString());
			numericalForecast.setWaveHighList(waveSpeed.toString());
			numericalForecast.setWaveDirectionList(waveDirection.toString());
			windSpeed.delete(0, windSpeed.length());
			windDirection.delete(0, windDirection.length());
			waterSpeed.delete(0, waterSpeed.length());
			waterDirection.delete(0, waterDirection.length());
			waveSpeed.delete(0, waveSpeed.length());
			waveDirection.delete(0, waveDirection.length());
			log.info(numericalForecast.toString());
			return numericalForecast;
		} else {// 当前电文是一条完整电文的一部分
			numericalMsgNo.append(String.valueOf(bdMsgUtil.getMsgNo(bytes))).append(",");
			numericalHex.append(",");
			return null;
		}
	}

	@Override
	public void analyzeBdLocationInfo(byte[] bytes) {
		LocationInfo li = new LocationInfo();
		String dataMsg = new String(bytes);
		String[] dataArr = dataMsg.substring(0, dataMsg.length() - 2).split(",");
		log.info(dataMsg.substring(0, dataMsg.length() - 1));
		li.setMsgNo(String.valueOf(bdMsgUtil.getMsgNo(bytes)));
		latlonSb.delete(0, latlonSb.length());
		if ((char) bytes[20] == 'S') {
			li.setModel("0");
			// 判断短信编号为1时，清空位置信息缓存
			if (Integer.parseInt(dataArr[6]) == 1) {
				SystemConfig.getManuallyLocationList().clear();
				hexSb.delete(0, hexSb.length());
			}
			hexSb.append(bdComUtil.getHexAndString(bytes)).append(",");
			for (int i = 0; i < (dataArr.length - 6) / 2; i++) {
				SystemConfig.getManuallyLocationList().add(dataArr[7 + i * 2] + "," + dataArr[8 + i * 2]);
			}
			for (int m = 0; m < SystemConfig.getManuallyLocationList().size(); m++) {
				log.info(SystemConfig.getManuallyLocationList().get(m));
				latlonSb.append(SystemConfig.getManuallyLocationList().get(m));
				if (m != SystemConfig.getManuallyLocationList().size() - 1) {
					latlonSb.append(",");
				}
			}
		} else {
			SystemConfig.setGeneralTime(Integer.parseInt(dataArr[2]));
			SystemConfig.setNumericalTime(Integer.parseInt(dataArr[3]));
			li.setModel("1");
			// 判断短信编号为1时，清空位置信息缓存
			if (Integer.parseInt(dataArr[6]) == 1) {
				SystemConfig.getAutoLocationList().clear();
				hexSb.delete(0, hexSb.length());
			}
			hexSb.append(bdComUtil.getHexAndString(bytes)).append(",");
			for (int i = 0; i < (dataArr.length - 6) / 2; i++) {
				SystemConfig.getAutoLocationList().add(dataArr[7 + i * 2] + "," + dataArr[8 + i * 2]);
			}
			for (int m = 0; m < SystemConfig.getAutoLocationList().size(); m++) {
				log.info(SystemConfig.getAutoLocationList().get(m));
				latlonSb.append(SystemConfig.getAutoLocationList().get(m));
				if (m != SystemConfig.getAutoLocationList().size() - 1) {
					latlonSb.append(",");
				}
			}
		}
		if (latlonSb.toString().split(",").length / 2 == bdMsgUtil.convHexToDec(dataArr[4])) {
			li.setLatlon(latlonSb.toString());
			li.setHexMsg(hexSb.toString());
			Calendar cal = Calendar.getInstance();
			li.setCreateTime(sdf.format(cal.getTime()));
			/* 保存数值预报 */
			locationInfoServiceImpl.addLocationInfoService(li);
		}
	}

	@Override
	public Message analyzeBdMessage(byte[] bytes) {
		String dataMsg = new String(bytes);
		String data[] = dataMsg.substring(19, dataMsg.length() - 2).split(",");
		Message message = new Message();
		message.setMsgNo(data[1]);
		message.setModel(data[2]);
		message.setMsgHex(bdComUtil.getHexAndString(bytes));
		message.setContent(data[3]);
		message.setCreateTime(sdf.format(new Date()));
		return message;
	}
}
