package com.scyb.aisbroadcast.ais.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.ais.service.ISendMessageService;
import com.scyb.aisbroadcast.common.bo.SystemCache;
import com.scyb.aisbroadcast.common.util.AisSerialController;
import com.scyb.aisbroadcast.common.util.IOManagerUtil;
import com.scyb.aisbroadcast.common.util.SocketAisManager;
import com.scyb.aisbroadcast.ui.MainFrame;

/**
 * Created with Intellij IDEA User:foo Date:2015/8/4 Time:9:08
 */
public class SendMessageServiceImpl implements ISendMessageService {

	private Logger log = Logger.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void sendMessages(List<String> aisMsgList) {
		if (IOManagerUtil.isAisSocket()) {
			Socket socket = SocketAisManager.getSocket();
			// 向服务器端发送数据
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(socket.getOutputStream());
				for (int i = 0; i < aisMsgList.size(); i++) {
					MainFrame.textAreaAis
							.append(sdf.format(Calendar.getInstance().getTime()) + "    " + aisMsgList.get(i) + "\r\n");
					pw.println(aisMsgList.get(i));
					pw.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (IOManagerUtil.isAisCom()) {
			AisSerialController aisSc = SystemCache.getAisSc();
			try {
				for (int i = 0; i < aisMsgList.size(); i++) {
					Thread.sleep(3000);
					MainFrame.textAreaAis.append(
							"发送:" + sdf.format(Calendar.getInstance().getTime()) + "    " + aisMsgList.get(i) + "\r\n");
					log.info(aisMsgList.get(i));
					aisSc.sendDirect(aisMsgList.get(i).getBytes());
					MainFrame.textAreaAis.setCaretPosition(MainFrame.textAreaAis.getText().length());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
