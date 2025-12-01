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
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio, Temporada, Descuento, Recargo " +
                "FROM tarifas WHERE ID_Tarifa = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio, Temporada, Descuento, Recargo " +
                "FROM tarifas WHERE ID_Vuelo = ?";
        List<Tarifa> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio, Temporada, Descuento, Recargo " +
                "FROM tarifas WHERE ID_Vuelo = ? AND ID_Clase = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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

    @Override
    public int registrar(Tarifa tarifa) {
        String sql = "INSERT INTO tarifas (ID_Vuelo, ID_Clase, Precio, Temporada, Descuento, Recargo) VALUES (?, ?, ?, ?, ?, ?)";
        int idGenerado = -1;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tarifa.getIdVuelo());
            ps.setInt(2, tarifa.getIdClase());
            ps.setBigDecimal(3, tarifa.getPrecio());
            ps.setString(4, tarifa.getTemporada());
            ps.setBigDecimal(5, tarifa.getDescuento());
            ps.setBigDecimal(6, tarifa.getRecargo());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }

    @Override
    public void modificar(Tarifa tarifa) {
        String sql = "UPDATE tarifas SET ID_Vuelo = ?, ID_Clase = ?, Precio = ?, Temporada = ?, Descuento = ?, Recargo = ? WHERE ID_Tarifa = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tarifa.getIdVuelo());
            ps.setInt(2, tarifa.getIdClase());
            ps.setBigDecimal(3, tarifa.getPrecio());
            ps.setString(4, tarifa.getTemporada());
            ps.setBigDecimal(5, tarifa.getDescuento());
            ps.setBigDecimal(6, tarifa.getRecargo());
            ps.setInt(7, tarifa.getIdTarifa());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int idTarifa) {
        String sql = "DELETE FROM tarifas WHERE ID_Tarifa = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTarifa);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Tarifa> listar() {
        String sql = "SELECT ID_Tarifa, ID_Vuelo, ID_Clase, Precio, Temporada, Descuento, Recargo FROM tarifas";
        List<Tarifa> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSetATarifa(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Tarifa mapearResultSetATarifa(ResultSet rs) throws SQLException {
        Tarifa t = new Tarifa();
        t.setIdTarifa(rs.getInt("ID_Tarifa"));
        t.setIdVuelo(rs.getInt("ID_Vuelo"));
        t.setIdClase(rs.getInt("ID_Clase"));
        t.setPrecio(rs.getBigDecimal("Precio"));
        t.setTemporada(rs.getString("Temporada"));
        t.setDescuento(rs.getBigDecimal("Descuento"));
        t.setRecargo(rs.getBigDecimal("Recargo"));
        return t;
    }
}