/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario extends Base {

    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
    private String estado;

    public Usuario(int id, String username, String nombre, String apellido,
                   String email, LocalDateTime fechaRegistro,
                   Boolean activo, String estado) {
        super(id, false); // eliminado = false por defecto
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
        this.estado = estado;
    }

    public Usuario() {
        super();
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    // Getter estilo boolean
    public boolean isActivo() { return activo != null && activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", activo=" + activo +
                ", estado='" + estado + '\'' +
                ", eliminado=" + isEliminado() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username) &&
               Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
