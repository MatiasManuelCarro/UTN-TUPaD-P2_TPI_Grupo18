package integradorfinal.programacion2.service.impl;

import integradorfinal.programacion2.dao.CredencialAccesoDao;
import integradorfinal.programacion2.dao.impl.CredencialAccesoDaoImpl;
import integradorfinal.programacion2.entities.CredencialAcceso;
import integradorfinal.programacion2.service.CredencialAccesoService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de CredencialAcceso.
 *
 * En esta clase centralizo la lógica de "negocio" relacionada con las
 * credenciales: - Delego las operaciones CRUD al DAO. - Aplico validaciones
 * mínimas cuando corresponde (por ejemplo, al actualizar password).
 *
 * La idea es que desde el menú o desde otras capas se hable con el Service y no
 * directamente con el DAO.
 */
public class CredencialAccesoServiceImpl implements CredencialAccesoService {

    // DAO que realmente se conecta a la base y ejecuta SQL
    private final CredencialAccesoDao credencialDao;

    /**
     * Constructor por defecto: instancio el DAO concreto que usa JDBC.
     */
    public CredencialAccesoServiceImpl() {
        this.credencialDao = new CredencialAccesoDaoImpl();
    }

    /**
     * Constructor alternativo: lo uso para inyectar un DAO "mock" en tests o
     * para cambiar la implementación sin modificar el código de arriba.
     */
    public CredencialAccesoServiceImpl(CredencialAccesoDao credencialDao) {
        this.credencialDao = credencialDao;
    }

