package com.scyb.aisbroadcast.bd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.ais.service.IAisMsgForecastService;
import com.scyb.aisbroadcast.ais.service.ISendMessageService;
import com.scyb.aisbroadcast.ais.service.impl.AisMsgForecastServiceImpl;
import com.scyb.aisbroadcast.ais.service.impl.SendMessageServiceImpl;
import com.scyb.aisbroadcast.ais.util.CharacterConvertAisCodeUtil;
import com.scyb.aisbroadcast.ais.util.IEC1371ConvertSerialUtil;
import com.scyb.aisbroadcast.ais.util.SerialDataUtil;
import com.scyb.aisbroadcast.bd.bo.GeneralForecast;
import com.scyb.aisbroadcast.bd.dao.IGeneralForecastDao;
import com.scyb.aisbroadcast.bd.dao.impl.GeneralForecastDaoImpl;
import com.scyb.aisbroadcast.bd.service.IGeneralForecastService;
import com.scyb.aisbroadcast.bd.util.BdStatementConnection;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/31
 * Time:10:57
 */
public class GeneralForecastServiceImpl implements IGeneralForecastService {

    private Logger log = Logger.getLogger(this.getClass());
    private IGeneralForecastDao generalForecastDaoImpl = new GeneralForecastDaoImpl();
    private IAisMsgForecastService aisMsgForecastServiceImpl = new AisMsgForecastServiceImpl();
    private CharacterConvertAisCodeUtil characterConvertAisCodeUtil = new CharacterConvertAisCodeUtil();
    private SerialDataUtil serialDataUtil = new SerialDataUtil();
    private IEC1371ConvertSerialUtil iec1371ConvertSerialUtil = new IEC1371ConvertSerialUtil();
    private ISendMessageService sendMessageServiceImpl = new SendMessageServiceImpl();

    @Override
    public void addGeneralForcastService(GeneralForecast generalForecast) {
        generalForecastDaoImpl.saveGeneralForecast(generalForecast);
    }

    @Override
    public void sendGeneralForcastService(GeneralForecast generalForecast, int msgNumber) {
        String msg = aisMsgForecastServiceImpl.generalForecastMsg(generalForecast);
        log.info(msg);
        String iecCode = iec1371ConvertSerialUtil.conver1371Table44(msg);
        log.info(iecCode);
        List<String> aisMsgList = new ArrayList<String>();
        if(msgNumber==6) {
        	List<String> mmsiList = BdStatementConnection.getMmsiList();
        	for(String mmsi:mmsiList) {
        		aisMsgList.addAll(serialDataUtil.generationABM(iecCode, mmsi));
        	}
        }else {
        	aisMsgList = serialDataUtil.generationBBM(iecCode);
        }
        log.info(aisMsgList.size());
        for(int k=0;k<aisMsgList.size();k++) {
        	log.info(aisMsgList.get(k));
        }
        log.info("播发常规预报");
        sendMessageServiceImpl.sendMessages(aisMsgList);
    }

    @Override
    public void sendGeneralForecastPlanService() {
//        if(!"".equals(SystemConfig.getGeneralForecastMsg())) {
//            String iecCode = characterConvertAisCodeUtil.convertAisCode(SystemConfig.getGeneralForecastMsg());
//            List<String> aisMsgList = serialDataUtil.generationBBM(iecCode);
//            sendMessageServiceImpl.sendMessages(aisMsgList);
//        }
    }

	@Override
	public void deleteGeneralForcastService() {
		// TODO Auto-generated method stub
		 generalForecastDaoImpl.deleteGeneralForecast();
	}
}