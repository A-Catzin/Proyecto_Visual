/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import com.mycompany.app_reserva_vuelos.model.Reserva;
import com.mycompany.app_reserva_vuelos.model.DetalleReserva;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */
public class ReservaDaoImpl implements ReservaDao {

    @Override
    public int crearReserva(Reserva reserva, List<DetalleReserva> detalles) {
        String sqlReserva = "INSERT INTO reservas (Codigo_Reserva, ID_Pasajero, Fecha_Reserva, Estado_Reserva) " +
                "VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_reserva (ID_Reserva, ID_Vuelo, ID_Tarifa, Numero_Asiento) " +
                "VALUES (?, ?, ?, ?)";

        int idGenerado = -1;

        try (Connection conn = ConexionBD.getConnection()) {
            if (conn == null) {
                System.err.println("No se pudo obtener conexión para crear la reserva.");
                return -1;
            }

            // Iniciar transacción
            conn.setAutoCommit(false);

            // 1) Insertar en tabla reservas
            try (PreparedStatement psReserva = conn.prepareStatement(sqlReserva, Statement.RETURN_GENERATED_KEYS)) {

                psReserva.setString(1, reserva.getCodigoReserva());
                psReserva.setInt(2, reserva.getIdPasajero());

                // Fecha_Reserva DATETIME
                LocalDateTime fecha = reserva.getFechaReserva();
                if (fecha != null) {
                    Timestamp ts = Timestamp.valueOf(fecha);
                    psReserva.setTimestamp(3, ts);
                } else {
                    // Si quieres que SQLite ponga CURRENT_TIMESTAMP, podrías pasar null
                    psReserva.setTimestamp(3, null);
                }

                psReserva.setString(4, reserva.getEstadoReserva());

                int filas = psReserva.executeUpdate();
                if (filas == 0) {
                    throw new SQLException("No se pudo insertar la reserva (0 filas afectadas).");
                }

                // Obtener ID_Reserva generado (AUTOINCREMENT)
                try (ResultSet rsKeys = psReserva.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idGenerado = rsKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado de la reserva.");
                    }
                }
            }

            // 2) Insertar detalles asociados
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                for (DetalleReserva det : detalles) {
                    psDetalle.setInt(1, idGenerado);
                    psDetalle.setInt(2, det.getIdVuelo());
                    psDetalle.setInt(3, det.getIdTarifa());
                    psDetalle.setString(4, det.getNumeroAsiento());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            // Confirmar transacción
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
            idGenerado = -1;
        }

        return idGenerado;
    }

    @Override
    public Reserva obtenerReservaPorId(int idReserva) {
        String sql = "SELECT ID_Reserva, Codigo_Reserva, ID_Pasajero, Fecha_Reserva, Estado_Reserva " +
                "FROM reservas WHERE ID_Reserva = ?";

        Reserva reserva = null;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para consultar reserva.");
                return null;
            }

            ps.setInt(1, idReserva);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reserva = mapearResultSetAReserva(rs);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reserva;
    }

    @Override
    public List<DetalleReserva> obtenerDetallesPorIdReserva(int idReserva) {
        String sql = "SELECT ID_Detalle, ID_Reserva, ID_Vuelo, ID_Tarifa, Numero_Asiento " +
                "FROM detalle_reserva WHERE ID_Reserva = ?";

        List<DetalleReserva> detalles = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para consultar detalles de reserva.");
                return detalles;
            }

            ps.setInt(1, idReserva);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleReserva det = new DetalleReserva();
                    det.setIdDetalle(rs.getInt("ID_Detalle"));
                    det.setIdReserva(rs.getInt("ID_Reserva"));
                    det.setIdVuelo(rs.getInt("ID_Vuelo"));
                    det.setIdTarifa(rs.getInt("ID_Tarifa"));
                    det.setNumeroAsiento(rs.getString("Numero_Asiento"));
                    detalles.add(det);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return detalles;
    }

    @Override
    public boolean actualizarEstadoReserva(int idReserva, String nuevoEstado) {
        String sql = "UPDATE reservas SET Estado_Reserva = ? WHERE ID_Reserva = ?";
        boolean exito = false;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para actualizar reserva.");
                return false;
            }

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);

            int filas = ps.executeUpdate();
            exito = (filas > 0);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return exito;
    }

    // ----------------- Métodos privados de mapeo -----------------

    private Reserva mapearResultSetAReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();

        reserva.setIdReserva(rs.getInt("ID_Reserva"));
        reserva.setCodigoReserva(rs.getString("Codigo_Reserva"));
        reserva.setIdPasajero(rs.getInt("ID_Pasajero"));

        Timestamp ts = rs.getTimestamp("Fecha_Reserva");
        if (ts != null) {
            LocalDateTime fecha = ts.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            reserva.setFechaReserva(fecha);
        }

        reserva.setEstadoReserva(rs.getString("Estado_Reserva"));

        return reserva;
    }

    @Override
    public List<Reserva> listarReservasPorPasajero(int idPasajero) {
        String sql = "SELECT ID_Reserva, Codigo_Reserva, ID_Pasajero, Fecha_Reserva, Estado_Reserva " +
                "FROM reservas WHERE ID_Pasajero = ? ORDER BY Fecha_Reserva DESC";

        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para listar reservas.");
                return reservas;
            }

            ps.setInt(1, idPasajero);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearResultSetAReserva(rs));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reservas;
    }

    @Override
    public List<Reserva> listarTodasReservas() {
        String sql = "SELECT ID_Reserva, Codigo_Reserva, ID_Pasajero, Fecha_Reserva, Estado_Reserva " +
                "FROM reservas ORDER BY Fecha_Reserva DESC";

        List<Reserva> reservas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("No se pudo obtener conexión para listar todas las reservas.");
                return reservas;
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearResultSetAReserva(rs));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return reservas;
    }
}
