/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {

    public static boolean registrar(String nombre, String usuario, String email, String password) {
        String sql = "INSERT INTO usuarios (nombre, usuario, email, contraseña) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener la conexión a la base de datos.");
                return false;
            }

            stmt.setString(1, nombre);
            stmt.setString(2, usuario);
            stmt.setString(3, email);
            stmt.setString(4, password); // ideal: encriptar antes

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
}