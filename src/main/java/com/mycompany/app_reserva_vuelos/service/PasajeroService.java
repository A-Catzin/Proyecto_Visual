/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Pasajero;

import java.util.List;

/**
 *
 * @author tehca
 */
public interface PasajeroService { // Nombre de la interfaz sin 'I'

    int registrarPasajero(Pasajero pasajero);

    Pasajero obtenerPasajeroPorId(int idPasajero);

    Pasajero obtenerPasajeroPorEmail(String email);

    List<Pasajero> listarTodos();
}
