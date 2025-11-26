/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Pasajero;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface PasajeroDao {

    int crearPasajero(Pasajero pasajero);

    Pasajero obtenerPorId(int idPasajero);

    Pasajero obtenerPorEmail(String email);

    List<Pasajero> listarTodos();
}