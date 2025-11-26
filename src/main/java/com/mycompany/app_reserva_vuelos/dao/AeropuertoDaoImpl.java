/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Aeropuerto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AeropuertoDaoImpl implements AeropuertoDao {

    @Override
    public Aeropuerto obtenerPorId(int idAeropuerto) {
        String sql = "SELECT ID_Aeropuerto, Codigo_IATA, Nombre_Aeropuerto, ID_Ciudad " +
                     "FROM aeropuertos WHERE ID_Aeropuerto = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setInt(1, idAeropuerto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAAeropuerto(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Aeropuerto obtenerPorCodigoIATA(String codigoIata) {
        String sql = "SELECT ID_Aeropuerto, Codigo_IATA, Nombre_Aeropuerto, ID_Ciudad " +
                     "FROM aeropuertos WHERE Codigo_IATA = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setString(1, codigoIata);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAAeropuerto(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Aeropuerto> listarTodos() {
        String sql = "SELECT ID_Aeropuerto, Codigo_IATA, Nombre_Aeropuerto, ID_Ciudad FROM aeropuertos";
        List<Aeropuerto> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (conn == null) return lista;

            while (rs.next()) {
                lista.add(mapearResultSetAAeropuerto(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Aeropuerto> listarPorIdCiudad(int idCiudad) {
        String sql = "SELECT ID_Aeropuerto, Codigo_IATA, Nombre_Aeropuerto, ID_Ciudad " +
                     "FROM aeropuertos WHERE ID_Ciudad = ?";
        List<Aeropuerto> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return lista;

            ps.setInt(1, idCiudad);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetAAeropuerto(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private Aeropuerto mapearResultSetAAeropuerto(ResultSet rs) throws SQLException {
        Aeropuerto a = new Aeropuerto();
        a.setIdAeropuerto(rs.getInt("ID_Aeropuerto"));
        a.setCodigoIATA(rs.getString("Codigo_IATA"));
        a.setNombreAeropuerto(rs.getString("Nombre_Aeropuerto"));
        a.setIdCiudad(rs.getInt("ID_Ciudad"));
        return a;
    }
}
