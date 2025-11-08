package com.action.headline.util;


import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    private static DataSource dataSource;

    // 初始化连接池
    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = JDBCUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* 获取数据源 */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /* 获取数据库连接 */
    public static Connection getConnection() {
        Connection connection = threadLocal.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            threadLocal.set(connection);
        }
        return connection;
    }


    /* 将连接归还给连接池 */
    public static void releaseConnection() {
        Connection connection = threadLocal.get();
        if (null != connection) {
            threadLocal.remove();
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
