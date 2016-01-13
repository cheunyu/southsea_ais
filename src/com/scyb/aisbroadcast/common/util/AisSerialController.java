package com.scyb.aisbroadcast.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.ui.MainFrame;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class AisSerialController implements SerialPortEventListener {

	private InputStream inputStream;
	private OutputStream outputStream;
	private CommPortIdentifier portId;
	public static final int PARAMS_DELAY = 200; // 延时等待端口数据准备的时间
	private Logger log = Logger.getLogger(this.getClass());
	private String comPort;
	private String comRate;
	
	

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public String getComRate() {
		return comRate;
	}

	public void setComRate(String comRate) {
		this.comRate = comRate;
	}

	public void openPort(String comPort, String comRate) {
		this.comPort = comPort;
		this.comRate = comRate;
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
		try {
			Thread.sleep(PARAMS_DELAY);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				byte[] b = new byte[inputStream.available()];
				if (inputStream.available() > 0) {
					inputStream.read(b);
					String serialData = new String(b);
					MainFrame.textAreaAis.append(serialData);
					if(serialData.indexOf("ABK")>0) {
						String arr[] = serialData.split(",");
						int status = 9;
						String mmsi = null;
						for(int i=0;i<arr.length;i++) {
							if(arr[i].indexOf("ABK")>0) {
								status = Integer.parseInt((arr[i+5].substring(0, 1)));
								mmsi = arr[i+1];
							} 
						}
						if(status==0) {
							MainFrame.textAreaAis.append("收到信息回执MMSI:" + mmsi +"成功接收");
							log.info("收到信息回执MMSI:" + mmsi +"成功接收");
							Thread.sleep(1000*60*60);
						}else if(status==1) {
							MainFrame.textAreaAis.append("收到信息回执MMSI:" + mmsi +"没有确认");
							log.info("收到信息回执MMSI:" + mmsi +"没有确认");
						}else if(status==2) {
							MainFrame.textAreaAis.append("收到信息回执MMSI:" +mmsi +"播发失败");
							log.info("收到信息回执MMSI:" + mmsi +"播发失败");
						}
						
					}
//					if(serialData.indexOf("ABK")>0) {
//						if("6".equals(serialData.split(",")[3])) {
//							if("0".equals(serialData.split(",")[5])) {
//								Map<Integer, String> informationMap = AisAbkCache.getAbmInformationIdMap();
//								informationMap.remove(serialData.split(",")[5]);
//							}
//						}else if("8".equals(serialData.split(",")[3])){
//							if("3".equals(serialData.split(",")[5])) {
//								Map<Integer, String> informationMap = AisAbkCache.getBbmInformationIdMap();
//								informationMap.remove(serialData.split(",")[5]);
//							}
//						}
//					}
					if (serialData.equals("\r")) {
						MainFrame.textAreaAis.append("\r");
					}
					MainFrame.textAreaAis.setCaretPosition(MainFrame.textAreaAis.getText().length());
				}
				inputStream.close();
			} catch (IOException | InterruptedException e) {
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

}