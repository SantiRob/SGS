package com.sgs.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Valores por defecto (usados en desarrollo local)
    private static final String DEFAULT_DATABASE_HOST = "127.0.0.1";
    private static final String DEFAULT_DATABASE_PORT = "3306";
    private static final String DEFAULT_DATABASE_NAME = "sgs_visits_db";
    private static final String DEFAULT_DATABASE_USER = "root";
    private static final String DEFAULT_DATABASE_PASSWORD = "root";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Obtener configuración de variables de entorno o usar valores por defecto
            String dbHost = System.getenv("DATABASE_HOST") != null ?
                    System.getenv("DATABASE_HOST") : DEFAULT_DATABASE_HOST;

            String dbPort = System.getenv("DATABASE_PORT") != null ?
                    System.getenv("DATABASE_PORT") : DEFAULT_DATABASE_PORT;

            String dbName = System.getenv("DATABASE_NAME") != null ?
                    System.getenv("DATABASE_NAME") : DEFAULT_DATABASE_NAME;

            String dbUser = System.getenv("DATABASE_USER") != null ?
                    System.getenv("DATABASE_USER") : DEFAULT_DATABASE_USER;

            String dbPassword = System.getenv("DATABASE_PASSWORD") != null ?
                    System.getenv("DATABASE_PASSWORD") : DEFAULT_DATABASE_PASSWORD;

            // Construir URL de conexión
            String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName +
                    "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

            // Registrar el driver y conectar
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, dbUser, dbPassword);

            System.out.println("Conexión exitosa a la base de datos: " + dbName + " en " + dbHost);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
        return connection;
    }
}