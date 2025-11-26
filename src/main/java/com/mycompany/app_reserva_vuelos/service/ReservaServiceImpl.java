/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.PasajeroDao;
import com.mycompany.app_reserva_vuelos.dao.PasajeroDaoImpl;
import com.mycompany.app_reserva_vuelos.dao.ReservaDao;
import com.mycompany.app_reserva_vuelos.dao.ReservaDaoImpl;
import com.mycompany.app_reserva_vuelos.dao.VueloDao;
import com.mycompany.app_reserva_vuelos.dao.VueloDaoImpl;
import com.mycompany.app_reserva_vuelos.model.DetalleReserva;
import com.mycompany.app_reserva_vuelos.model.Pasajero;
import com.mycompany.app_reserva_vuelos.model.Reserva;
import com.mycompany.app_reserva_vuelos.model.Vuelo;

import java.util.List;

/**
 *
 * @author tehca
 */
/*
 * Implementación de ReservaService.
 */
public class ReservaServiceImpl implements ReservaService { // Implementa la interfaz ReservaService

    private final ReservaDao reservaDao;
    private final PasajeroDao pasajeroDao;
    private final VueloDao vueloDao;

    public ReservaServiceImpl() {
        this.reservaDao = new ReservaDaoImpl();
        this.pasajeroDao = new PasajeroDaoImpl();
        this.vueloDao = new VueloDaoImpl();
    }

    public ReservaServiceImpl(ReservaDao reservaDao, PasajeroDao pasajeroDao, VueloDao vueloDao) {
        this.reservaDao = reservaDao;
        this.pasajeroDao = pasajeroDao;
        this.vueloDao = vueloDao;
    }

    @Override
    public int crearReserva(Reserva reserva, List<DetalleReserva> detalles) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La reserva debe tener al menos un detalle");
        }

        Pasajero pasajero = pasajeroDao.obtenerPorId(reserva.getIdPasajero());
        if (pasajero == null) {
            throw new IllegalArgumentException("El pasajero asociado a la reserva no existe");
        }

        for (DetalleReserva det : detalles) {
            Vuelo vuelo = vueloDao.obtenerPorId(det.getIdVuelo());
            if (vuelo == null) {
                throw new IllegalArgumentException("El vuelo con ID " + det.getIdVuelo() + " no existe");
            }
        }

        try {
            return reservaDao.crearReserva(reserva, detalles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear la reserva", e);
        }
    }

    @Override
    public boolean cancelarReserva(int idReserva, String nuevoEstado) {
        try {
            return reservaDao.actualizarEstadoReserva(idReserva, nuevoEstado);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cancelar la reserva", e);
        }
    }

    @Override
    public Reserva obtenerReservaPorId(int idReserva) {
        try {
            return reservaDao.obtenerReservaPorId(idReserva);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la reserva", e);
        }
    }

    @Override
    public List<DetalleReserva> obtenerDetallesPorReserva(int idReserva) {
        try {
            return reservaDao.obtenerDetallesPorIdReserva(idReserva);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los detalles de la reserva", e);
        }
    }

    /**
     * DTO que agrupa reserva y detalles.
     */
    public static class ReservaCompleta {
        private Reserva reserva;
        private List<DetalleReserva> detalles;

        public ReservaCompleta(Reserva reserva, List<DetalleReserva> detalles) {
            this.reserva = reserva;
            this.detalles = detalles;
        }

        public Reserva getReserva() { return reserva; }

        public List<DetalleReserva> getDetalles() { return detalles; }
    }

    @Override
    public ReservaCompleta obtenerReservaCompleta(int idReserva) {
        try {
            Reserva reserva = reservaDao.obtenerReservaPorId(idReserva);
            if (reserva == null) {
                return null;
            }
            List<DetalleReserva> detalles = reservaDao.obtenerDetallesPorIdReserva(idReserva);
            return new ReservaCompleta(reserva, detalles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener la reserva completa", e);
        }
    }
}