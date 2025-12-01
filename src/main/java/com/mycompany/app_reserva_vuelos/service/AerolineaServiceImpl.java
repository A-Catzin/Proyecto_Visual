/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.AerolineaDao;
import com.mycompany.app_reserva_vuelos.dao.AerolineaDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import java.util.List;

/**
 *
 * @author tehca
 */
public class AerolineaServiceImpl implements AerolineaService {

    private final AerolineaDao aerolineaDao;

    public AerolineaServiceImpl() {
        this.aerolineaDao = new AerolineaDaoImpl();
    }

    @Override
    public int registrarAerolinea(Aerolinea aerolinea) {
        return aerolineaDao.registrar(aerolinea);
    }

    @Override
    public void modificarAerolinea(Aerolinea aerolinea) {
        aerolineaDao.modificar(aerolinea);
    }

    @Override
    public void eliminarAerolinea(int idAerolinea) {
        aerolineaDao.eliminar(idAerolinea);
    }

    @Override
    public List<Aerolinea> listarAerolineas() {
        return aerolineaDao.listar();
    }

    @Override
    public Aerolinea obtenerAerolineaPorId(int idAerolinea) {
        return aerolineaDao.obtenerPorId(idAerolinea);
    }
}
