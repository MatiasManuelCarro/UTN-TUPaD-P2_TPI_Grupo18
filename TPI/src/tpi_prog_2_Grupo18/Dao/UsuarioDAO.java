/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi_prog_2_Grupo18.Dao;

import tpi_prog_2_Grupo18.Models.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import tpi_prog_2_Grupo18.Config.DatabaseConnection;
//import tpi_prog_2_Grupo18.Models.Credencial;

/**
 *
 * @author Matias
 */
public class UsuarioDAO implements GenericDAO<Usuario>{
    
     private static final String INSERT_SQL = "INSERT INTO personas (nombre, apellido, dni, domicilio_id) VALUES (?, ?, ?, ?)";
    
}
