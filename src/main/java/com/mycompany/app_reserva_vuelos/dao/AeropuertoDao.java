/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface AeropuertoDao {

    Aeropuerto obtenerPorId(int idAeropuerto);

    Aeropuerto obtenerPorCodigoIATA(String codigoIata);

    List<Aeropuerto> listarTodos();

    /**
     * Lista aeropuertos de una ciudad concreta (opcional, pero útil).
     */
    List<Aeropuerto> listarPorIdCiudad(int idCiudad);
}