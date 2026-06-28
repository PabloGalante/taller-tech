package com.tallertech.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PATRÓN SINGLETON: garantiza una única instancia de conexión
 * a la base de datos durante toda la ejecución del sistema.
 *
 * La instancia se crea una sola vez (lazy initialization) y se
 * reutiliza en todas las llamadas posteriores, evitando el costo
 * de abrir múltiples conexiones innecesarias.
 */
public class DBConnection {

    private static final String URL  =
        "jdbc:mysql://localhost:3306/tallertech" +
        "?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER = "root";
    private static final String PASS = "";

    // Instancia única — núcleo del patrón Singleton
    private static Connection instance;

    // Constructor privado: impide instanciación externa
    private DBConnection() {}

    /**
     * Punto de acceso global a la conexión.
     * Crea la instancia si no existe o si la conexión fue cerrada.
     */
    public static Connection get() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection(URL, USER, PASS);
        }
        return instance;
    }

    public static void close() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
            } catch (SQLException ignored) {}
        }
    }
}
