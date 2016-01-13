package com.scyb.aisbroadcast.bd.service.impl;

import com.scyb.aisbroadcast.ais.service.ISendMessageService;
import com.scyb.aisbroadcast.ais.service.impl.SendMessageServiceImpl;
import com.scyb.aisbroadcast.ais.util.CharacterConvertAisCodeUtil;
import com.scyb.aisbroadcast.ais.util.SerialDataUtil;
import com.scyb.aisbroadcast.bd.bo.Message;
import com.scyb.aisbroadcast.bd.dao.IMessageDao;
import com.scyb.aisbroadcast.bd.dao.impl.MessageDaoImpl;
import com.scyb.aisbroadcast.bd.service.IMessageService;
import com.scyb.aisbroadcast.bd.util.BdStatementConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/17 Time:10:47
 */
public class MessageServiceImpl implements IMessageService {

	private IMessageDao messageDaoImpl = new MessageDaoImpl();
	private CharacterConvertAisCodeUtil characterConvertAisCodeUtil = new CharacterConvertAisCodeUtil();
	private ISendMessageService sendMessageServiceImpl = new SendMessageServiceImpl();
	private SerialDataUtil serialDataUtil = new SerialDataUtil();

	@Override
	public void addMessageService(Message message) {
		messageDaoImpl.saveMessage(message);
	}

	@Override
	public void sendMessageService(Message message) {
		String iecCode = characterConvertAisCodeUtil.convertAisCode(message.getContent());
		List<String> aisMsgList = new ArrayList<String>();
		if ("0".equals(message.getModel())) {
			aisMsgList = serialDataUtil.generationBBM(iecCode);
			sendMessageServiceImpl.sendMessages(aisMsgList);
		} else {
			List<String> mmsiList = BdStatementConnection.getMmsiList();
			for (String mmsi : mmsiList) {
				aisMsgList = serialDataUtil.generationABM(iecCode, mmsi);
				sendMessageServiceImpl.sendMessages(aisMsgList);
			}

		}

	}
}
