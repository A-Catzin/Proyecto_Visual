/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.AeropuertoDao;
import com.mycompany.app_reserva_vuelos.dao.AeropuertoDaoImpl;
import com.mycompany.app_reserva_vuelos.dao.VueloDao;
import com.mycompany.app_reserva_vuelos.dao.VueloDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import com.mycompany.app_reserva_vuelos.model.Vuelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tehca
 */

/**
 * Implementación de VueloService.
 * La UI debe depender de VueloService, no de esta clase concreta.
 */
public class VueloServiceImpl implements VueloService { // Implementa la interfaz VueloService

    private final VueloDao vueloDao;
    private final AeropuertoDao aeropuertoDao;

    public VueloServiceImpl() {
        this.vueloDao = new VueloDaoImpl();
        this.aeropuertoDao = new AeropuertoDaoImpl();
    }

    public VueloServiceImpl(VueloDao vueloDao, AeropuertoDao aeropuertoDao) {
        this.vueloDao = vueloDao;
        this.aeropuertoDao = aeropuertoDao;
    }

    @Override
    public List<Vuelo> buscarVuelos(int idAeropuertoOrigen, int idAeropuertoDestino, LocalDate fecha) {
        try {
            return vueloDao.buscarVuelosPorOrigenDestinoYFecha(idAeropuertoOrigen, idAeropuertoDestino, fecha);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar vuelos", e);
        }
    }

    /**
     * DTO para la UI con datos amigables del vuelo y aeropuertos.
     */
    public static class VueloInfoUI {
        private Vuelo vuelo;
        private String nombreAeropuertoOrigen;
        private String nombreAeropuertoDestino;
        private String codigoIataOrigen;
        private String codigoIataDestino;

        public VueloInfoUI(Vuelo vuelo,
                           String nombreAeropuertoOrigen,
                           String nombreAeropuertoDestino,
                           String codigoIataOrigen,
                           String codigoIataDestino) {
            this.vuelo = vuelo;
            this.nombreAeropuertoOrigen = nombreAeropuertoOrigen;
            this.nombreAeropuertoDestino = nombreAeropuertoDestino;
            this.codigoIataOrigen = codigoIataOrigen;
            this.codigoIataDestino = codigoIataDestino;
        }

        public Vuelo getVuelo() { return vuelo; }

        public String getNombreAeropuertoOrigen() { return nombreAeropuertoOrigen; }

        public String getNombreAeropuertoDestino() { return nombreAeropuertoDestino; }

        public String getCodigoIataOrigen() { return codigoIataOrigen; }

        public String getCodigoIataDestino() { return codigoIataDestino; }

        @Override
        public String toString() {
            return vuelo.getNumeroVuelo() + " - " +
                    codigoIataOrigen + " (" + nombreAeropuertoOrigen + ") -> " +
                    codigoIataDestino + " (" + nombreAeropuertoDestino + ")";
        }
    }

    @Override
    public List<VueloInfoUI> buscarVuelosConInfoAeropuertos(
            int idAeropuertoOrigen, int idAeropuertoDestino, LocalDate fecha) {

        List<VueloInfoUI> resultado = new ArrayList<>();
        try {
            List<Vuelo> vuelos = vueloDao.buscarVuelosPorOrigenDestinoYFecha(
                    idAeropuertoOrigen, idAeropuertoDestino, fecha);

            for (Vuelo v : vuelos) {
                Aeropuerto origen = aeropuertoDao.obtenerPorId(v.getIdAeropuertoOrigen());
                Aeropuerto destino = aeropuertoDao.obtenerPorId(v.getIdAeropuertoDestino());

                String nombreOrigen = (origen != null) ? origen.getNombreAeropuerto() : "Desconocido";
                String nombreDestino = (destino != null) ? destino.getNombreAeropuerto() : "Desconocido";
                String codigoOrigen = (origen != null) ? origen.getCodigoIATA() : "";
                String codigoDestino = (destino != null) ? destino.getCodigoIATA() : "";

                VueloInfoUI info = new VueloInfoUI(v, nombreOrigen, nombreDestino, codigoOrigen, codigoDestino);
                resultado.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar vuelos con información de aeropuertos", e);
        }
        return resultado;
    }

    @Override
    public Vuelo obtenerVueloPorId(int idVuelo) {
        try {
            return vueloDao.obtenerPorId(idVuelo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener vuelo", e);
        }
    }
}
