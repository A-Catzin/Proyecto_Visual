/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String email;
    private String contraseña;
    private String telefono;
    private String rol;
    private String estado;

    // Constructores
    /** Constructor vacío para crear una instancia de Usuario sin parámetros */
    public Usuario() {
    }

    /** Constructor con parámetros para inicializar un Usuario con datos específicos */
    public Usuario(int id, String nombre, String usuario, String email, String contraseña, String telefono, String rol,
            String estado) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.email = email;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y Setters
    /** Obtiene el identificador único del usuario */
    public int getId() {
        return id;
    }

    /** Establece el identificador único del usuario */
    public void setId(int id) {
        this.id = id;
    }

    /** Obtiene el nombre completo del usuario */
    public String getNombre() {
        return nombre;
    }

    /** Establece el nombre completo del usuario */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Obtiene el nombre de usuario para login */
    public String getUsuario() {
        return usuario;
    }

    /** Establece el nombre de usuario para login */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /** Obtiene el correo electrónico del usuario */
    public String getEmail() {
        return email;
    }

    /** Establece el correo electrónico del usuario */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Obtiene la contraseña del usuario */
    public String getContraseña() {
        return contraseña;
    }

    /** Establece la contraseña del usuario */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /** Obtiene el número de teléfono del usuario */
    public String getTelefono() {
        return telefono;
    }

    /** Establece el número de teléfono del usuario */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** Obtiene el rol del usuario en el sistema */
    public String getRol() {
        return rol;
    }

    /** Establece el rol del usuario en el sistema */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /** Obtiene el estado del usuario */
    public String getEstado() {
        return estado;
    }

    /** Establece el estado del usuario */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Retorna la representación en string del usuario */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
