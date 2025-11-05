package com.action.headline.dao;


import com.action.headline.util.JDBCUtil;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {


    /**
     * 查询单个对象（通用方法）
     * 用于执行返回单个结果的 SQL 查询，例如：COUNT(*)、SUM()、AVG() 等聚合函数
     */
    public <T> T baseQueryObject(Class<T> clazz, String sql, Object... args) {
        printSQL(sql, args);

        T result = null;
        Connection connection = JDBCUtil.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = (T) resultSet.getObject(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            JDBCUtil.releaseConnection();
        }
        return result;
    }


    /**
     * 查询对象列表（通用方法）
     */
    public <T> List<T> baseQuery(Class<T> clazz, String sql, Object... args) {
        printSQL(sql, args);

        List<T> list = new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (resultSet.next()) {
                    T obj = clazz.getDeclaredConstructor().newInstance();

                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnLabel(i);
                        Object value = resultSet.getObject(columnName);

                        if (value != null) {
                            if (value.getClass().equals(LocalDateTime.class)) {
                                value = Timestamp.valueOf((LocalDateTime) value);
                            }

                            Field field = clazz.getDeclaredField(columnName);
                            field.setAccessible(true);
                            field.set(obj, value);
                        }
                    }

                    list.add(obj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            JDBCUtil.releaseConnection();
        }
        return list;
    }

    /**
     * 通用的增删改方法
     * 用于执行 INSERT、UPDATE、DELETE 等数据修改操作
     */
    public int baseUpdate(String sql, Object... args) {
        printSQL(sql, args);

        Connection connection = JDBCUtil.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            JDBCUtil.releaseConnection();
        }
    }

    public void printSQL(String sql, Object... args) {
        System.out.println("==> SQL: " + sql);
        if (args.length > 0) {
            System.out.print("==> Parameters: [");
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }
}
