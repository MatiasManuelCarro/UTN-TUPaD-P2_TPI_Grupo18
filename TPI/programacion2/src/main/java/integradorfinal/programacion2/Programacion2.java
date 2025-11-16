package integradorfinal.programacion2;

import integradorfinal.programacion2.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

/**
 
 * Prueba la conexión con la base de datos tpi_prog_2.
 */
public class Programacion2 {

    public static void testConnection() {
        System.out.println("Recordar modificar el Archivo db.resources en src\\main\\resources "
                + "para que la aplicacion conecte a la base de datos");
        System.out.println("Para mas info, leer el README");
        System.out.println("Iniciando prueba de conexión...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión exitosa al esquema tpi_prog_2");
            } else {
                System.err.println(" No se pudo establecer la conexión.");
            }
        } catch (SQLException e) {
            // Mensaje amigable para el usuario
            System.err.println("No se pudo conectar con la base de datos. Verifique configuración o intente más tarde.");
            // Detalle técnico para el desarrollador (puede ir a logs)
            System.err.println("Detalles técnicos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado al intentar conectar.");
            System.err.println("Detalles técnicos: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}