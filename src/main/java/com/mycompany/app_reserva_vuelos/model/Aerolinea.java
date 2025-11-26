/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Aerolinea {
    private int idAerolinea;
    private String nombreAerolinea;

    // Constructores
    public Aerolinea() {}

    public Aerolinea(int idAerolinea, String nombreAerolinea) {
        this.idAerolinea = idAerolinea;
        this.nombreAerolinea = nombreAerolinea;
    }

    // Getters y Setters
    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public String getNombreAerolinea() {
        return nombreAerolinea;
    }

    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }

    @Override
    public String toString() {
        return "Aerolinea{" +
                "idAerolinea=" + idAerolinea +
                ", nombreAerolinea='" + nombreAerolinea + '\'' +
                '}';
    }
}
