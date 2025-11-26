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
    public ClaseAsiento() {}

    public ClaseAsiento(int idClase, String nombreClase) {
        this.idClase = idClase;
        this.nombreClase = nombreClase;
    }

    // Getters y Setters
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    @Override
    public String toString() {
        return "ClaseAsiento{" +
                "idClase=" + idClase +
                ", nombreClase='" + nombreClase + '\'' +
                '}';
    }
}