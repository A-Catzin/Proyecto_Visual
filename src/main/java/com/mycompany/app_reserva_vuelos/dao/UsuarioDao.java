/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Usuario;

/**
 *
 * @author tehca
 */
public interface UsuarioDao {

    /**
     * Busca un usuario por su nombre de usuario y contraseña.
     * Devuelve el objeto Usuario si las credenciales son correctas,
     * o null si no hay coincidencia.
     */
    Usuario obtenerPorCredenciales(String usuario, String contraseña);

    /**
     * Busca un usuario por su nombre de usuario (sin validar contraseña).
     */
    Usuario obtenerPorNombreUsuario(String usuario);
}