package com.example.student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/college?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";

    // CHANGE THIS
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
