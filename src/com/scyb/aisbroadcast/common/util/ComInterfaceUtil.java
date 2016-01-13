package com.scyb.aisbroadcast.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.common.bo.SystemConfig;
import com.scyb.aisbroadcast.ui.MainFrame;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class ComInterfaceUtil implements SerialPortEventListener {

	private Logger log = Logger.getLogger(this.getClass());
	// 端口是否打开了
	boolean isOpen = false;
	private static SerialPort serialPort = null;

	// private BDInfoUtil bdInfoUtil = new BDInfoUtil();
	public boolean isOpen() {
		return isOpen;
	}

	public List<String> commPortCheck() {
		List<String> comList = new ArrayList<String>();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
			comList.add(portId.getName());
		}
		return comList;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
