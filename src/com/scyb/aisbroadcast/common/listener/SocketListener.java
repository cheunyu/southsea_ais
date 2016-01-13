package com.scyb.aisbroadcast.common.listener;

import com.scyb.aisbroadcast.common.bo.SystemConfig;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with Intellij IDEA
 * User:foo
 * Date:2015/7/31
 * Time:10:36
 */
public class SocketListener implements ServletContextListener {

    private Logger log = Logger.getLogger(this.getClass());
    private ServletContextEvent sce;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String baseStationIp = SystemConfig.getAisSocketIp();
        int baseStationPort = SystemConfig.getAisSocketPort();
        try {
            Socket socket = new Socket(baseStationIp, baseStationPort);
            char b[] = { 0x01, 0x77, 0x73, 0x6D, 0x00, 0x36, 0x30, 0x31, 0x32, 0x30, 0x32, 0x00 };
            // 向服务器端发送数据
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(b);
            pw.flush();
            servletContextEvent.getServletContext().setAttribute("aisSocket", socket);
            log.info("Ais岸基" + baseStationIp + ":" + baseStationPort + "已连接." );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
