/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.service;

import java.sql.Connection;
import java.util.List;
import tpi_prog_2_Grupo18.Models.Usuario;
import tpi_prog_2_Grupo18.Dao.UsuarioDAO;

public class UsuarioServiceImpl {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServiceImpl(Connection connection) {
        this.usuarioDAO = new UsuarioDAO(); // tu DAO usa DatabaseConnection internamente
    }

    // Crear usuario
    public void insertarUsuario(Usuario usuario, String password, String estado) throws Exception {
        // Podés extender para crear credencial también
        usuario.setEstado(estado);
        usuario.setActivo(true);
        usuarioDAO.insertar(usuario);
    }

    // Listar todos los usuarios
    public List<Usuario> getAll() throws Exception {
        return usuarioDAO.getAll();
    }

    // Buscar usuario por ID
    public Usuario getUsuarioById(int id) throws Exception {
        return usuarioDAO.getById(id);
    }

    // Actualizar usuario
    public void actualizarUsuario(Usuario usuario) throws Exception {
        usuarioDAO.actualizar(usuario);
    }

    // Eliminar usuario (baja lógica)
    public void eliminarUsuario(int id) throws Exception {
        usuarioDAO.eliminar(id);
    }

    // Buscar por username
    public List<Usuario> buscarPorUsername(String filtro) throws Exception {
        return usuarioDAO.buscarPorUsername(filtro);
    }

    // Buscar por email
    public Usuario buscarPorEmail(String email) throws Exception {
        return usuarioDAO.buscarPorEmail(email);
    }
}