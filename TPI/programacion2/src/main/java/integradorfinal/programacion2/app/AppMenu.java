package integradorfinal.programacion2.app;

import integradorfinal.programacion2.entities.Usuario;
import integradorfinal.programacion2.entities.CredencialAcceso;
import integradorfinal.programacion2.entities.Estado;
import integradorfinal.programacion2.service.UsuarioService;
import integradorfinal.programacion2.service.CredencialAccesoService;
import integradorfinal.programacion2.service.impl.UsuarioServiceImpl;
import integradorfinal.programacion2.service.impl.CredencialAccesoServiceImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Esta clase es el menú principal de mi aplicación de consola.
 * Desde acá arranco el programa, muestro las opciones y llamo a los servicios
 * de Usuario y Credencial para hacer todo el CRUD y las operaciones
 * transaccionales que me piden en el TFI.
 */
public class AppMenu {

    // Scanner que uso en toda la clase para leer las entradas por consola.
    private final Scanner sc = new Scanner(System.in);

    // Servicio de Usuario: lo uso para toda la lógica de negocio de usuarios.
    private final UsuarioService usuarioService = new UsuarioServiceImpl();

    // Servicio de Credencial: acá centralizo la lógica de las credenciales.
    private final CredencialAccesoService credService = new CredencialAccesoServiceImpl();

    // Punto de entrada de la aplicación. Arranco creando un AppMenu y llamando a run().
    public static void main(String[] args) {
        new AppMenu().run();
    }

