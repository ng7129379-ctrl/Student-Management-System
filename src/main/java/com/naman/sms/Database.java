package com.naman.sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    // Set these environment variables on your computer before running the app.
    // Do not commit real database passwords to GitHub.
    private static final String URL = System.getenv().getOrDefault(
             "SMS_DB_URL", "jdbc:mysql://localhost:3306/student_management");
    private static final String USER = System.getenv().getOrDefault("SMS_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("SMS_DB_PASSWORD", "");

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
