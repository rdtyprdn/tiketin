package com.tiketin.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null){
                throw new RuntimeException("db properties tidak ditemukan di src/main/resources");
            }
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Gagal membaca db.properties", e);
        }
    }


    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            PROPS.getProperty("db.url"),
            PROPS.getProperty("db.user"),
            PROPS.getProperty("db.password"));
    }
}
