package integradorfinal.programacion2.service.impl;

import integradorfinal.programacion2.config.DatabaseConnection;
import integradorfinal.programacion2.dao.CredencialAccesoDao;
import integradorfinal.programacion2.dao.UsuarioDao;
import integradorfinal.programacion2.dao.impl.CredencialAccesoDaoImpl;
import integradorfinal.programacion2.dao.impl.UsuarioDaoImpl;
import integradorfinal.programacion2.entities.CredencialAcceso;
import integradorfinal.programacion2.entities.Estado;
import integradorfinal.programacion2.entities.Usuario;
import integradorfinal.programacion2.service.UsuarioService;
import integradorfinal.programacion2.util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Capa de negocio para Usuario. Orquesta DAO + transacciones (commit/rollback).
 */
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private final CredencialAccesoDao credencialDao;

    // Inyección simple por defecto
    public UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
        this.credencialDao = new CredencialAccesoDaoImpl();
    }

    // (Opcional) Inyección por constructor para tests
    public UsuarioServiceImpl(UsuarioDao usuarioDao, CredencialAccesoDao credencialDao) {
        this.usuarioDao = usuarioDao;
        this.credencialDao = credencialDao;
    }

    // ================================
    // CRUD (delegan en DAO)
    // ================================
    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * <p>
     * Si la entidad no tiene fecha de registro o estado definidos, se asignan
     * valores por defecto razonables:
     * <ul>
     * <li>Fecha de registro: {@link LocalDateTime#now()}</li>
     * <li>Estado: {@link Estado#ACTIVO}</li>
     * </ul>
     * </p>
     *
     * @param entity el usuario a crear
     * @return el ID generado para el nuevo usuario
     * @throws SQLException si ocurre un error al guardar el usuario en la base
     * de datos
     */
    @Override
    public Long create(Usuario entity) throws SQLException {
        // Defaults razonables
        if (entity.getFechaRegistro() == null) {
            entity.setFechaRegistro(LocalDateTime.now());
        }
        if (entity.getEstado() == null) {
            entity.setEstado(Estado.ACTIVO);
        }

        try {
            return usuarioDao.create(entity);
        } catch (SQLException e) {
            //SQLException con un mensaje más claro
            throw new SQLException("Error al crear el usuario con username = "
                    + entity.getUsername(), e);
        }
    }

    /**
     * Busca un usuario en la base de datos a partir de su ID.
     *
     * <p>
     * Este método delega la consulta al DAO correspondiente y devuelve un
     * {@link Optional} con el usuario si existe. Si ocurre un error de acceso a
     * datos, se lanza una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param id identificador del usuario a buscar
     * @return un {@link Optional} con el usuario si existe; vacío si no se
     * encuentra
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Optional<Usuario> findById(Long id) throws SQLException {
        try {
            return usuarioDao.findById(id);
        } catch (SQLException e) {
            //SQLException con un mensaje más claro
            throw new SQLException("Error al buscar el usuario con id = " + id, e);
        }
    }

    /**
     * Obtiene todos los usuarios que no estén marcados como eliminados.
     *
     * <p>
     * Este método consulta la base de datos a través del DAO y devuelve una
     * lista de usuarios activos. Si ocurre un error de acceso a datos, se lanza
     * una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @return una lista de usuarios activos (no eliminados). Si no hay
     * registros, la lista estará vacía.
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public List<Usuario> findAll() throws SQLException {
        try {
            return usuarioDao.findAll();
        } catch (SQLException e) {
            //SQLException con un mensaje más claro
            throw new SQLException("Error al listar los usuarios.", e);
        }
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * <p>
     * Si el usuario no tiene definido un estado, se asigna por defecto
     * {@link Estado#ACTIVO}. Luego se delega la actualización al DAO.</p>
     *
     * @param entity el usuario con los datos actualizados
     * @throws SQLException si ocurre un error al actualizar el usuario en la
     * base de datos
     */
    @Override
    public void update(Usuario entity) throws SQLException {
        if (entity.getEstado() == null) {
            entity.setEstado(Estado.ACTIVO);
        }

        try {
            usuarioDao.update(entity);
        } catch (SQLException e) {
            //SQLException con un mensaje más claro
            throw new SQLException("Error al actualizar el usuario con id = "
                    + entity.getIdUsuario(), e);
        }
    }

    /**
     * Realiza la baja lógica de un usuario en la base de datos.
     *
     * <p>
     * Este método marca al usuario como eliminado (eliminado = TRUE), sin
     * borrar físicamente el registro. La operación se delega al DAO. Si ocurre
     * un error de acceso a datos, se lanza una {@link SQLException} con un
     * mensaje descriptivo.</p>
     *
     * @param id identificador del usuario a dar de baja lógicamente
     * @throws SQLException si ocurre un error al marcar el usuario como
     * eliminado
     */
    @Override
    public void softDeleteById(Long id) throws SQLException {
        try {
            usuarioDao.softDeleteById(id);
        } catch (SQLException e) {
            //SQLException con un mensaje más claro
            throw new SQLException("Error al realizar la baja lógica del usuario con id = " + id, e);
        }
    }

    /**
     * Elimina físicamente un usuario de la base de datos.
     *
     * <p>
     * Este método borra el registro del usuario asociado al identificador
     * indicado. A diferencia de la baja lógica, aquí el registro se elimina de
     * forma definitiva. Si ocurre un error de acceso a datos, se lanza una
     * {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param id identificador del usuario a eliminar
     * @throws SQLException si ocurre un error al eliminar el usuario de la base
     * de datos
     */
    @Override
    public void deleteById(Long id) throws SQLException {
        try {
            usuarioDao.deleteById(id);
        } catch (SQLException e) {
            // Re-lanzamos la SQLException con un mensaje más claro
            throw new SQLException("Error al eliminar físicamente el usuario con id = " + id, e);
        }
    }

    // ================================
    // Métodos específicos
    // ================================
    /**
     * Busca un usuario en la base de datos a partir de su nombre de usuario
     * (username).
     *
     * <p>
     * Este método delega la consulta al DAO correspondiente y devuelve un
     * {@link Optional} con el usuario si existe. Si ocurre un error de acceso a
     * datos, se lanza una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param username nombre de usuario a buscar
     * @return un {@link Optional} con el usuario si existe; vacío si no se
     * encuentra
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Optional<Usuario> findByUsername(String username) throws SQLException {
        try {
            return usuarioDao.findByUsername(username);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al buscar el usuario con usernam = " + username, e);
        }
    }

    /**
     * Busca un usuario en la base de datos a partir de su correo electrónico.
     *
     * <p>
     * Este método delega la consulta al DAO correspondiente y devuelve un
     * {@link Optional} con el usuario si existe. Si ocurre un error de acceso a
     * datos, se lanza una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param email correo electrónico del usuario a buscar
     * @return un {@link Optional} con el usuario si existe; vacío si no se
     * encuentra
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Optional<Usuario> findByEmail(String email) throws SQLException {
        try {
            return usuarioDao.findByEmail(email);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al buscar el usuario con email = " + email, e);
        }
    }

    /**
     * Crea un nuevo {@link Usuario} junto con su {@link CredencialAcceso} en
     * una única transacción.
     *
     * <p>
     * Este método aplica validaciones de negocio mínimas (username, email y
     * credencial obligatorios), inicializa valores por defecto (fecha de
     * registro, estado, último cambio) y asegura la seguridad de la contraseña
     * generando un {@code salt} aleatorio y calculando el hash con SHA-256
     * antes de persistir los datos.</p>
     *
     * <p>
     * La operación se ejecuta de forma transaccional:
     * <ul>
     * <li>Se desactiva el autocommit de la conexión.</li>
     * <li>Se inserta primero el usuario en la tabla {@code usuario},
     * recuperando su ID generado.</li>
     * <li>Se inserta la credencial asociada en la tabla
     * {@code credencial_acceso}, usando el ID del usuario como FK.</li>
     * <li>Si ambas operaciones son exitosas, se confirma la transacción con
     * {@code commit()}.</li>
     * <li>Si ocurre cualquier error, se ejecuta {@code rollback()} para
     * revertir los cambios y evitar inconsistencias.</li>
     * </ul>
     * Al finalizar, se restaura el estado original de autocommit y se cierra la
     * conexión.</p>
     *
     * @param usuario objeto {@link Usuario} a persistir, con datos básicos y
     * credencial asociada
     * @return el identificador generado para el nuevo usuario
     * @throws IllegalArgumentException si el usuario o su credencial no cumplen
     * las validaciones mínimas
     * @throws SQLException si ocurre un error en la inserción de usuario o
     * credencial, o en las operaciones de commit/rollback
     *
     * @see java.sql.Connection#setAutoCommit(boolean)
     * @see java.sql.Connection#commit()
     * @see java.sql.Connection#rollback()
     */
    @Override
    public Long createUsuarioConCredencial(Usuario usuario) throws SQLException {
        // Validaciones mínimas de negocio
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no puede ser null");
        }
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email es obligatorio");
        }

        CredencialAcceso cred = usuario.getCredencial();
        if (cred == null) {
            throw new IllegalArgumentException("La credencial es obligatoria");
        }
        if (cred.getHashPassword() == null || cred.getHashPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        // Defaults razonables
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado(Estado.ACTIVO);
        }
        if (cred.getEstado() == null) {
            cred.setEstado(Estado.ACTIVO);
        }
        if (cred.getUltimoCambio() == null) {
            cred.setUltimoCambio(LocalDateTime.now());
        }

        // ===== Seguridad de contraseña =====
        String salt = PasswordUtil.generateSalt(16); // genera un salt aleatorio de 16 bytes
        String hash = PasswordUtil.hashPassword(cred.getHashPassword(), salt);

        cred.setSalt(salt);
        cred.setHashPassword(hash);

        Connection conn = null;
        boolean prevAutoCommit = true;

        try {
            conn = DatabaseConnection.getConnection();
            prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // --- INICIO TRANSACCIÓN ---

            // 1) Crear USUARIO (genera id)
            Long userId = usuarioDao.create(usuario, conn);

            // 2) Crear CREDENCIAL con FK al usuario recién creado
            cred.setUsuarioId(userId);
            credencialDao.create(cred, conn);

            // 3) Commit si todo OK
            conn.commit();
            return userId;

        } catch (SQLException ex) {
            // Rollback ante cualquier problema
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignore) {
                }
            }
            throw ex;
        } finally {
            // Restaurar autocommit y cerrar conexión
            if (conn != null) {
                try {
                    conn.setAutoCommit(prevAutoCommit);
                } catch (SQLException ignore) {
                }
            }
            DatabaseConnection.closeConnection();
        }
    }

    /**
     * DEMOSTRACION ROLLBACK Demostración de una transacción con
     * rollbackforzado.
     *
     * <p>
     * Este método no tiene sirve como prueba para mostrar cómo funciona el
     * manejo transaccional en JDBC:</p>
     *
     * <ol>
     * <li>Se inicia una transacción desactivando el autocommit.</li>
     * <li>Se crea un usuario de prueba en la base de datos (sin commit).</li>
     * <li>Se lanza intencionalmente una {@link SQLException} para simular un
     * error.</li>
     * <li>El bloque {@code catch} ejecuta un {@code rollback()}, revirtiendo la
     * operación y asegurando que el usuario no quede persistido.</li>
     * <li>Finalmente se restaura el estado original de autocommit y se cierra
     * la conexión.</li>
     * </ol>
     *
     * <p>
     * Al ejecutar este método, se puede comprobar en la base de datos que no se
     * insertan registros nuevos, validando que el rollback funciona
     * correctamente.</p>
     *
     * @throws SQLException siempre, ya que el error es forzado para demostrar
     * el rollback
     */
    @Override
    public void demoRollback() throws SQLException {
        Connection conn = null;
        boolean prevAutoCommit = true;

        try {
            System.out.println(">>> Iniciando demo de rollback con error simulado...");

            conn = DatabaseConnection.getConnection();
            prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false); // --- INICIO TRANSACCIÓN ---

            // 1) Crear un usuario de prueba
            Usuario u = new Usuario();
            u.setUsername("rollback_demo_" + System.currentTimeMillis());
            u.setNombre("Demo");
            u.setApellido("Rollback");
            u.setEmail("demo.rollback@example.com");
            u.setFechaRegistro(LocalDateTime.now());
            u.setActivo(true);
            u.setEstado(Estado.ACTIVO);
            u.setEliminado(false);

            Long idUsuario = usuarioDao.create(u, conn);
            System.out.println("Usuario demo creado con ID (sin commit): " + idUsuario);

            // 💣 2) ERROR FORZADO para demostrar rollback
            throw new SQLException("Error simulado para demostrar ROLLBACK");

            // 3) (Nunca llega acá)
            // conn.commit();
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // ROLLBACK REAL
                    System.out.println(">>> Se ejecutó ROLLBACK correctamente.");
                } catch (SQLException ignore) {
                }
            }
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(prevAutoCommit);
                } catch (SQLException ignore) {
                }
            }
            DatabaseConnection.closeConnection();
        }
    }

}
