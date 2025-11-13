/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.integradorfinal.programacion2.entities;

/**
 *
 * @author Matias
 */

/**
 * Enumeración para representar los estados posibles (ACTIVO / INACTIVO)
 * presentes en las tablas usuario y credencial_acceso.
 */
public enum Estado {
    ACTIVO,
    INACTIVO;

    /**
     * Convierte un texto a enum, devolviendo ACTIVO si es nulo.
     */
        public static Estado from(String valor) {
        if (valor == null) return ACTIVO;
        return Estado.valueOf(valor.toUpperCase());
    }

    /**
     * Devuelve el valor para guardar en la base de datos.
     */
    public String dbValue() {
        return name();
    }
}
