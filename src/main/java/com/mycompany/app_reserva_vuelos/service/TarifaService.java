/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Tarifa;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface TarifaService {
    int registrarTarifa(Tarifa tarifa);

    void modificarTarifa(Tarifa tarifa);

    void eliminarTarifa(int idTarifa);

    List<Tarifa> listarTarifas();

    Tarifa obtenerTarifaPorId(int idTarifa);

    List<Tarifa> listarTarifasPorVuelo(int idVuelo);
}
