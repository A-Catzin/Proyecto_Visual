/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import java.util.List;

/**
 * Interfaz DAO para operaciones CRUD de Aerolinea
 * @author tehca
 */
public interface AerolineaDao {
    /** Registra una nueva aerolinea en la base de datos y retorna su ID generado */
    int registrar(Aerolinea aerolinea);

    /** Modifica los datos de una aerolinea existente */
    void modificar(Aerolinea aerolinea);

    /** Elimina una aerolinea por su identificador */
    void eliminar(int idAerolinea);

    /** Obtiene la lista completa de aerolineas */
    List<Aerolinea> listar();

    /** Obtiene una aerolinea específica por su identificador */
    Aerolinea obtenerPorId(int idAerolinea);
}
