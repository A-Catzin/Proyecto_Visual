/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.dao.UsuarioDao;
import com.mycompany.app_reserva_vuelos.dao.UsuarioDaoImpl;
import com.mycompany.app_reserva_vuelos.model.Usuario;
import java.util.List;

/**
 *
 * @author tehca
 */
public class UsuarioServiceImpl implements UsuarioService {
    private static UsuarioServiceImpl instance;
    private final UsuarioDao usuarioDao;
    private Usuario usuarioAutenticado;

    private UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    public static UsuarioServiceImpl getInstance() {
        if (instance == null) {
            instance = new UsuarioServiceImpl();
        }
        return instance;
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
    public int registrarUsuario(Usuario usuario) {
        // Validar si el usuario ya existe
        if (usuarioDao.obtenerPorNombreUsuario(usuario.getUsuario()) != null) {
            return -1; // Usuario ya existe
        }
        return usuarioDao.registrar(usuario);
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        usuarioDao.modificar(usuario);
    }

    @Override
    public void eliminarUsuario(int idUsuario) {
        usuarioDao.eliminar(idUsuario);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listar();
    }
}