    // ================================
    // CRUD genérico
    // ================================
    /**
     * Creo una nueva credencial delegando directamente en el DAO.
     */
    //@Override
    //public Long create(CredencialAcceso entity) throws SQLException {
    //    return credencialDao.create(entity);
    //}
    /**
     * Crea una nueva credencial de acceso para un usuario.
     *
     * @param entity la credencial a crear
     * @return el ID generado de la credencial
     * @throws IllegalArgumentException si el usuarioId es nulo
     * @throws IllegalStateException si el usuario ya tiene una credencial
     * asociada
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Long create(CredencialAcceso entity) throws SQLException {
        if (entity.getUsuarioId() == null) {
            throw new IllegalArgumentException("usuarioId es obligatorio");
        }
        if (credencialDao.findByUsuarioId(entity.getUsuarioId()).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene una credencial (Utilize Actualizar contraseña)");
        }
        return credencialDao.create(entity);
    }

    /**
     * Busco una credencial por su ID.
     */
    //@Override
    //public Optional<CredencialAcceso> findById(Long id) throws SQLException {
    //    return credencialDao.findById(id);
    //}
    /**
     * Busca una credencial por su ID.
     *
     * @param id identificador de la credencial
     * @return Optional con la credencial si existe
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Optional<CredencialAcceso> findById(Long id) throws SQLException {
        try {
            return credencialDao.findById(id);
        } catch (SQLException e) {
            // SQLException pero con un mensaje más amigable
            throw new SQLException("Error al buscar la credencial con id=" + id, e);
        }
    }

    /**
     * Obtiene todas las credenciales de acceso que no estén marcadas como
     * eliminadas.
     *
     * <p>
     * Este método consulta la base de datos a través del DAO y devuelve una
     * lista de credenciales activas. Si ocurre un error de acceso a datos, se
     * lanza una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @return una lista de credenciales activas (no eliminadas). Si no hay
     * registros, la lista estará vacía.
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public List<CredencialAcceso> findAll() throws SQLException {
        try {
            return credencialDao.findAll();
        } catch (SQLException e) {
            throw new SQLException("Error al listar las credenciales de acceso.", e);
        }
    }

    /**
     * Actualiza una credencial existente en la base de datos.
     *
     * <p>
     * Este método recibe una entidad {@link CredencialAcceso} y delega la
     * actualización al DAO correspondiente. Si ocurre un error de acceso a
     * datos, se lanza una {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param entity la credencial con los datos actualizados
     * @throws SQLException si ocurre un error al actualizar la credencial en la
     * base de datos
     */
    @Override
    public void update(CredencialAcceso entity) throws SQLException {
        try {
            credencialDao.update(entity);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al actualizar la credencial con id="
                    + entity.getIdCredencial(), e);
        }
    }

    /**
     * Realiza la baja lógica de una credencial de acceso.
     *
     * <p>
     * Este método marca la credencial como eliminada (eliminado = TRUE) en la
     * base de datos, sin borrar físicamente el registro. Si ocurre un error de
     * acceso a datos, se lanza una {@link SQLException} con un mensaje
     * descriptivo.</p>
     *
     * @param id identificador de la credencial a dar de baja
     * @throws SQLException si ocurre un error al marcar la credencial como
     * eliminada
     */
    @Override
    public void softDeleteById(Long id) throws SQLException {
        try {
            credencialDao.softDeleteById(id);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al realizar la baja lógica de la credencial con id=" + id, e);
        }
    }

    /**
     * Elimina físicamente una credencial de acceso de la base de datos.
     *
     * <p>
     * Este método borra el registro de credencial asociado al identificador
     * indicado. A diferencia de la baja lógica, aquí el registro se elimina de
     * forma definitiva. Si ocurre un error de acceso a datos, se lanza una
     * {@link SQLException} con un mensaje descriptivo.</p>
     *
     * @param id identificador de la credencial a eliminar
     * @throws SQLException si ocurre un error al eliminar la credencial de la
     * base de datos
     */
    @Override
    public void deleteById(Long id) throws SQLException {
        try {
            credencialDao.deleteById(id);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al eliminar físicamente la credencial con id=" + id, e);
        }
    }

    // ================================
    // Métodos específicos
    // ================================
    /**
     * Busca la credencial asociada a un usuario a partir de su ID.
     *
     * <p>
     * Este método se utiliza en el login y en operaciones donde es necesario
     * obtener la credencial vinculada a un usuario. Si ocurre un error de
     * acceso a datos, se lanza una {@link SQLException} con un mensaje
     * descriptivo.</p>
     *
     * @param usuarioId identificador del usuario dueño de la credencial
     * @return un {@link Optional} con la credencial si existe; vacío si no se
     * encuentra
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    @Override
    public Optional<CredencialAcceso> findByUsuarioId(Long usuarioId) throws SQLException {
        try {
            return credencialDao.findByUsuarioId(usuarioId);
        } catch (SQLException e) {
            // SQLException con un mensaje más claro
            throw new SQLException("Error al buscar la credencial asociada al usuario con id="
                    + usuarioId, e);
        }
    }

    /**
     * Actualiza la contraseña de un usuario de forma segura.
     *
     * <p>
     * El proceso incluye:
     * <ul>
     * <li>Validar que el nuevo password no esté vacío.</li>
     * <li>Generar un salt aleatorio.</li>
     * <li>Calcular el hash usando SHA-256 + salt mediante
     * {@link PasswordUtil}.</li>
     * <li>Delegar en el DAO para que ejecute el stored procedure que actualiza
     * la contraseña.</li>
     * </ul>
     * </p>
     *
     * @param usuarioId identificador del usuario cuya credencial se va a
     * actualizar
     * @param nuevoPasswordPlano contraseña en texto plano que será validada,
     * salteada y hasheada
     * @param ignorar parámetro reservado para compatibilidad con la interfaz;
     * actualmente no se utiliza
     * @throws IllegalArgumentException si el nuevo password es nulo o está
     * vacío
     * @throws SQLException si ocurre un error al actualizar la contraseña en la
     * base de datos
     */
    @Override
    public void updatePasswordSeguro(Long usuarioId, String nuevoPasswordPlano, String ignorar) throws SQLException {
        if (nuevoPasswordPlano == null || nuevoPasswordPlano.isBlank()) {
            throw new IllegalArgumentException("El password no puede estar vacío");
        }

        // Genero un salt nuevo para esta actualización
        String salt = integradorfinal.programacion2.util.PasswordUtil.generateSalt(16);

        // Calculo el hash mezclando el password con el salt
        String hash = integradorfinal.programacion2.util.PasswordUtil.hashPassword(nuevoPasswordPlano, salt);

        // Delego al DAO para que llame al stored procedure con el hash y el salt
        credencialDao.updatePasswordSeguro(usuarioId, hash, salt);
    }

}
