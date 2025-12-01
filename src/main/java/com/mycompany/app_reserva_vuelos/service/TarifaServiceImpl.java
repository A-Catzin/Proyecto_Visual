/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.TarifaDao;
import com.mycompany.app_reserva_vuelos.dao.TarifaDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Tarifa;
import java.util.List;

/**
 *
 * @author tehca
 */
public class TarifaServiceImpl implements TarifaService {

    private final TarifaDao tarifaDao;

    public TarifaServiceImpl() {
        this.tarifaDao = new TarifaDaoImpl();
    }

    @Override
    public int registrarTarifa(Tarifa tarifa) {
        return tarifaDao.registrar(tarifa);
    }

    @Override
    public void modificarTarifa(Tarifa tarifa) {
        tarifaDao.modificar(tarifa);
    }

    @Override
    public void eliminarTarifa(int idTarifa) {
        tarifaDao.eliminar(idTarifa);
    }

    @Override
    public List<Tarifa> listarTarifas() {
        return tarifaDao.listar();
    }

    @Override
    public Tarifa obtenerTarifaPorId(int idTarifa) {
        return tarifaDao.obtenerPorId(idTarifa);
    }

    @Override
    public List<Tarifa> listarTarifasPorVuelo(int idVuelo) {
        return tarifaDao.listarPorIdVuelo(idVuelo);
    }
}
