/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author tehca
 */
public interface VueloDao {

    /**
     * Busca vuelos por aeropuerto de origen, aeropuerto de destino y fecha (día completo).
     * La fecha se compara solo por el día (ignora la hora).
     */
    List<Vuelo> buscarVuelosPorOrigenDestinoYFecha(int idAeropuertoOrigen,int idAeropuertoDestino,LocalDate fecha);
    /**
     * Obtiene un vuelo por su ID.
     *
     * @param idVuelo ID del vuelo
     * @return el vuelo encontrado o null si no existe
     */
    Vuelo obtenerPorId(int idVuelo);
}