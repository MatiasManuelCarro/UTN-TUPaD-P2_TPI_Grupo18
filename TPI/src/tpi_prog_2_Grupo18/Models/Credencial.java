/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Credencial extends Base {

    // Estado de la credencial como texto: "ACTIVO" o "INACTIVO"
    private String estado;

    private LocalDateTime ultimaSesion;
    private String hashPassword;
    private String salt;
    private LocalDateTime ultimoCambio;
    private Boolean requiereReset;
    private Usuario usuario;

    // Constructor completo
    public Credencial(int id, String estado, LocalDateTime ultimaSesion,
                      String hashPassword, String salt,
                      LocalDateTime ultimoCambio, Boolean requiereReset,
                      Usuario usuario) {
        super(id, false);
        this.estado = estado;
        this.ultimaSesion = ultimaSesion;
        this.hashPassword = hashPassword;
        this.salt = salt;
        this.ultimoCambio = ultimoCambio;
        this.requiereReset = requiereReset;
        this.usuario = usuario;
    }

    // Constructor por defecto
    public Credencial() {
        super();
    }

    // Getters y Setters
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getUltimaSesion() { return ultimaSesion; }
    public void setUltimaSesion(LocalDateTime ultimaSesion) { this.ultimaSesion = ultimaSesion; }

    public String getHashPassword() { return hashPassword; }
    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword; }

    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }

    public LocalDateTime getUltimoCambio() { return ultimoCambio; }
    public void setUltimoCambio(LocalDateTime ultimoCambio) { this.ultimoCambio = ultimoCambio; }

    public Boolean getRequiereReset() { return requiereReset; }
    public void setRequiereReset(Boolean requiereReset) { this.requiereReset = requiereReset; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Credencial{" +
                "id=" + getId() +
                ", estado='" + estado + '\'' +
                ", ultimaSesion=" + ultimaSesion +
                ", hashPassword='" + hashPassword + '\'' +
                ", salt='" + salt + '\'' +
                ", ultimoCambio=" + ultimoCambio +
                ", requiereReset=" + requiereReset +
                ", usuario=" + (usuario != null ? usuario.getId() : null) +
                ", eliminado=" + isEliminado() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credencial)) return false;
        Credencial that = (Credencial) o;
        return Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario);
    }
}
