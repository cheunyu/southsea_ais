package com.scyb.aisbroadcast.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.scyb.aisbroadcast.common.bo.SystemConfig;

public class SystemConfigUtil {

	private static Logger log = Logger.getLogger("SystemConfigUtil");
	private static Properties props = new Properties();

	/**
	 * 加载配置文件
	 */
	public static void loadConfigFile() {
		String configPath = new File(SystemConfigUtil.class.getResource("/").getPath()).getParent()
				+ "/bin/config/config.properties";
//		String configPath = "D:/ais/config/config.properties";
		try {
			props.load(new FileInputStream(configPath));
			SystemConfig.setBdCom(props.getProperty("bd_com"));
			SystemConfig.setBdComRate(props.getProperty("bd_comrate"));
			SystemConfig.setAisSocketIp(props.getProperty("ais_socket_ip"));
			SystemConfig.setAisSocketPort(Integer.parseInt(props.getProperty("ais_socket_port")));
			SystemConfig.setAisCom(props.getProperty("ais_com"));
			SystemConfig.setAisComRate(props.getProperty("ais_comrate"));
			SystemConfig.setDefaultConnection(Integer.parseInt(props.getProperty("defaultConnection")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存配置文件
	 */
	public static void saveProperties() {
		String configPath = new File(SystemConfigUtil.class.getResource("/").getPath()).getParent()
				+ "/bin/config/config.properties";
		try {
			props.load(new FileInputStream(configPath));
			props.setProperty("bd_com", SystemConfig.getBdCom());
			props.setProperty("bd_comrate", SystemConfig.getBdComRate());
			props.setProperty("ais_socket_ip", SystemConfig.getAisSocketIp());
			props.setProperty("ais_socket_port", String.valueOf(SystemConfig.getAisSocketPort()));
			props.setProperty("ais_com", SystemConfig.getAisCom());
			props.setProperty("ais_comrate", SystemConfig.getAisComRate());
			props.setProperty("defaultConnection", String.valueOf(SystemConfig.getDefaultConnection()));
			FileOutputStream fos = new FileOutputStream(new File(configPath));
			props.store(fos, "the primary key of article table");
//			props.setProperty("ais_socket_ip", SystemConfig.getAisSocketIp());
//			props.setProperty("ais_socket_port", String.valueOf(SystemConfig.getAisSocketPort()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
