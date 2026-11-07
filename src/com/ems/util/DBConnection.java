package com.ems.util;

import com.ems.exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides JDBC connections to MySQL.
 * Credentials can be overridden with environment variables
 * EMS_DB_URL, EMS_DB_USER and EMS_DB_PASSWORD.
 */
public final class DBConnection {

    private static final String URL = getEnv("EMS_DB_URL",
            "jdbc:mysql://localhost:3306/employee_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    private static final String USER = getEnv("EMS_DB_USER", "root");
    private static final String PASSWORD = getEnv("EMS_DB_PASSWORD", "Shravya@2005");

    private DBConnection() { }

    private static String getEnv(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DataAccessException("Could not connect to the database: " + e.getMessage(), e);
        }
    }
}
