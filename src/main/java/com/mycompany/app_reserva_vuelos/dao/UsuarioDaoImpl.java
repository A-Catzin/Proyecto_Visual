/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tehca
 */
public class UsuarioDaoImpl implements UsuarioDao {

    @Override
    public Usuario obtenerPorCredenciales(String usuario, String contraseña) {
        String sql = "SELECT id, nombre, usuario, email, contraseña " +
                     "FROM usuarios WHERE usuario = ? AND contraseña = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para validar usuario.");
                return null;
            }

            ps.setString(1, usuario);
            ps.setString(2, contraseña); // en un sistema real se usaría hash

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAUsuario(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Usuario obtenerPorNombreUsuario(String usuario) {
        String sql = "SELECT id, nombre, usuario, email, contraseña " +
                     "FROM usuarios WHERE usuario = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para consultar usuario.");
                return null;
            }

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAUsuario(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------- método privado de mapeo ----------

    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setUsuario(rs.getString("usuario"));
        u.setEmail(rs.getString("email"));
        u.setContraseña(rs.getString("contraseña"));
        return u;
    }
}