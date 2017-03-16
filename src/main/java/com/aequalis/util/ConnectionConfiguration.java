package com.aequalis.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfiguration {
    public static final String URL = "jdbc:mysql://192.168.1.22:3306/slingschemaupdated";
    /**
     * In my case username is "root" *
     */
    public static final String USERNAME = "root";
    /**
     * In my case password is "1234" *
     */
    public static final String PASSWORD = "root";
 
    public static Connection getConnection() {
        Connection connection = null;
 
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return connection;
    }
 
}