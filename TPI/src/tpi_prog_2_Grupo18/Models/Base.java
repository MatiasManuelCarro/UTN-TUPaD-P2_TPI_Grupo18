package tpi_prog_2_Grupo18.Models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Grupo18
 */
public abstract class Base {

    private int id;
    private boolean eliminado;

    protected Base(int id, boolean eliminado) {
        this.id = id;
        this.eliminado = eliminado;
    }

    protected Base() {
        this.eliminado = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}


