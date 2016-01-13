package com.scyb.aisbroadcast.common.util;

/**
 *  IO口管理工具类
 * */
public class IOManagerUtil {

	private static boolean aisSocket = false;
	private static boolean aisCom = false;
	private static boolean bdCom = false;
	
	public IOManagerUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static boolean isAisSocket() {
		return aisSocket;
	}
	public static void setAisSocket(boolean aisSocket) {
		IOManagerUtil.aisSocket = aisSocket;
	}
	public static boolean isAisCom() {
		return aisCom;
	}
	public static void setAisCom(boolean aisCom) {
		IOManagerUtil.aisCom = aisCom;
	}
	public static boolean isBdCom() {
		return bdCom;
	}
	public static void setBdCom(boolean bdCom) {
		IOManagerUtil.bdCom = bdCom;
	}
	
	
	
}
