/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Usuario;

public interface UsuarioDao {
    Usuario obtenerPorCredenciales(String usuario, String contraseña);

    Usuario obtenerPorNombreUsuario(String usuario);

    int registrar(Usuario usuario);

    void modificar(Usuario usuario);

    void eliminar(int idUsuario);

    java.util.List<Usuario> listar();
}