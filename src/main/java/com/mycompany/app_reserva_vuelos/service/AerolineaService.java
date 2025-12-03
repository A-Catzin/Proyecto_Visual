/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import java.util.List;

/**
 * Interfaz Service para operaciones de negocio de Aerolinea
 * @author tehca
 */
public interface AerolineaService {
    /** Registra una nueva aerolinea en el sistema */
    int registrarAerolinea(Aerolinea aerolinea);

    /** Modifica los datos de una aerolinea existente */
    void modificarAerolinea(Aerolinea aerolinea);

    /** Elimina una aerolinea por su identificador */
    void eliminarAerolinea(int idAerolinea);

    /** Obtiene la lista completa de aerolineas */
    List<Aerolinea> listarAerolineas();

    /** Obtiene una aerolinea específica por su identificador */
    Aerolinea obtenerAerolineaPorId(int idAerolinea);
}
