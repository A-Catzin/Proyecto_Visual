/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Aeropuerto {
    private int idAeropuerto;
    private String codigoIATA;
    private String nombreAeropuerto;
    private int idCiudad;

    // Constructores
    public Aeropuerto() {}

    public Aeropuerto(int idAeropuerto, String codigoIATA, String nombreAeropuerto, int idCiudad) {
        this.idAeropuerto = idAeropuerto;
        this.codigoIATA = codigoIATA;
        this.nombreAeropuerto = nombreAeropuerto;
        this.idCiudad = idCiudad;
    }

    // Getters y Setters
    public int getIdAeropuerto() {
        return idAeropuerto;
    }

    public void setIdAeropuerto(int idAeropuerto) {
        this.idAeropuerto = idAeropuerto;
    }

    public String getCodigoIATA() {
        return codigoIATA;
    }

    public void setCodigoIATA(String codigoIATA) {
        this.codigoIATA = codigoIATA;
    }

    public String getNombreAeropuerto() {
        return nombreAeropuerto;
    }

    public void setNombreAeropuerto(String nombreAeropuerto) {
        this.nombreAeropuerto = nombreAeropuerto;
    }

    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    @Override
    public String toString() {
        return "Aeropuerto{" +
                "idAeropuerto=" + idAeropuerto +
                ", codigoIATA='" + codigoIATA + '\'' +
                ", nombreAeropuerto='" + nombreAeropuerto + '\'' +
                ", idCiudad=" + idCiudad +
                '}';
    }
}