    /**
     * Método principal del menú.
     * Acá muestro las opciones, leo lo que el usuario elige y disparo cada caso.
     * El do/while mantiene la app viva hasta que elija la opción 0 (Salir).
     */
    public void run() {
        int op;
        do {
            mostrarMenu();
            op = leerInt("Opcion");
            try {
                // Uso switch con sintaxis mejorada (Java 14+) para cada opción del menú.
                switch (op) {
                    case 1 ->
                        crearUsuarioSimple();
                    case 2 ->
                        listarUsuarios();
                    case 3 ->
                        verUsuarioPorId();
                    case 4 ->
                        buscarUsuarioPorUsername();
                    case 5 ->
                        buscarUsuarioPorEmail();
                    case 6 ->
                        actualizarUsuario();
                    case 7 ->
                        bajaLogicaUsuario();
                    case 8 ->
                        bajaFisicaUsuario();
                    case 9 ->
                        crearUsuarioConCredencialTx();
                    case 10 ->
                        crearCredencialParaUsuario();
                    case 11 ->
                        verCredencialPorUsuarioId();
                    case 12 ->
                        verCredencialPorId();
                    case 13 ->
                        actualizarPasswordSeguro();
                    case 14 ->
                        loginUsuario();
                    case 15 ->
                        demoRollbackMenu();
                    case 0 ->
                        System.out.println("Saliendo...");
                    default ->
                        System.out.println("Opción invalida.");
                }
            } catch (Exception e) {
                // Centralizo acá el manejo de cualquier excepción que se dispare en las operaciones.
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        } while (op != 0);
    }

    /**
     * Muestro por consola todas las opciones disponibles del TFI
     * para que el usuario elija qué operación quiere ejecutar.
     */
    private void mostrarMenu() {
        System.out.println("========= MENÚ TFI (Usuario / Credencial) =========");
        System.out.println(" 1) Crear Usuario (simple)");
        System.out.println(" 2) Listar Usuarios");
        System.out.println(" 3) Ver Usuario por ID");
        System.out.println(" 4) Buscar Usuario por NOMBRE DE USUARIO");
        System.out.println(" 5) Buscar Usuario por EMAIL");
        System.out.println(" 6) Actualizar Usuario");
        System.out.println(" 7) Eliminar Usuario (baja lógica)");
        System.out.println(" 8) Eliminar Usuario (baja física)");
        System.out.println(" 9) Crear Usuario + Credencial (TRANSACCIÓN)");
        System.out.println("10) Crear Credencial para Usuario existente");
        System.out.println("11) Ver Credencial por usuarioId");
        System.out.println("12) Ver Datos de Credencial por ID (de la credencial)");
        System.out.println("13) Actualizar contraseña (stored procedure)");
        System.out.println("14) Login de Usuario (validar contraseña)");
        System.out.println("15) PRUEBA DE ROLLBACK (Solo para demostracion)");
        System.out.println(" 0) Salir");
    }

    // ===================== USUARIO =====================

    /**
     * Creo un usuario básico (sin credencial) y lo guardo usando el service.
     * Acá solamente armo el objeto Usuario con los datos que leo por consola.
     */
    private void crearUsuarioSimple() throws SQLException {
        Usuario u = new Usuario();
        u.setEliminado(false); // inicio siempre como no eliminado (baja lógica en falso)
        u.setUsername(leerStr("Username"));
        u.setNombre(leerStr("Nombre"));
        u.setApellido(leerStr("Apellido"));
        u.setEmail(leerStr("Email"));
        u.setFechaRegistro(LocalDateTime.now()); // uso la fecha/hora actual del sistema
        u.setActivo(true); // por defecto lo creo activo
        u.setEstado(leerEstado()); // pido ACTIVO/INACTIVO y lo convierto al enum

        Long id = usuarioService.create(u);
        System.out.println("Usuario creado con id=" + id);
    }

    /**
     * Listo todos los usuarios que obtengo del service.
     * Si la lista viene vacía, informo que no hay usuarios.
     */
    private void listarUsuarios() throws SQLException {
        List<Usuario> lista = usuarioService.findAll();
        if (lista.isEmpty()) {
            System.out.println("(sin usuarios)");
            return;
        }
        // Uso method reference para imprimir cada usuario con su toString().
        lista.forEach(System.out::println);
    }

    /**
     * Pido un ID y muestro los datos del usuario si existe,
     * usando Optional para manejar el "no encontrado".
     */
    private void verUsuarioPorId() throws SQLException {
        long id = leerLong("ID de usuario");
        Optional<Usuario> u = usuarioService.findById(id);
        System.out.println(u.map(Object::toString).orElse("Usuario no encontrado"));
    }

    /**
     * Busco un usuario por su username (campo único) y muestro el resultado.
     */
    private void buscarUsuarioPorUsername() throws SQLException {
        String username = leerStr("Username");
        Optional<Usuario> u = usuarioService.findByUsername(username);
        System.out.println(u.map(Object::toString).orElse("(Usuario no encontrado)"));
    }

    /**
     * Busco un usuario por email (campo único) y muestro el resultado.
     */
    private void buscarUsuarioPorEmail() throws SQLException {
        String email = leerStr("Email");
        Optional<Usuario> u = usuarioService.findByEmail(email);
        System.out.println(u.map(Object::toString).orElse("(Usuario no encontrado)"));
    }

    /**
     * Actualizo los datos de un usuario existente.
     * Primero lo traigo por ID, muestro lo que tiene, y después,
     * campo por campo, pregunto si quiero cambiarlo (enter = lo dejo igual).
     */
    private void actualizarUsuario() throws SQLException {
        long id = leerLong("ID de usuario");
        Optional<Usuario> opt = usuarioService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("(no existe ese usuario)");
            return;
        }
        Usuario u = opt.get();
        System.out.println("Editando: " + u);

        // Para cada campo permito cargar un nuevo valor o dejar el actual.
        String username = leerStrOpc("Nuevo username (enter para dejar igual)");
        if (!username.isBlank()) {
            u.setUsername(username);
        }

        String nombre = leerStrOpc("Nuevo nombre (enter para dejar igual)");
        if (!nombre.isBlank()) {
            u.setNombre(nombre);
        }

        String apellido = leerStrOpc("Nuevo apellido (enter para dejar igual)");
        if (!apellido.isBlank()) {
            u.setApellido(apellido);
        }

        String email = leerStrOpc("Nuevo email (enter para dejar igual)");
        if (!email.isBlank()) {
            u.setEmail(email);
        }

        String activoStr = leerStrOpc("Activo? (S/N, enter para dejar igual)");
        if (!activoStr.isBlank()) {
            u.setActivo(activoStr.equalsIgnoreCase("S"));
        }

        String estStr = leerStrOpc("Estado (ACTIVO/INACTIVO, enter para dejar igual)");
        if (!estStr.isBlank()) {
            u.setEstado(Estado.from(estStr));
        }

        usuarioService.update(u);
        System.out.println("Usuario actualizado.");
    }

    /**
     * Realizo una baja lógica del usuario: marco el flag "eliminado" en true
     * (la lógica concreta está dentro del service/DAO).
     */
    private void bajaLogicaUsuario() throws SQLException {
        long id = leerLong("ID de usuario a dar de baja (logica)");
        usuarioService.softDeleteById(id);
        System.out.println("Usuario marcado como eliminado.");
    }

