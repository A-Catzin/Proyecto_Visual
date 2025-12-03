/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;
import java.time.LocalDate;

/**
 *
 * @author tehca
 */
public class Pasajero {
    private int idPasajero;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;

    // Constructores
    /** Constructor vacío para crear una instancia de Pasajero sin parámetros */
    public Pasajero() {}

    /** Constructor con parámetros para inicializar un Pasajero con datos específicos */
    public Pasajero(int idPasajero, String nombre, String apellido, String email, LocalDate fechaNacimiento) {
        this.idPasajero = idPasajero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y Setters
    /** Obtiene el identificador único del pasajero */
    public int getIdPasajero() {
        return idPasajero;
    }

    /** Establece el identificador único del pasajero */
    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    /** Obtiene el nombre del pasajero */
    public String getNombre() {
        return nombre;
    }

    /** Establece el nombre del pasajero */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Obtiene el apellido del pasajero */
    public String getApellido() {
        return apellido;
    }

    /** Establece el apellido del pasajero */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /** Obtiene el correo electrónico del pasajero */
    public String getEmail() {
        return email;
    }

    /** Establece el correo electrónico del pasajero */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Obtiene la fecha de nacimiento del pasajero */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** Establece la fecha de nacimiento del pasajero */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** Retorna la representación en string del pasajero */
    @Override
    public String toString() {
        return "Pasajero{" +
                "idPasajero=" + idPasajero +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                '}';
    }
}
