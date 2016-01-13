package com.scyb.aisbroadcast.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.TooManyListenersException;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.bd.util.BDComUtil;
import com.scyb.aisbroadcast.bd.util.BDInfoUtil;
import com.scyb.aisbroadcast.bd.util.BDMsgUtil;
import com.scyb.aisbroadcast.ui.MainFrame;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class BdSerialController implements SerialPortEventListener {

	private Logger log = Logger.getLogger(this.getClass());
	private InputStream inputStream;
	private OutputStream outputStream;
	private CommPortIdentifier portId;
	private BDInfoUtil bdInfoUtil = new BDInfoUtil();
	private BDComUtil bdComUtil = new BDComUtil();
	private BDMsgUtil bdMsgUtil = new BDMsgUtil();
	private static int messageNo = 1;
	private static boolean msgFlag = false;

	public void openPort(String comPort, String comRate) {
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(comPort)) {
					try {
						SerialPort serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
						inputStream = serialPort.getInputStream();/* 获取端口的输入流对象 */
						outputStream = serialPort.getOutputStream();
						serialPort.addEventListener(this);
						serialPort.notifyOnDataAvailable(true);
						serialPort.setSerialPortParams(Integer.parseInt(comRate), SerialPort.DATABITS_8,
								SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					} catch (PortInUseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TooManyListenersException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		// TODO Auto-generated method stub
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				byte[] b = new byte[inputStream.available()];
				if (inputStream.available() > 0) {
					inputStream.read(b);
					bdComUtil.dataProcess(b);
					if (bdComUtil.monitor()) {
						if (!bdComUtil.filterData()) {
							byte[] bdMsg = bdComUtil.effectiveData();
							if (bdMsg != null) {
								log.info("有效电文:" + BDComUtil.getHexAndString(bdMsg));
								int tempNo = bdMsgUtil.getMsgNo(bdMsg);
								if (tempNo % 2 == 0) {
									log.info("补发信息");
									if (msgFlag) {
										if (messageNo % 2 == 0) {
											msgFlag = true;
										} else {
											msgFlag = false;
										}
									} else {
										msgFlag = true;
									}
								} else {
									log.info("首发信息");
									msgFlag = true;
								}
								messageNo = tempNo;
								// sendDirect(BDMsgUtil.getTXSQ("R," + tempNo,
								// 455763));
								if (msgFlag) {
									bdInfoUtil.checkDataType(bdMsg);
									byte[] viewData = new byte[bdMsg.length - 21];
									for (int i = 19; i < bdMsg.length - 2; i++) {
										viewData[i - 19] = bdMsg[i];
									}
									String serialData = new String(viewData);
									MainFrame.textArea.append("接收:" + serialData + "\r\n");
									MainFrame.textArea.setCaretPosition(MainFrame.textArea.getText().length());
								}
							}
						}
					}

					// if(bdComUtil.dataComplete(b)) {
					// byte[] bdMsg = bdComUtil.dataByteFilter();
					// log.info(bdMsg.length);
					// bdInfoUtil.checkDataType(bdMsg);
					// byte[] viewData = new byte[bdMsg.length - 21];
					// for(int i=19;i<bdMsg.length - 2;i++) {
					// viewData[i - 19] = bdMsg[i];
					// }
					// String serialData = new String(viewData);
					// MainFrame.textArea.append("接收:" + serialData + "\r\n");
					// MainFrame.textArea.setCaretPosition(MainFrame.textArea.getText().length());
					// }

					// log.info(new String(b));
					// if (dataByte != null) {
					// String serialData = new String(dataByte);
					// MainFrame.textArea.append("接收:" + serialData + "\r\n");
					// MainFrame.textArea.setCaretPosition(MainFrame.textArea.getText().length());
					// bdInfoUtil.checkDataType(dataByte);
					// }
				}
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 直接发送数据
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void sendDirect(byte[] data) throws Exception {
		outputStream.write(data);
		outputStream.flush();
	}

	public static void main(String args[]) {
		BdSerialController sa = new BdSerialController();
		sa.openPort("COM2", "19200");
	}

}