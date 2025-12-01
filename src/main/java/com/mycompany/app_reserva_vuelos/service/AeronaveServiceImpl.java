/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.AeronaveDao;
import com.mycompany.app_reserva_vuelos.dao.AeronaveDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Aeronave;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AeronaveServiceImpl implements AeronaveService {

    private final AeronaveDao aeronaveDao;

    public AeronaveServiceImpl() {
        this.aeronaveDao = new AeronaveDaoImpl();
    }

    @Override
    public List<Aeronave> listar() {
        return aeronaveDao.listar();
    }

    @Override
    public Aeronave obtenerPorId(int idAeronave) {
        return aeronaveDao.obtenerPorId(idAeronave);
    }
}
