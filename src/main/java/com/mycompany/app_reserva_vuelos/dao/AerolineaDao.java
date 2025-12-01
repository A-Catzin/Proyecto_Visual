/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface AerolineaDao {
    int registrar(Aerolinea aerolinea);

    void modificar(Aerolinea aerolinea);

    void eliminar(int idAerolinea);

    List<Aerolinea> listar();

    Aerolinea obtenerPorId(int idAerolinea);
}
