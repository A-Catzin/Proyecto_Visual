/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Tarifa;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface TarifaDao {

    Tarifa obtenerPorId(int idTarifa);

    /**
     * Lista todas las tarifas asociadas a un vuelo.
     */
    List<Tarifa> listarPorIdVuelo(int idVuelo);

    /**
     * Obtiene una tarifa específica por vuelo + clase.
     */
    Tarifa obtenerPorVueloYClase(int idVuelo, int idClase);
}
