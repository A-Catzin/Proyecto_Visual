/*
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.service;

import com.mycompany.app_reserva_vuelos.model.Usuario;
import java.util.List;

/**
 *
 * @author tehca
 */
public interface UsuarioService {
    boolean autenticar(String nombreUsuario, String contraseña);

    Usuario getUsuarioAutenticado();

    boolean hayUsuarioAutenticado();

    void cerrarSesion();

    Usuario obtenerPorNombreUsuario(String nombreUsuario);

    int registrarUsuario(Usuario usuario);

    void modificarUsuario(Usuario usuario);

    void eliminarUsuario(int idUsuario);

    List<Usuario> listarUsuarios();
}
