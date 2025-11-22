/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author tehca
 */
public class ConexionBD {
    //Coloca aqui toda la conexion a la base de datos
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/proyectovisual?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String pass = "1881";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Conexión exitosa 🎉");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage()); 
        }
    }
}       
