/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.dao;

import com.mycompany.app_reserva_vuelos.model.Usuario;

/**
 * Interfaz DAO para operaciones CRUD de Usuario
 */
public interface UsuarioDao {
    /** Obtiene un usuario por sus credenciales de login (usuario y contraseña) */
    Usuario obtenerPorCredenciales(String usuario, String contraseña);

    /** Obtiene un usuario específico por su nombre de usuario */
    Usuario obtenerPorNombreUsuario(String usuario);

    /** Registra un nuevo usuario en la base de datos */
    int registrar(Usuario usuario);

    /** Modifica los datos de un usuario existente */
    void modificar(Usuario usuario);

    /** Elimina un usuario por su identificador */
    void eliminar(int idUsuario);

    /** Obtiene la lista completa de usuarios */
    java.util.List<Usuario> listar();
}