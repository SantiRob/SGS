package com.sgs.repository;

import com.sgs.util.Env;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    private DatabaseConnection() {}

    public static synchronized Connection getConnection() {
        if (connection == null || isConnectionClosed()) {
            try {
                String url = Env.get("DB_URL");
                String username = Env.get("DB_USER");
                String password = Env.get("DB_PASSWORD");

                // Registrar el driver y conectar
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);

                System.out.println("Nueva conexión establecida a la base de datos en Aiven Cloud");

            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Error al conectar con la base de datos:");
                e.printStackTrace();
            }
        } else {
            System.out.println("Reutilizando conexión existente a la base de datos");
        }
        return connection;
    }

    // Método para verificar si la conexión está cerrada
    private static boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al verificar el estado de la conexión:");
            e.printStackTrace();
            return true;
        }
    }

    // Método para cerrar la conexión (útil al finalizar la aplicación)
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada correctamente");
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión:");
                e.printStackTrace();
            }
        }
    }
}