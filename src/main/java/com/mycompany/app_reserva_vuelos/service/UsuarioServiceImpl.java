/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.UsuarioDao;
import com.mycompany.app_reserva_vuelos.dao.UsuarioDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Usuario;

/**
 *
 * @author tehca
 */
public class UsuarioServiceImpl implements UsuarioService { // Implementa la interfaz UsuarioService

    private final UsuarioDao usuarioDao;
    private Usuario usuarioAutenticado;

    public UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public boolean autenticar(String nombreUsuario, String contrasenia) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()
                || contrasenia == null || contrasenia.trim().isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = usuarioDao.obtenerPorCredenciales(nombreUsuario, contrasenia);
            if (usuario != null) {
                this.usuarioAutenticado = usuario;
                return true;
            } else {
                this.usuarioAutenticado = null;
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.usuarioAutenticado = null;
            return false;
        }
    }

    @Override
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    @Override
    public boolean hayUsuarioAutenticado() {
        return usuarioAutenticado != null;
    }

    @Override
    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

    @Override
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        try {
            return usuarioDao.obtenerPorNombreUsuario(nombreUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener usuario por nombre de usuario", e);
        }
    }
}