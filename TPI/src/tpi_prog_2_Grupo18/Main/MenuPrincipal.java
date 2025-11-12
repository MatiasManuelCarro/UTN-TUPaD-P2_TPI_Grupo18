/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Main;

/**
 *
 * @Grupo_18
 */
import java.util.Scanner;
//import prog2int.Dao.DomicilioDAO;
//import prog2int.Dao.PersonaDAO;
//import prog2int.Service.DomicilioServiceImpl;
//import prog2int.Service.PersonaServiceImpl;

public class MenuPrincipal {

    //private final Scanner scanner;
    //private final MenuHandler menuHandler;
  //  private final Scanner scanner;

   // private final MenuHandler menuHandler;

    private boolean running = true;

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (running) {
            MenuPantalla.mostrarMenuPrincipal();
            int opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.println(">> Crear usuario");
                    // UsuarioDAO.insertar()
                    break;
                case 2:
                    System.out.println(">> Mostrar usuarios");
                    // UsuarioDAO.listarTodos()
                    break;
                case 3:
                    System.out.println(">> Actualizar usuario");
                    break;
                case 4:
                    System.out.println(">> Eliminar usuario (baja lógica)");
                    break;
                case 5:
                    System.out.println(">> Crear credencial para usuario");
                    // CredencialDAO.insertar()
                    break;
                case 6:
                    System.out.println(">> Mostrar estado de credenciales");
                    // CredencialDAO.listarMetadatos()
                    break;
                case 7:
                    System.out.println(">> Actualizar credencial por ID de usuario");
                    break;
                case 10:
                    System.out.println(">> Mostrar usuario y su estado de credencial");
                    // JOIN usuario + credencial
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }

        sc.close();
    }

    public static void main(String[] args) {
        MenuPrincipal app = new MenuPrincipal();
        app.run();
    }
}
