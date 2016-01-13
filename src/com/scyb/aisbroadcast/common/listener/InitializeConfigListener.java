package com.scyb.aisbroadcast.common.listener; /**
 * @Title: InitializeConfig.java
 * @Package com.scyb.aisweather.common.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com     
 * @date 2014年7月29日 下午3:58:16    
 * @version V1.0
 */

import com.scyb.aisbroadcast.common.bo.SystemConfig;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * ??? ?@ClassName:?InitializeConfig??? ?@Description:?TODO(初始化加载配置文件)???
 * ?@author?cheunyu?xiaoyuuii@hotmail.com ?@date?2014年7月29日?下午3:58:16?????? ????
 */
public class InitializeConfigListener implements ServletContextListener {

    private Logger log = Logger.getLogger(this.getClass());

    /**
     * ?(非?Javadoc)??? ?
     * <p>
     * Title:?contextDestroyed
     * </p>
     * ?? ?
     * <p>
     * Description:?
     * </p>
     * ?? ?@param arg0??? ?@see
     * javax.servlet.ServletContextListener#contextDestroyed
     * (javax.servlet.ServletContextEvent)???
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * ?(非?Javadoc)??? ?
     * <p>
     * Title:?contextInitialized
     * </p>
     * ?? ?
     * <p>
     * Description:?
     * </p>
     * ?? ?@param arg0??? ?@see
     * javax.servlet.ServletContextListener#contextInitialized
     * (javax.servlet.ServletContextEvent)???
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

//        springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        this.initializedConfig(sce);
//		this.initializedSixBitBinary(sce);
//		this.initializedBaseStation(sce);
//		scanNetCdfServiceImpl = (IScanNetCdfService) springContext.getBean("scanNetCdf");
//		scanNetCdfServiceImpl.scan();
    }

    /**
     * ?@Title:?initializedApp??? ?@Description:?TODO(初始化配置文件)???
     * ?@param?@param sce????设定文件?? ?@return?void????返回类型?? ?@throws???
     */
    public void initializedConfig(ServletContextEvent sce) {
        // TODO Auto-generated method stub
        Properties props = new Properties();
        String configPath = new File(InitializeConfigListener.class.getResource("/").getPath()).getParent() + "/config/config.properties";
        log.info("config.properties loading......");
        try {
            props.load(new FileInputStream(configPath));
            String aisCom = props.getProperty("ais_com");
            String bdCom = props.getProperty("bd_com");
            String aisSocketIp = props.getProperty("ais_socket_ip");
            int aisSocketPort = Integer.parseInt(props.getProperty("ais_socket_port"));
            SystemConfig gv = new SystemConfig();
            gv.setBdCom(bdCom);
            gv.setAisSocketIp(aisSocketIp);
            gv.setAisSocketPort(aisSocketPort);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("config.properties Loaded successfully!");
    }

}
