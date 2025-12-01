/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.AeropuertoDao;
import com.mycompany.app_reserva_vuelos.dao.AeropuertoDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AeropuertoServiceImpl implements AeropuertoService {

    private final AeropuertoDao aeropuertoDao;

    public AeropuertoServiceImpl() {
        this.aeropuertoDao = new AeropuertoDaoImpl();
    }

    @Override
    public List<Aeropuerto> listarTodos() {
        return aeropuertoDao.listarTodos();
    }

    @Override
    public Aeropuerto obtenerPorId(int idAeropuerto) {
        return aeropuertoDao.obtenerPorId(idAeropuerto);
    }

    @Override
    public Aeropuerto obtenerPorCodigoIATA(String codigoIata) {
        return aeropuertoDao.obtenerPorCodigoIATA(codigoIata);
    }
}
