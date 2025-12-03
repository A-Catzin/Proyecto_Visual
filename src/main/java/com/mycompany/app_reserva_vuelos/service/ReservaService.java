/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.DetalleReserva;
import com.mycompany.app_reserva_vuelos.model.Reserva;

import java.util.List;

/**
 *
 * @author tehca
 */
public interface ReservaService { // Nombre de la interfaz sin 'I'

    int crearReserva(Reserva reserva, List<DetalleReserva> detalles);

    boolean cancelarReserva(int idReserva, String nuevoEstado);

    Reserva obtenerReservaPorId(int idReserva);

    List<DetalleReserva> obtenerDetallesPorReserva(int idReserva);

    ReservaServiceImpl.ReservaCompleta obtenerReservaCompleta(int idReserva);

    List<Reserva> listarReservasPorPasajero(int idPasajero);

    List<Reserva> listarTodasReservas();
}
