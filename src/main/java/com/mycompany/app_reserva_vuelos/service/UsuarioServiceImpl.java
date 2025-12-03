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
 * Implementación Service para operaciones de negocio de Usuario
 * @author tehca
 */
public class UsuarioServiceImpl implements UsuarioService {
    private static UsuarioServiceImpl instance;
    private final UsuarioDao usuarioDao;
    private Usuario usuarioAutenticado;

    /** Constructor privado para Singleton */
    private UsuarioServiceImpl() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    /** Retorna la instancia única del servicio (patrón Singleton) */
    public static UsuarioServiceImpl getInstance() {
        if (instance == null) {
            instance = new UsuarioServiceImpl();
        }
        return instance;
    }

    /** Constructor para inyección de dependencias (testing) */
    public UsuarioServiceImpl(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /** Autentica un usuario validando sus credenciales */
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
    /** Retorna el usuario autenticado actualmente */
    @Override
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    /** Verifica si hay un usuario autenticado en la sesión actual */
    @Override
    public boolean hayUsuarioAutenticado() {
        return usuarioAutenticado != null;
    }
    /** Cierra la sesión del usuario autenticado */
    @Override
    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }
    /** Obtiene un usuario específico por su nombre de usuario */
    @Override
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        try {
            return usuarioDao.obtenerPorNombreUsuario(nombreUsuario);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener usuario por nombre de usuario", e);
        }
    }
    /** Registra un nuevo usuario validando que no exista otro con el mismo nombre */
    @Override
    public int registrarUsuario(Usuario usuario) {
        // Validar si el usuario ya existe
        if (usuarioDao.obtenerPorNombreUsuario(usuario.getUsuario()) != null) {
            return -1; // Usuario ya existe
        }
        return usuarioDao.registrar(usuario);
    }
    /** Modifica los datos de un usuario existente */
    @Override
    public void modificarUsuario(Usuario usuario) {
        usuarioDao.modificar(usuario);
    }
    
    /** Elimina un usuario por su identificador */
    @Override
    public void eliminarUsuario(int idUsuario) {
        usuarioDao.eliminar(idUsuario);
    }
    /** Obtiene la lista completa de usuarios del sistema */
    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioDao.listar();
    }
}