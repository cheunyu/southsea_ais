package com.scyb.aisbroadcast.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.scyb.aisbroadcast.common.bo.SystemConfig;

public class SocketAisManager {

	private static Socket socket;
	
	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		SocketAisManager.socket = socket;
	}

	public static void initialized() {
		try {
			socket = new Socket(SystemConfig.getAisSocketIp(), SystemConfig.getAisSocketPort());
			// 权限验证
			char b[] = { 0x01, 0x77, 0x73, 0x6D, 0x00, 0x36, 0x30, 0x31, 0x32, 0x30, 0x32, 0x00 };
            // 向服务器端发送数据
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(b);
            pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
