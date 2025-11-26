/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Usuario;

/**
 *
 * @author tehca
 */
public interface UsuarioService {

    /**
     * Intenta autenticar a un usuario con su nombre de usuario y contraseña.
     *
     * @param nombreUsuario nombre de usuario ingresado
     * @param contraseña    contraseña ingresada
     * @return true si las credenciales son válidas, false en caso contrario
     */
    boolean autenticar(String nombreUsuario, String contraseña);

    /**
     * Devuelve el usuario actualmente autenticado, o null si no hay sesión.
     */
    Usuario getUsuarioAutenticado();

    /**
     * Indica si hay un usuario autenticado en la sesión.
     */
    boolean hayUsuarioAutenticado();

    /**
     * Cierra la sesión actual.
     */
    void cerrarSesion();

    /**
     * Obtiene un usuario por su nombre de usuario.
     */
    Usuario obtenerPorNombreUsuario(String nombreUsuario);

    /**
     * Registra un nuevo usuario aplicando validaciones básicas
     * (por ejemplo, que no exista ya el nombre de usuario).
     *
     * @return id generado (>0) si todo va bien,
     *         -1 si hubo error general,
     *         -2 si el nombre de usuario ya existe.
     */
    int registrarUsuario(String nombre, String nombreUsuario, String email, String contraseña);
}
