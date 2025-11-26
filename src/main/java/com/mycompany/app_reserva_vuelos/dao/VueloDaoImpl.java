/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;
import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Vuelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */

public class VueloDaoImpl implements VueloDao {

    @Override
    public List<Vuelo> buscarVuelosPorOrigenDestinoYFecha(int idAeropuertoOrigen,
                                                          int idAeropuertoDestino,
                                                          LocalDate fecha) {
        List<Vuelo> vuelos = new ArrayList<>();

        String sql = "SELECT ID_Vuelo, Numero_Vuelo, ID_Aeropuerto_Origen, " +
                     "       ID_Aeropuerto_Destino, ID_Aeronave, " +
                     "       Fecha_Hora_Salida, Fecha_Hora_Llegada " +
                     "FROM vuelos " +
                     "WHERE ID_Aeropuerto_Origen = ? " +
                     "  AND ID_Aeropuerto_Destino = ? " +
                     "  AND DATE(Fecha_Hora_Salida) = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return vuelos;

            ps.setInt(1, idAeropuertoOrigen);
            ps.setInt(2, idAeropuertoDestino);
            ps.setString(3, fecha.toString()); // "YYYY-MM-DD"

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vuelos.add(mapearResultSetAVuelo(rs));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return vuelos;
    }

    @Override
    public Vuelo obtenerPorId(int idVuelo) {
        Vuelo vuelo = null;

        String sql = "SELECT ID_Vuelo, Numero_Vuelo, ID_Aeropuerto_Origen, " +
                     "       ID_Aeropuerto_Destino, ID_Aeronave, " +
                     "       Fecha_Hora_Salida, Fecha_Hora_Llegada " +
                     "FROM vuelos WHERE ID_Vuelo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            ps.setInt(1, idVuelo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vuelo = mapearResultSetAVuelo(rs);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return vuelo;
    }

    private Vuelo mapearResultSetAVuelo(ResultSet rs) throws SQLException {
        Vuelo vuelo = new Vuelo();

        vuelo.setIdVuelo(rs.getInt("ID_Vuelo"));
        vuelo.setNumeroVuelo(rs.getString("Numero_Vuelo"));
        vuelo.setIdAeropuertoOrigen(rs.getInt("ID_Aeropuerto_Origen"));
        vuelo.setIdAeropuertoDestino(rs.getInt("ID_Aeropuerto_Destino"));
        vuelo.setIdAeronave(rs.getInt("ID_Aeronave"));

        Timestamp tsSalida  = rs.getTimestamp("Fecha_Hora_Salida");
        Timestamp tsLlegada = rs.getTimestamp("Fecha_Hora_Llegada");

        if (tsSalida != null) {
            LocalDateTime salida = tsSalida.toLocalDateTime();
            vuelo.setFechaHoraSalida(salida);
        }
        if (tsLlegada != null) {
            LocalDateTime llegada = tsLlegada.toLocalDateTime();
            vuelo.setFechaHoraLlegada(llegada);
        }

        return vuelo;
    }
}