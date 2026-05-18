package edu.ccrm.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:app-data/ccrm.db";

    public static Connection getConnection() throws SQLException {
        try {
            java.nio.file.Path dataDir = java.nio.file.Paths.get("app-data");
            if (!java.nio.file.Files.exists(dataDir)) {
                java.nio.file.Files.createDirectories(dataDir);
            }
        } catch (Exception e) {
            System.err.println("Failed to create app-data directory.");
        }
        return DriverManager.getConnection(DB_URL);
    }
}