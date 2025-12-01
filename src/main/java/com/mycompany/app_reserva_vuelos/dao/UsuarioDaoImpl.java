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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class UsuarioDaoImpl implements UsuarioDao {

    @Override
    public Usuario obtenerPorCredenciales(String usuario, String contraseña) {
        String sql = "SELECT id, nombre, usuario, email, contraseña, telefono, rol, estado " +
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
        String sql = "SELECT id, nombre, usuario, email, contraseña, telefono, rol, estado " +
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

    @Override
    public int registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, usuario, email, contraseña, telefono, rol, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int idGenerado = -1;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para registrar usuario.");
                return -1;
            }

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getContraseña());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getRol());
            ps.setString(7, usuario.getEstado());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el usuario (0 filas afectadas).");
            }

            // Obtener el ID generado (AUTOINCREMENT)
            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) {
                    idGenerado = rsKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idGenerado;
    }

    @Override
    public void modificar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, usuario = ?, email = ?, contraseña = ?, telefono = ?, rol = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null)
                return;

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getContraseña());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getRol());
            ps.setString(7, usuario.getEstado());
            ps.setInt(8, usuario.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null)
                return;

            ps.setInt(1, idUsuario);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, usuario, email, contraseña, telefono, rol, estado FROM usuarios";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (conn == null)
                return lista;

            while (rs.next()) {
                lista.add(mapearResultSetAUsuario(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ---------- método privado de mapeo ----------

    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombre(rs.getString("nombre"));
        u.setUsuario(rs.getString("usuario"));
        u.setEmail(rs.getString("email"));
        u.setContraseña(rs.getString("contraseña"));
        u.setTelefono(rs.getString("telefono"));
        u.setRol(rs.getString("rol"));
        u.setEstado(rs.getString("estado"));
        return u;
    }
}