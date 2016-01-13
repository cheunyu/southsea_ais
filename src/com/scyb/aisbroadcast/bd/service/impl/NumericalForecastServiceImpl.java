package com.scyb.aisbroadcast.bd.service.impl;

import com.scyb.aisbroadcast.ais.service.IAisMsgForecastService;
import com.scyb.aisbroadcast.ais.service.ISendMessageService;
import com.scyb.aisbroadcast.ais.service.impl.AisMsgForecastServiceImpl;
import com.scyb.aisbroadcast.ais.service.impl.SendMessageServiceImpl;
import com.scyb.aisbroadcast.ais.util.CharacterConvertAisCodeUtil;
import com.scyb.aisbroadcast.ais.util.IEC1371ConvertSerialUtil;
import com.scyb.aisbroadcast.ais.util.NMEA0183ConvertBinaryUtil;
import com.scyb.aisbroadcast.ais.util.SerialDataUtil;
import com.scyb.aisbroadcast.bd.bo.NumericalForecast;
import com.scyb.aisbroadcast.bd.dao.INumericalForecastDao;
import com.scyb.aisbroadcast.bd.dao.impl.NumericalForecastDaoImpl;
import com.scyb.aisbroadcast.bd.service.INumericalForecastService;
import com.scyb.aisbroadcast.bd.util.BdStatementConnection;
import com.scyb.aisbroadcast.common.bo.SystemConfig;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/4 Time:10:42
 */
public class NumericalForecastServiceImpl implements INumericalForecastService {

	private INumericalForecastDao numericalForecastDaoImpl = new NumericalForecastDaoImpl();
	private IAisMsgForecastService aisMsgForecastServiceImpl = new AisMsgForecastServiceImpl();
	private CharacterConvertAisCodeUtil characterConvertAisCodeUtil = new CharacterConvertAisCodeUtil();
	private NMEA0183ConvertBinaryUtil nmeaUtil = new NMEA0183ConvertBinaryUtil();
	private SerialDataUtil serialDataUtil = new SerialDataUtil();
	private ISendMessageService sendMessageServiceImpl = new SendMessageServiceImpl();
	private IEC1371ConvertSerialUtil iec1371ConvertSerialUtil = new IEC1371ConvertSerialUtil();
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public void addNumericalForcastService(NumericalForecast numericalForecast) {
		numericalForecastDaoImpl.saveNumericalForecast(numericalForecast);
	}

	@Override
	public void sendNumericalForcastService(NumericalForecast numericalForecast, int model, int msgNumber) {
		List<String> numericalForecastList = aisMsgForecastServiceImpl.numericalForecastMsg(numericalForecast, model);
		for (int i = 0; i < numericalForecastList.size(); i++) {
			log.info(numericalForecastList.get(i));
			String iecCode = iec1371ConvertSerialUtil.conver1371Table44(numericalForecastList.get(i));
			log.info(iecCode);
			// String iecCode =
			// characterConvertAisCodeUtil.convertAisCode(numericalForecastList.get(i));
			List<String> aisMsgList = new ArrayList<String>();
			if (msgNumber == 6) {
				List<String> mmsiList = BdStatementConnection.getMmsiList();
				for (String mmsi : mmsiList) {
					aisMsgList.addAll(serialDataUtil.generationABM(iecCode, mmsi));
				}
			} else {
				aisMsgList = serialDataUtil.generationBBM(iecCode);
			}
			sendMessageServiceImpl.sendMessages(aisMsgList);
		}
	}

	@Override
	public void sendNumericalForecastPlanService() {
		// if (SystemConfig.getNumericalForecastMsgList().size() > 0) {
		// for (int i = 0; i <
		// SystemConfig.getNumericalForecastMsgList().size(); i++) {
		// String iecCode =
		// characterConvertAisCodeUtil.convertAisCode(SystemConfig.getNumericalForecastMsgList().get(i));
		// List<String> aisMsgList = serialDataUtil.generationBBM(iecCode);
		// sendMessageServiceImpl.sendMessages(aisMsgList);
		// }
		// }
	}

	@Override
	public void deleteNumericalForcastService() {
		// TODO Auto-generated method stub
		numericalForecastDaoImpl.deleteNumericalForecast();
	}
}
