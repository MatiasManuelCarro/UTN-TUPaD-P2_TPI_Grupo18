/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.integradorfinal.programacion2.config;

public final class Config {
    private Config() {}

    public static final String JDBC_URL =
        "jdbc:mysql://127.0.0.1:3306/tpi_prog_2"
        + "?useUnicode=true&characterEncoding=utf8"
        + "&useSSL=false&allowPublicKeyRetrieval=true"
        + "&serverTimezone=America/Argentina/Buenos_Aires";

    public static final String DB_USER = "root";
    public static final String DB_PASS = "1234";
}
