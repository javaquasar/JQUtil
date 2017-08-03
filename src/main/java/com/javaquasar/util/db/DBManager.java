package com.javaquasar.util.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Java Quasar
 */
public class DBManager {

    public static List<List<String>> executeQuery(String jdbcUrl, String userName, String password, String driverName, String sql) throws IOException, ClassNotFoundException {
        List<List<String>> dataList = new ArrayList<>();
        Class.forName(driverName);
        try (Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            dataList = convertResultSetToList(rs);
        } catch (SQLException e) {
            // todo
        }
        return dataList;
    }

    private static List<List<String>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<List<String>> dataList = new ArrayList<>();
        dataList.add(new ArrayList<>());
        ResultSetMetaData metaData = rs.getMetaData();
        int countColumn = metaData.getColumnCount();
        for (int i = 1; i <= countColumn; i++) {
            dataList.get(0).add(metaData.getColumnName(i));
        }
        int row = 1;
        while (rs.next()) {
            dataList.add(new ArrayList<>());
            for (int i = 1; i <= countColumn; i++) {
                String result = rs.getString(i);
                dataList.get(row).add(result);
            }
            row++;
        }
        return dataList;
    }
}
