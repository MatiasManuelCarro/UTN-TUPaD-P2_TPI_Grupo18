/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.integradorfinal.programacion2.config;
/**
 *
 * @author Matias
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection = null;
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                    Config.JDBC_URL,
                    Config.DB_USER,
                    Config.DB_PASS
                );
                System.out.println("✅ Conectado correctamente a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver JDBC no encontrado: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
