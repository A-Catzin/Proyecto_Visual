/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Pasajero;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class PasajeroDaoImpl implements PasajeroDao {

    @Override
    public int crearPasajero(Pasajero pasajero) {
        String sql = "INSERT INTO pasajeros (Nombre, Apellido, Email, Fecha_Nacimiento) " +
                     "VALUES (?, ?, ?, ?)";

        int idGenerado = -1;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para crear pasajero.");
                return -1;
            }

            ps.setString(1, pasajero.getNombre());
            ps.setString(2, pasajero.getApellido());
            ps.setString(3, pasajero.getEmail());

            LocalDate fn = pasajero.getFechaNacimiento();
            if (fn != null) {
                ps.setDate(4, Date.valueOf(fn));
            } else {
                ps.setDate(4, null);
            }

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo insertar el pasajero (0 filas afectadas).");
            }

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
    public Pasajero obtenerPorId(int idPasajero) {
        String sql = "SELECT ID_Pasajero, Nombre, Apellido, Email, Fecha_Nacimiento " +
                     "FROM pasajeros WHERE ID_Pasajero = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para consultar pasajero.");
                return null;
            }

            ps.setInt(1, idPasajero);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAPasajero(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Pasajero obtenerPorEmail(String email) {
        String sql = "SELECT ID_Pasajero, Nombre, Apellido, Email, Fecha_Nacimiento " +
                     "FROM pasajeros WHERE Email = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para consultar pasajero.");
                return null;
            }

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAPasajero(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Pasajero> listarTodos() {
        String sql = "SELECT ID_Pasajero, Nombre, Apellido, Email, Fecha_Nacimiento FROM pasajeros";
        List<Pasajero> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para listar pasajeros.");
                return lista;
            }

            while (rs.next()) {
                lista.add(mapearResultSetAPasajero(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ---------- mapeo privado ----------

    private Pasajero mapearResultSetAPasajero(ResultSet rs) throws SQLException {
        Pasajero p = new Pasajero();
        p.setIdPasajero(rs.getInt("ID_Pasajero"));
        p.setNombre(rs.getString("Nombre"));
        p.setApellido(rs.getString("Apellido"));
        p.setEmail(rs.getString("Email"));

        Date fn = rs.getDate("Fecha_Nacimiento");
        if (fn != null) {
            p.setFechaNacimiento(fn.toLocalDate());
        }

        return p;
    }
}