    /**
     * Realizo una baja física del usuario: borro el registro de la base.
     * Esta operación es más destructiva, por eso la separo explícitamente.
     */
    private void bajaFisicaUsuario() throws SQLException {
        long id = leerLong("ID de usuario a eliminar (fisico)");
        usuarioService.deleteById(id);
        System.out.println(" Usuario eliminado físicamente.");
    }

    /**
     * Crea un nuevo Usuario junto con su CredencialAcceso en una única transacción.
     *
     * En este método:
     * - Primero armo el objeto Usuario con los datos que leo por consola.
     * - Después creo la CredencialAcceso asociada, con su estado y campos básicos.
     * - Asocio la credencial al usuario (u.setCredencial(c)).
     * - Finalmente delego toda la lógica transaccional al usuarioService,
     *   que se encarga de hacer commit o rollback según corresponda.
     *
     * @throws SQLException si ocurre un error al guardar en la base de datos.
     */
    private void crearUsuarioConCredencialTx() throws SQLException {
        // Usuario
        Usuario u = new Usuario();
        u.setEliminado(false);
        u.setUsername(leerStr("Username"));
        u.setNombre(leerStr("Nombre"));
        u.setApellido(leerStr("Apellido"));
        u.setEmail(leerStr("Email"));
        u.setFechaRegistro(LocalDateTime.now());
        u.setActivo(true);
        u.setEstado(leerEstado());

        // Credencial
        CredencialAcceso c = new CredencialAcceso();
        c.setEliminado(false);
        c.setEstado(Estado.ACTIVO);
        // En esta opción transaccional dejo el password tal cual lo ingresa el usuario
        // asumiendo que el hash se aplica en otra capa o es solo demostrativo.
        c.setHashPassword(leerStr("Password (se guardara el hash SHA-256)"));
        c.setSalt("manual"); // acá setteo un salt fijo solo a modo de ejemplo
        c.setUltimoCambio(LocalDateTime.now());
        c.setRequiereReset(false);
        // El usuarioService se encarga luego de persistir Usuario y Credencial
        // garantizando la relación 1→1 y la transacción.
        u.setCredencial(c);

        Long nuevoId = usuarioService.createUsuarioConCredencial(u);
        System.out.println("Transaccion OK. Usuario id=" + nuevoId + " + credencial creada.");
    }

    // ===================== CREDENCIAL =====================

    /**
     * Creo una credencial para un usuario que ya existe.
     * Acá sí genero un salt aleatorio y calculo el hash SHA-256 de la contraseña
     * usando PasswordUtil, para que quede guardada de manera más segura.
     */
    private void crearCredencialParaUsuario() throws SQLException {
        long usuarioId = leerLong("Usuario ID para asociar credencial");
        String passwordPlano = leerStr("Password (se guardará el hash SHA-256)");

        // Genero un salt aleatorio de 16 bytes para esta credencial.
        String salt = integradorfinal.programacion2.util.PasswordUtil.generateSalt(16);

        // Calculo el hash mezclando la contraseña con el salt.
        String hash = integradorfinal.programacion2.util.PasswordUtil.hashPassword(passwordPlano, salt);

        CredencialAcceso c = new CredencialAcceso();
        c.setEliminado(false);
        c.setUsuarioId(usuarioId);
        c.setEstado(Estado.ACTIVO);
        c.setUltimaSesion(null);
        c.setHashPassword(hash);   // guardo el hash ya calculado
        c.setSalt(salt);           // guardo el salt que generé recién
        c.setUltimoCambio(LocalDateTime.now());
        c.setRequiereReset(false);

        Long id = credService.create(c);
        System.out.println("Credencial creada con id=" + id);
    }

    /**
     * Busco una credencial por su ID propio (de la tabla credenciales)
     * y muestro su contenido si existe.
     */
    private void verCredencialPorId() throws SQLException {
        long id = leerLong("ID de credencial");
        Optional<CredencialAcceso> c = credService.findById(id);
        System.out.println(c.map(Object::toString).orElse("(no encontrada)"));
    }

    /**
     * Busco la credencial asociada a un usuario a partir del usuarioId.
     */
    private void verCredencialPorUsuarioId() throws SQLException {
        long usuarioId = leerLong("Usuario ID");
        Optional<CredencialAcceso> c = credService.findByUsuarioId(usuarioId);
        System.out.println(c.map(Object::toString).orElse("(no encontrada)"));
    }

