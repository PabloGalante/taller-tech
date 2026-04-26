package com.tallertech.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton que provee la conexión a MySQL.
 * XAMPP corre MySQL en localhost:3306 por defecto.
 */
public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/tallertech?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER = "root";
    private static final String PASS = "";   // XAMPP no tiene contraseña por defecto

    private static Connection instance;

    private DBConnection() {}

    public static Connection get() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASS);
        }
        return instance;
    }

    public static void close() {
        if (instance != null) {
            try { instance.close(); } catch (SQLException ignored) {}
        }
    }
}
