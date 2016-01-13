package com.scyb.aisbroadcast.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class JdbcUtils {
    
    //连接数据库的参数
    private static String url = null;
    private static String user = null;
    private static String driver = null;
    private static String password = null;
     
    private JdbcUtils () {
 
    }
 
    private static JdbcUtils instance = null;
 
    public static JdbcUtils getInstance() {
        if (instance == null) {
            synchronized (JdbcUtils.class) {
                if (instance == null) {
                    instance = new JdbcUtils();
                }
 
            }
        }
 
        return instance;
    }
     
    //配置文件
    private static Properties prop = new Properties();
    //注册驱动
    static {
        try {
            //利用类加载器读取配置文件
        	prop.load(new FileInputStream(new File(SystemConfigUtil.class.getResource("/").getPath()).getParent()
        			+ "/bin/config/dbInfo.properties"));
//        	prop.load(new FileInputStream(new File("D:/ais/config/dbInfo.properties")));
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            driver = prop.getProperty("driver");
            password = prop.getProperty("password");
             
            Class.forName(driver);
             
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    //该方法获得连接
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
     
    //释放资源
    public void free(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                  
                e.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                          
                        e.printStackTrace();
                    } finally {
                        if (conn != null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                  
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
     
}