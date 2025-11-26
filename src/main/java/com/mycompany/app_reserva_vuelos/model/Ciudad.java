/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Ciudad {
    private int idCiudad;
    private String nombreCiudad;
    private int idPais;

    // Constructores
    public Ciudad() {}

    public Ciudad(int idCiudad, String nombreCiudad, int idPais) {
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.idPais = idPais;
    }

    // Getters y Setters
    public int getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "idCiudad=" + idCiudad +
                ", nombreCiudad='" + nombreCiudad + '\'' +
                ", idPais=" + idPais +
                '}';
    }
}
