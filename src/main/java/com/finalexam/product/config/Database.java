package com.finalexam.product.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Tao ket noi JDBC. Uu tien DB_URL, DB_USER, DB_PASSWORD neu co. */
public final class Database {
    private static final Properties PROPERTIES = loadProperties();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Khong tim thay MySQL JDBC Driver: " + e.getMessage());
        }
    }

    private Database() {
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Khong tim thay src/main/resources/db.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Khong doc duoc cau hinh co so du lieu", e);
        }
        return properties;
    }

    private static String config(String env, String property) {
        String value = System.getenv(env);
        return value != null ? value : PROPERTIES.getProperty(property, "");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config("DB_URL", "db.url"),
                config("DB_USER", "db.username"),
                config("DB_PASSWORD", "db.password")
        );
    }
}