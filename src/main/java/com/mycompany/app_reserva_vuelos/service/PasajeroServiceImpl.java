/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.PasajeroDao;
import com.mycompany.app_reserva_vuelos.dao.PasajeroDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Pasajero;

import java.util.List;

/**
 *
 * @author tehca
 */
public class PasajeroServiceImpl implements PasajeroService { // Implementa la interfaz PasajeroService

    private final PasajeroDao pasajeroDao;

    public PasajeroServiceImpl() {
        this.pasajeroDao = new PasajeroDaoImpl();
    }

    public PasajeroServiceImpl(PasajeroDao pasajeroDao) {
        this.pasajeroDao = pasajeroDao;
    }

    @Override
    public int registrarPasajero(Pasajero pasajero) {
        if (pasajero == null) {
            throw new IllegalArgumentException("El pasajero no puede ser nulo");
        }
        if (pasajero.getEmail() == null || pasajero.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del pasajero es obligatorio");
        }

        try {
            Pasajero existente = pasajeroDao.obtenerPorEmail(pasajero.getEmail());
            if (existente != null) {
                throw new IllegalStateException("Ya existe un pasajero registrado con ese email");
            }
            return pasajeroDao.crearPasajero(pasajero);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al registrar pasajero", e);
        }
    }

    @Override
    public Pasajero obtenerPasajeroPorId(int idPasajero) {
        try {
            return pasajeroDao.obtenerPorId(idPasajero);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener pasajero por ID", e);
        }
    }

    @Override
    public Pasajero obtenerPasajeroPorEmail(String email) {
        try {
            return pasajeroDao.obtenerPorEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener pasajero por email", e);
        }
    }

    @Override
    public List<Pasajero> listarTodos() {
        try {
            return pasajeroDao.listarTodos();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar pasajeros", e);
        }
    }
}
