/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Tarifa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class TarifaDaoImpl implements TarifaDao {

    @Override
    public Tarifa obtenerPorId(int idTarifa) {
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio " +
                     "FROM tarifas WHERE ID_Tarifa = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setInt(1, idTarifa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetATarifa(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Tarifa> listarPorIdVuelo(int idVuelo) {
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio " +
                     "FROM tarifas WHERE ID_Vuelo = ?";
        List<Tarifa> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return lista;

            ps.setInt(1, idVuelo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetATarifa(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Tarifa obtenerPorVueloYClase(int idVuelo, int idClase) {
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio " +
                     "FROM tarifas WHERE ID_Vuelo = ? AND ID_Clase = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setInt(1, idVuelo);
            ps.setInt(2, idClase);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetATarifa(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Tarifa mapearResultSetATarifa(ResultSet rs) throws SQLException {
        Tarifa t = new Tarifa();
        t.setIdTarifa(rs.getInt("ID_Tarifa"));
        t.setIdVuelo(rs.getInt("ID_Vuelo"));
        t.setIdClase(rs.getInt("ID_Clase"));
        t.setPrecio(rs.getBigDecimal("Precio"));
        return t;
    }
}