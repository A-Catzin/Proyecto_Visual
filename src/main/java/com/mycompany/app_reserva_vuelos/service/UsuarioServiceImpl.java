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
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDao usuarioDao;
    private Usuario usuarioAutenticado;

    public UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public boolean autenticar(String nombreUsuario, String contraseña) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()
                || contraseña == null || contraseña.trim().isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = usuarioDao.obtenerPorCredenciales(nombreUsuario, contraseña);
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

    @Override
    public int registrarUsuario(String nombre, String nombreUsuario, String email, String contraseña) {
        // Validación básica de campos
        if (nombre == null || nombre.trim().isEmpty()
                || nombreUsuario == null || nombreUsuario.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || contraseña == null || contraseña.trim().isEmpty()) {
            return -1; // datos inválidos
        }

        try {
            // Verificar si ya existe un usuario con ese nombre de usuario
            Usuario existente = usuarioDao.obtenerPorNombreUsuario(nombreUsuario);
            if (existente != null) {
                return -2; // nombre de usuario ya en uso
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setUsuario(nombreUsuario);
            nuevo.setEmail(email);
            nuevo.setContraseña(contraseña);

            int idGenerado = usuarioDao.registrar(nuevo);
            return (idGenerado > 0) ? idGenerado : -1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}