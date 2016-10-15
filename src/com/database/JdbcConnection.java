package com.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by lgluo on 2016/10/15.
 */
public class JdbcConnection {
    private static Properties p;
    private static ThreadLocal<Connection> threadLocal;

    public JdbcConnection() {

    }

    public static Connection getConnectino() {
        try {
            Class.forName(p.getProperty("driver"));
            Connection e = threadLocal.get();
            if(e == null) {
                e = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
                threadLocal.set(e);
            }
            return e;
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }

    public static void close(Statement psmt) {
        if(psmt != null) {
            try {
                psmt.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void close(ResultSet rs, Statement psmt) {
        if(rs != null) {
            try {
                rs.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        if(psmt != null) {
            try {
                psmt.close();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    public static void closeConnection() {
        Connection conn = threadLocal.get();
        if(conn != null) {
            threadLocal.set(null);

            try {
                conn.close();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

    }

    static {
        try {
            p = new Properties();
            p.load(JdbcConnection.class.getResourceAsStream("/database.properties"));
        } catch (IOException var1) {
            var1.printStackTrace();
        }

        threadLocal = new ThreadLocal();
    }
}


