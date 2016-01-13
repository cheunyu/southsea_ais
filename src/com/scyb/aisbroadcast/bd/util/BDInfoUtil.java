package com.scyb.aisbroadcast.bd.util;

import com.scyb.aisbroadcast.ais.service.IAisMsgForecastService;
import com.scyb.aisbroadcast.ais.service.impl.AisMsgForecastServiceImpl;
import com.scyb.aisbroadcast.ais.util.AscIIConvertBinaryUtil;
import com.scyb.aisbroadcast.ais.util.BroadcastConfig;
import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.bo.LocationInfo;
import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.service.*;
import com.scyb.aisbroadcast.bd.service.impl.AnalyzeComDataServiceImpl;
import com.scyb.aisbroadcast.bd.service.impl.GeneralForecastServiceImpl;
import com.scyb.aisbroadcast.bd.service.impl.LocationInfoServiceImpl;
import com.scyb.aisbroadcast.bd.service.impl.MessageServiceImpl;
import com.scyb.aisbroadcast.bd.service.impl.NumericalForecastServiceImpl;
import com.scyb.aisbroadcast.common.bo.SystemCache;
import com.scyb.aisbroadcast.common.bo.SystemConfig;
import com.scyb.aisbroadcast.common.util.BdSerialController;

import org.apache.log4j.Logger;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Intellij IDEA User:foo Date:2015/7/27 Time:14:31
 */
public class BDInfoUtil {

	private Logger log = Logger.getLogger(this.getClass());
	private BDMsgUtil bdMsgUtil = new BDMsgUtil();
	private IGeneralForecastService generalForecastServiceImpl = new GeneralForecastServiceImpl();
	private INumericalForecastService numericalForecastServiceImpl = new NumericalForecastServiceImpl();
	private ILocationInfoService locationInfoServiceImpl = new LocationInfoServiceImpl();
	private IAnalyzeComDataService analyzeComDataServiceImpl = new AnalyzeComDataServiceImpl();
	private IAisMsgForecastService aisMsgForecastServiceImpl = new AisMsgForecastServiceImpl();
	private IMessageService messageServiceImpl = new MessageServiceImpl();
	private BdSerialController bc = SystemCache.getBdSc();
	private static int beforeMsgNo = 0;

	/**
	 * 鉴定接收的数据指令类别
	 *
	 * @param dataByte
	 *            数据字节
	 */
	public void checkDataType(byte[] dataByte) {
		String dataStr = new String(dataByte);
		if (dataStr.indexOf("TXXX") > -1) {// 通信信息类别
			int clientId = Integer.valueOf(BDComUtil.toFullBinaryString(dataByte[11], 8)
					+ BDComUtil.toFullBinaryString(dataByte[12], 8) + BDComUtil.toFullBinaryString(dataByte[13], 8), 2);
			if ((char) dataByte[19] == 'M') {
				log.info("接收到MMSI信息");
				String dataMsg = new String(dataByte);
				String data[] = dataMsg.substring(18, dataMsg.length() - 1).split(",");
				int count = Integer.parseInt(data[2]);
				int current = Integer.parseInt(data[3]);
				List<String> mmsiList = new ArrayList<String>();
				if (current == 1) {
					BdStatementConnection.getMmsiList().clear();
				} else {
					if (current - 1 == beforeMsgNo) {

					} else {
						return;
					}
				}
				beforeMsgNo = current;
				int contentIndex = 0;
				for (int i = 19; i < dataByte.length - 4; i++) {
					if (dataByte[i] == 0x2C) {
						contentIndex++;
					}
					if (contentIndex > 3) {
						int mmsi = Integer.valueOf(BDComUtil.toFullBinaryString(dataByte[i + 1], 8)
								+ BDComUtil.toFullBinaryString(dataByte[i + 2], 8)
								+ BDComUtil.toFullBinaryString(dataByte[i + 3], 8)
								+ BDComUtil.toFullBinaryString(dataByte[i + 4], 8), 2);
						i = i + 4;
						mmsiList.add(String.valueOf(mmsi));
						log.info(mmsi);
					}
				}
				BdStatementConnection.addMmsiList(mmsiList);
				if (current == count) {
					beforeMsgNo = 0;
				}
			}
			if ((char) dataByte[19] == 'C') {
				log.info("收到一条常规预报.发送方:" + clientId);
				/* 解析常规预报电文，封装数据 */
				GeneralForecast generalForecast = analyzeComDataServiceImpl.analyzeBdGeneralForecast(dataByte);
				/* 手动模式 */
				if ((char) dataByte[20] == 'S') {
					/* 广播模式 */
					if ("8".equals(generalForecast.getBroadModel())) {
						// SystemConfig.setGeneralForecastMsg(aisMsgForecastServiceImpl.generalForecastMsg(generalForecast));
						// generalForecastServiceImpl.sendGeneralForecastPlanService();
						generalForecastServiceImpl.sendGeneralForcastService(generalForecast, 8);
					} else { /* 点对点模式 */
						/* 发送常规预报，6号报文 */
						generalForecastServiceImpl.sendGeneralForcastService(generalForecast, 6);
					}
				} else {
					/* 删除自动常规预报 */
					generalForecastServiceImpl.deleteGeneralForcastService();
				}
				/* 保存常规预报 */
				generalForecastServiceImpl.addGeneralForcastService(generalForecast);
			} else if ((char) dataByte[19] == 'N') {
				log.info("收到一条数值预报.发送方:" + clientId);
				/* 解析数值预报电文，封装数据 */
				NumericalForecast numericalForecast = analyzeComDataServiceImpl.analyzeBdNumericalForecast(dataByte);
				if (numericalForecast != null) {
					if ((char) dataByte[20] == 'S') {
						numericalForecastServiceImpl.sendNumericalForcastService(numericalForecast, 0, Integer.parseInt(numericalForecast.getBoradModel()));
					} else {
						if (Integer.parseInt(numericalForecast.getBdNo()) == 1) {
							numericalForecastServiceImpl.deleteNumericalForcastService();
						}
					}
					log.info(numericalForecast.toString());
					/* 保存数值预报 */
					numericalForecastServiceImpl.addNumericalForcastService(numericalForecast);
				}
			} else if ((char) dataByte[19] == 'U') {
				log.info("收到一条短信息.发送方:" + clientId);
				/* 解析短信息电文，封装数据 */
				Message message = analyzeComDataServiceImpl.analyzeBdMessage(dataByte);
				/* 保存短信息 */
				messageServiceImpl.addMessageService(message);
				/* 发送短信息,多条数据处理完整开始播发 */
				messageServiceImpl.sendMessageService(message);
			} else if ((char) dataByte[19] == 'P') {
				log.info("收到一条位置设置电文.发送方:" + clientId);
				// /* 解析位置设置电文，封装数据 */
				analyzeComDataServiceImpl.analyzeBdLocationInfo(dataByte);
			}
		}
	}
}
