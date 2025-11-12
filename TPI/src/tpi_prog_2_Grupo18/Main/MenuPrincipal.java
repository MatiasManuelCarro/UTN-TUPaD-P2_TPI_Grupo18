/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Main;

/**
 *
 * @Grupo_18
 */
import java.sql.Connection;
import java.util.Scanner;
import tpi_prog_2_Grupo18.Config.DatabaseConnection;

public class MenuPrincipal {

    private boolean running = true;

    public void run() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Instanciamos el handler con la conexión
            MenuHandler handler = new MenuHandler(conn);
            Scanner sc = new Scanner(System.in);

            while (running) {
                MenuPantalla.mostrarMenuPrincipal(); // imprime el menú
                int opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                // Delegamos la opción al handler
                handler.ejecutarOpcion(opcion);

                if (opcion == 0) {
                    running = false;
                }
            }

            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MenuPrincipal app = new MenuPrincipal();
        app.run();
    }
}
