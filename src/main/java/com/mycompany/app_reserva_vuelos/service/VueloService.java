/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Vuelo;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface VueloService {

    List<Vuelo> buscarVuelos(int idAeropuertoOrigen, int idAeropuertoDestino, LocalDate fecha);

    List<VueloServiceImpl.VueloInfoUI> buscarVuelosConInfoAeropuertos(
            int idAeropuertoOrigen, int idAeropuertoDestino, LocalDate fecha);

    Vuelo obtenerVueloPorId(int idVuelo);

    int registrarVuelo(Vuelo vuelo);

    void modificarVuelo(Vuelo vuelo);

    void eliminarVuelo(int idVuelo);

    List<Vuelo> listarVuelos();
}