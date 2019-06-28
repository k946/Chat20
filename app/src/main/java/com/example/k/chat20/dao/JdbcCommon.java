package com.example.k.chat20.dao;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JdbcCommon {

    static {
        try {
            //1. 加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //System.out.println("驱动加载成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@xx.xx.xx.xx:1521:chat", "username","passwd");
            //System.out.println("获取链接成功！！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;

    }

    public static void closeConnection(Connection conn) {
        try {
            if(null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
