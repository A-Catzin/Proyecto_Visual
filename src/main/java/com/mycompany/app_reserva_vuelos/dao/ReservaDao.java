/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Reserva;
import com.mycompany.app_reserva_vuelos.model.DetalleReserva;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface ReservaDao {

    /**
     * Inserta una nueva reserva y sus detalles en la base de datos.
     * Debe hacerse en una transacción.
     *
     * @param reserva         objeto Reserva (sin idReserva aún)
     * @param detalles        lista de detalles asociados al vuelo/tarifa/asiento
     * @return el id generado para la reserva o -1 si falla
     */
    int crearReserva(Reserva reserva, List<DetalleReserva> detalles);

    /**
     * Obtiene una reserva por su ID (solo cabecera).
     */
    Reserva obtenerReservaPorId(int idReserva);

    /**
     * Obtiene los detalles de una reserva.
     */
    List<DetalleReserva> obtenerDetallesPorIdReserva(int idReserva);

    /**
     * Cambia el estado de la reserva (por ejemplo, 'Activa' -> 'Cancelada').
     */
    boolean actualizarEstadoReserva(int idReserva, String nuevoEstado);
}