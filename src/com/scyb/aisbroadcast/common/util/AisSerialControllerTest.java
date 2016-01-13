package com.scyb.aisbroadcast.common.util;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import com.scyb.aisbroadcast.ui.MainFrame;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * 本类不需要实现为单一实例，但在本BlackBox应用中实现为单一实例， 目的为在BlackBox应用程序中，不运行同时打开多个串口进行操作。
 * 使用方法如下： 1、得到本实例 2、使用 OpenPort打开端口，会自动从配置文件中读取串口参数 3、使用串口进行读写，已经提供读写方法，直接调用就行
 * 4、使用完毕后一定要调用ClosePort关闭端口 注意，OpenPort和ClosePort一定要匹配调用
 * 
 * 本类不支持发送和接收的并行进行，也就是说单次的发送过程中不允许读操作， 单次的读过程中不允许发送数据
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class AisSerialControllerTest implements SerialPortEventListener {
	private CommPortIdentifier portId;
	private SerialPort serialPort = null;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String comPort = "1";
	private boolean recieveStop = true;

	// 缺省8N1
	private int dataBit = SerialPort.DATABITS_8;
	private int parity = SerialPort.PARITY_NONE;
	private int stopBit = SerialPort.STOPBITS_1;

	public void setParity(int parity) {
		this.parity = parity;
	}

	public void setStopBit(int stopBit) {
		this.stopBit = stopBit;
	}

	public boolean isRecieveStop() {
		return recieveStop;
	}

	public void setRecieveStop(boolean recieveStop) {
		this.recieveStop = recieveStop;
	}

	public void setDataBit(int dataBit) {
		this.dataBit = dataBit;
	}

	public String getComPort() {
		return comPort;
	}

	/**
	 * 打开串行端口，串口参数会从配置文件中读取，其中串口名称一定从1开始， 原则上不限定上限，但根据操作系统要求，打开端口可能不成功。
	 * 本类的实现由于要求单一实例，因此在此方法中不判断端口是否打开，在后续 的实现中可考虑，如果先前端口已经打开是进行复用还是关闭先前的端口然后重新
	 * 打开端口。
	 * 
	 * @throws java.lang.Exception
	 *             但出现错误时会抛出异常
	 */
	@SuppressWarnings("unchecked")
	public void OpenPort(String comPort, String comRate) throws Exception {
		// 查找指定的端口是否被系统支持
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		boolean portavil = false;
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(comPort)) {
					portavil = true;
					break;
				}
			}
		}
		if (!portavil) {
			portId = null;
			throw new Exception("No such Port:" + comPort);
		}
		// 下面的代码为打开串口，处理speed可以参数化外其它部分不应做改动
		serialPort = (SerialPort) portId.open("SerialController", 6000);
		// serialPort.notifyOnDataAvailable(true);
		serialPort.setInputBufferSize(10 * 1024);
		serialPort.setOutputBufferSize(10 * 1024);// 10k
		serialPort.notifyOnCTS(false);
		serialPort.notifyOnDSR(false);

		inputStream = serialPort.getInputStream();
		outputStream = serialPort.getOutputStream();
		serialPort.setSerialPortParams(Integer.parseInt(comRate), dataBit, stopBit, parity);
		serialPort.addEventListener(this);
		this.comPort = comPort;
		serialPort.setEndOfInputChar((byte) 0x00);
		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		System.out.println("打开串口");
	}

	/**
	 * 关闭先前已经打开的端口，如果端口没有打开，则此方法无作用
	 */
	public void closePort() {
		if (serialPort != null) {
			serialPort.notifyOnDataAvailable(false);
			serialPort.removeEventListener();
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (IOException e) {
				}
			}
			serialPort.close();
			serialPort = null;
		}
	}

	public boolean isOpened() {

		if (portId == null) {
			return false;
		} else
			return portId.isCurrentlyOwned();
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

	/**
	 * 根据CTS控制进行数据发送，此方法将根据CTS状态进行发送，而不是真正的硬件流控制
	 * 如果在硬件上短接CTS/RTS端，则此方法的结果和sendDirect(byte[] data)类似 请在硬件电缆线上注意接口的连接
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void sendCheck(byte[] data) throws Exception {
		long start = System.currentTimeMillis();
		int offset = 0;
		int length = data.length;
		while (true) {
			if (!serialPort.isCTS()) {
				if (System.currentTimeMillis() - start > 3000)
					throw new Exception("发送数据到设备超时");
				Thread.sleep(5);
			} else {
				outputStream.write(data, offset++, 1);
				outputStream.flush();
				if (--length <= 0)
					break;
				start = System.currentTimeMillis();
			}
		}

	}

	public boolean hasDataInBuffer() throws Exception {
		return inputStream.available() > 0;
	}

	/**
	 * 读取指定长度的数据
	 * 
	 * @param length
	 *            要读取的数据长度
	 * @param timeOut
	 *            硬件读取超时时间
	 * @return
	 * @throws Exception
	 */
	public byte[] recieve(int length, int timeOut) throws Exception {
		if (timeOut < 0)
			timeOut = 0;
		if (timeOut > 0)
			serialPort.enableReceiveTimeout(timeOut + 100);// 故意多设置100毫秒
		else
			serialPort.enableReceiveTimeout(Integer.MAX_VALUE);// FIXME:不激活超时,当设置为0时，下面的read方法可能不能读到数据，这是串口驱动的问题，此处进行屏蔽
		long start = System.currentTimeMillis();
		byte[] buffer = new byte[length];
		int rc = 0;
		int recieved = 0;
		int remain = length;
		while (remain > 0) {
			int avail = inputStream.available();
			// LogManager.logInfo("aaa","start read avail: "+avail);
			if (avail > 0 && avail <= remain)
				rc = inputStream.read(buffer, recieved, avail);
			else
				rc = inputStream.read(buffer, recieved, remain);
			// LogManager.logInfo("aaa","try read avail: "+avail+" recv: "+rc+"
			// timeOut: "+timeOut);

			if (rc < 0)
				throw new Exception("Read Stream Closed!");

			remain -= rc;
			recieved += rc;

			if (remain == 0)
				break;// 如果已经读完，则退出
			// 否则判断超时
			if (timeOut > 0) {
				if (rc > 0)
					start = System.currentTimeMillis();
				else {
					if ((System.currentTimeMillis() - start) > timeOut)
						throw new Exception("读数据超时!");
				}
			}

		}
		return buffer;

	}

	/**
	 * 根据提供的结尾字符来读取数据包，读取过程的数据如果遇到endCodes中的任一个字符，则读取结束
	 * 
	 * @param endCodes
	 *            结束字符
	 * @param timeOut
	 *            超时时间
	 * @return
	 * @throws Exception
	 */
	public byte[] recieve(byte[] endCodes, int maxCheck, int timeOut) throws Exception {
		if (endCodes.length == 0)
			throw new Exception("not set end codes");
		if (timeOut < 0)
			timeOut = 0;
		ByteBuffer bb = new ByteBuffer();
		boolean keepReading = true;
		int count = 0;
		long start = System.currentTimeMillis();
		while (keepReading) {
			byte[] data = new byte[1];
			int rc = inputStream.read(data);
			if (rc < 0)
				throw new Exception("Read Stream Closed!");
			if (timeOut > 0) {
				if (rc > 0)
					start = System.currentTimeMillis();
				if (rc == 0)
					if (System.currentTimeMillis() - start > timeOut)
						throw new Exception("读数据超时!");
			}
			bb.append(data);
			for (int i = 0; i < endCodes.length; i++) {
				if (endCodes[i] == data[0]) {
					keepReading = false;
					break;
				}
			}
			if (count++ > maxCheck)
				throw new Exception("没有能够检测到结尾数据，检查次数：" + maxCheck);
		}
		return bb.getValue();
	}

	public byte[] recieve(byte startCode, byte[] endCodes, int maxCheck, int timeOut) throws Exception {
		if (endCodes.length == 0)
			throw new Exception("not set end codes");
		if (timeOut < 0)
			timeOut = 0;
		serialPort.enableReceiveTimeout(timeOut);
		ByteBuffer bb = new ByteBuffer();
		boolean keepReading = true;
		boolean started = false;
		int count = 0;
		long start = System.currentTimeMillis();
		while (keepReading) {
			byte[] data = new byte[1];
			int rc = inputStream.read(data);
			if (rc < 0)
				throw new Exception("Read Stream Closed!");
			if (timeOut > 0) {
				if (rc > 0)
					start = System.currentTimeMillis();
				if (rc == 0)
					if (System.currentTimeMillis() - start > timeOut)
						throw new Exception("读数据超时!");
			}
			if (data[0] == startCode) {
				started = true;
				count = 0;
			}
			if (!started) {
				if (count++ > maxCheck)
					throw new Exception("没有能够检测到开始数据，检查次数：" + maxCheck);
			} else {
				bb.append(data);
				for (int i = 0; i < endCodes.length; i++) {
					if (endCodes[i] == data[0]) {
						keepReading = false;
						break;
					}
				}
				if (count++ > maxCheck)
					throw new Exception("没有能够检测到开始数据，检查次数：" + maxCheck);
			}
		}
		return bb.getValue();

	}

	/**
	 * 读取数据
	 * 
	 * @param length
	 *            要读取的数据长度
	 * @param timeOut
	 *            硬件读取超时时间
	 * @return
	 * @throws Exception
	 */
	public byte[] recieve() throws Exception {
		while (recieveStop) {
			byte[] b = new byte[inputStream.available()];
			if (b.length > 0) {
				inputStream.read(b);
				MainFrame.textAreaAis.append("接收:" + new String(b) + "\r\n");
//				System.out.print(new String(b));
			}
			// inputStream.close();
		}
		return null;
	}

	/**
	 * 清除缓存中的未读数据
	 * 
	 * @throws Exception
	 */
	public void clearReadBuffer() throws Exception {
		inputStream.skip(inputStream.available());
	}

	/**
	 * 此方法为内部实现调用方法，外部不应调用，此处不作说明，如需 改动，请在相关位置添加代码，但一般不要对DATA_AVAILABLE部分
	 * 
	 * @param event
	 */
	public void serialEvent(SerialPortEvent event) {
		System.out.println("1");
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
			break;
		case SerialPortEvent.CTS:
			System.out.println("recieved the device CTS:" + event.getNewValue());
			break;
		case SerialPortEvent.DSR:
			System.out.println("recieved the device DSR:" + event.getNewValue());
			break;
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			System.out.println("recieved the device DATA_AVAILABLE:" + event.getNewValue());
			break;
		}
	}

}
