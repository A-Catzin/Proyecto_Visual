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
public interface UsuarioService { // Nombre de la interfaz sin 'I'

    boolean autenticar(String nombreUsuario, String contrasenia);

    Usuario getUsuarioAutenticado();

    boolean hayUsuarioAutenticado();

    void cerrarSesion();

    Usuario obtenerPorNombreUsuario(String nombreUsuario);
}
