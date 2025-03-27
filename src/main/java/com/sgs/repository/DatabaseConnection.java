package com.sgs.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_NAME = "sgs_visits_db";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar con la base de datos:");
            e.printStackTrace();
        }
        return connection;
    }
}
