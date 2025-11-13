/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tpi_prog_2_Grupo18.Models.Usuario;
import tpi_prog_2_Grupo18.Models.Credencial;
import tpi_prog_2_Grupo18.Util.PasswordUtil;

public class CredencialServiceImpl {
    private final Connection connection;

    public CredencialServiceImpl(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection no puede ser null");
        }
        this.connection = connection;
    }

// Crear credencial para un usuario
public void insertarCredencial(Usuario usuario, String password, String estado) throws Exception {
    String salt = PasswordUtil.generarSalt();
    String hash = PasswordUtil.hashPassword(password, salt);

    String sql = "INSERT INTO credencial_acceso (usuario_id, hash_password, salt, estado, requiere_reset) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, usuario.getId());
        ps.setString(2, hash);
        ps.setString(3, salt);
        ps.setString(4, estado);   // ahora es String, no enum
        ps.setBoolean(5, false);
        ps.executeUpdate();
    }
}
    // Listar todas las credenciales
    public List<Credencial> getAll() throws Exception {
        List<Credencial> lista = new ArrayList<>();
        String sql = "SELECT * FROM credencial_acceso";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Credencial c = new Credencial();
                c.setId(rs.getInt("id"));
                c.setEstado(rs.getString("estado"));
                c.setUltimaSesion(rs.getTimestamp("ultima_sesion").toLocalDateTime());
                c.setRequiereReset(rs.getBoolean("requiere_reset"));
                lista.add(c);
            }
        }
        return lista;
    }

    // Actualizar credencial (ej: cambiar contraseña)
    public void actualizarCredencial(Usuario usuario, String newPassword, boolean requiereReset) throws Exception {
        String salt = PasswordUtil.generarSalt();
        String hash = PasswordUtil.hashPassword(newPassword, salt);

        String sql = "UPDATE credencial_acceso SET hash_password = ?, salt = ?, requiere_reset = ?, ultimo_cambio = NOW() WHERE usuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, salt);
            ps.setBoolean(3, requiereReset);
            ps.setInt(4, usuario.getId());
            ps.executeUpdate();
        }
    }

    // Obtener credencial por usuario
    public Credencial getCredencialByUsuarioId(Usuario usuario) throws Exception {
        String sql = "SELECT * FROM credencial_acceso WHERE usuario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, usuario.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Credencial c = new Credencial();
                    c.setId(rs.getInt("id"));
                    c.setEstado(rs.getString("estado"));
                    c.setUltimaSesion(rs.getTimestamp("ultima_sesion").toLocalDateTime());
                    c.setRequiereReset(rs.getBoolean("requiere_reset"));
                    return c;
                }
            }
        }
        return null;
    }
}