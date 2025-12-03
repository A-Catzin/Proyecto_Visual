/*
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Usuario;
import java.util.List;

/**
 * Interfaz Service para operaciones de negocio de Usuario
 * @author tehca
 */
public interface UsuarioService {
    /** Autentica un usuario con su nombre de usuario y contraseña */
    boolean autenticar(String nombreUsuario, String contraseña);

    /** Obtiene el usuario autenticado actualmente */
    Usuario getUsuarioAutenticado();

    /** Verifica si hay un usuario autenticado en la sesión actual */
    boolean hayUsuarioAutenticado();

    /** Cierra la sesión del usuario autenticado */
    void cerrarSesion();

    /** Obtiene un usuario específico por su nombre de usuario */
    Usuario obtenerPorNombreUsuario(String nombreUsuario);

    /** Registra un nuevo usuario en el sistema */
    int registrarUsuario(Usuario usuario);

    /** Modifica los datos de un usuario existente */
    void modificarUsuario(Usuario usuario);

    /** Elimina un usuario por su identificador */
    void eliminarUsuario(int idUsuario);

    /** Obtiene la lista completa de usuarios del sistema */
    List<Usuario> listarUsuarios();
}
