/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Aeronave;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface AeronaveService {
    List<Aeronave> listar();

    Aeronave obtenerPorId(int idAeronave);
}