    /**
     * Actualizo la contraseña de un usuario usando un stored procedure en la base.
     * De esta forma centralizo la lógica de cambio de password del lado de la BD.
     */
    private void actualizarPasswordSeguro() throws SQLException {
        long usuarioId = leerLong("Usuario ID");
        String nuevoPassword = leerStr("Nuevo password (se guardará hash SHA-256)");
        // El tercer parámetro "IGNORAR" es un placeholder según la firma del SP.
        credService.updatePasswordSeguro(usuarioId, nuevoPassword, "IGNORAR");
        System.out.println("Password actualizada vía stored procedure.");
    }

    // ===================== Helpers de entrada =====================

    /**
     * Helper para leer un String obligatorio.
     * Si el usuario deja vacío, vuelvo a pedir hasta que ingrese algo.
     */
    private String leerStr(String label) {
        System.out.print(label + ": ");
        String s = sc.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("Requerido. " + label + ": ");
            s = sc.nextLine();
        }
        return s.trim();
    }

    /**
     * Helper para leer un String opcional.
     * Si el usuario simplemente aprieta enter, devuelvo cadena vacía.
     */
    private String leerStrOpc(String label) {
        System.out.print(label + ": ");
        String s = sc.nextLine();
        return s == null ? "" : s.trim();
    }

    /**
     * Helper genérico para leer un int desde consola.
     * Atrapo el NumberFormatException y vuelvo a pedir hasta que ingrese un número válido.
     */
    private int leerInt(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                String s = sc.nextLine();
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    /**
     * Helper para leer un long desde consola con validación básica del formato numérico.
     */
    private long leerLong(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                String s = sc.nextLine();
                return Long.parseLong(s.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número valido (long).");
            }
        }
    }

    /**
     * Helper para leer el estado del usuario como enum.
     * Sólo acepto "ACTIVO" o "INACTIVO" (no sensible a mayúsculas/minúsculas).
     */
    private Estado leerEstado() {
        while (true) {
            String s = leerStr("Estado (ACTIVO/INACTIVO)").toUpperCase();
            if (s.equals("ACTIVO") || s.equals("INACTIVO")) {
                return Estado.valueOf(s);
            }
            System.out.println("Valor invalido. Use ACTIVO o INACTIVO.");
        }
    }

    /**
     * Simulo el proceso de login:
     * - Busco al usuario por username.
     * - Busco su credencial asociada.
     * - Valido la contraseña ingresada contra el hash almacenado usando salt.
     * - Si todo está OK, actualizo la fecha de última sesión.
     */
    private void loginUsuario() throws SQLException {
        System.out.println("=== LOGIN DE USUARIO ===");

        String username = leerStr("Username");
        String passwordIngresada = leerStr("Password");

        // 1. Buscar usuario por username
        Optional<Usuario> optUser = usuarioService.findByUsername(username);
        if (optUser.isEmpty()) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        Usuario u = optUser.get();

        // 2. Buscar credencial asociada al usuario
        Optional<CredencialAcceso> optCred = credService.findByUsuarioId(u.getIdUsuario());
        if (optCred.isEmpty()) {
            System.out.println("No hay credencial asociada a este usuario.");
            return;
        }

        CredencialAcceso cred = optCred.get();

        // 3. Validar password ingresada contra hash y salt usando PasswordUtil.
        boolean ok = integradorfinal.programacion2.util.PasswordUtil.validatePassword(
                passwordIngresada,
                cred.getSalt(),
                cred.getHashPassword()
        );

        if (ok) {
            System.out.println("Login exitoso. Bienvenido, " + u.getNombre() + "!");
            // Actualizo la última sesión del usuario.
            cred.setUltimaSesion(LocalDateTime.now());
            credService.update(cred); // persisto el cambio
        } else {
            System.out.println("Contraseña incorrecta.");
        }
    }

    /**
     * Método para demostrar el rollback en una operación transaccional.
     * Llamo a usuarioService.demoRollback() que internamente fuerza un error
     * para que se dispare el rollback y así poder mostrarlo en el video del TFI.
     */
    private void demoRollbackMenu() throws SQLException {
        System.out.println("=== DEMO ROLLBACK (error simulado) ===");
        System.out.println("Antes de la demo, verificá en MySQL cuántos usuarios tenés.");

        try {
            usuarioService.demoRollback();
            System.out.println("Después de la demo, volvés a MySQL y comprobás que NO hay usuarios nuevos.");
        } catch (SQLException e) {
            System.out.println("La operación falló y se revirtió completamente.");
        }
    }

}
