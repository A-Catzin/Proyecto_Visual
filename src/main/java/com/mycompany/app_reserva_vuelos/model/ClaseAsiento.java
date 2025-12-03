/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class ClaseAsiento {
    private int idClase;
    private String nombreClase;

    // Constructores
    /** Constructor vacío para crear una instancia de ClaseAsiento sin parámetros */
    public ClaseAsiento() {}

    /** Constructor con parámetros para inicializar una ClaseAsiento con datos específicos */
    public ClaseAsiento(int idClase, String nombreClase) {
        this.idClase = idClase;
        this.nombreClase = nombreClase;
    }

    // Getters y Setters
    /** Obtiene el identificador único de la clase de asiento */
    public int getIdClase() {
        return idClase;
    }

    /** Establece el identificador único de la clase de asiento */
    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    /** Obtiene el nombre de la clase de asiento */
    public String getNombreClase() {
        return nombreClase;
    }

    /** Establece el nombre de la clase de asiento */
    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    /** Retorna la representación en string de la clase de asiento */
    @Override
    public String toString() {
        return "ClaseAsiento{" +
                "idClase=" + idClase +
                ", nombreClase='" + nombreClase + '\'' +
                '}';
    }
}