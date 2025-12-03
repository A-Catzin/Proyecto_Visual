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
    /** Constructor vacío para crear una instancia de Ciudad sin parámetros */
    public Ciudad() {}

    /** Constructor con parámetros para inicializar una Ciudad con datos específicos */
    public Ciudad(int idCiudad, String nombreCiudad, int idPais) {
        this.idCiudad = idCiudad;
        this.nombreCiudad = nombreCiudad;
        this.idPais = idPais;
    }

    // Getters y Setters
    /** Obtiene el identificador único de la ciudad */
    public int getIdCiudad() {
        return idCiudad;
    }

    /** Establece el identificador único de la ciudad */
    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    /** Obtiene el nombre de la ciudad */
    public String getNombreCiudad() {
        return nombreCiudad;
    }

    /** Establece el nombre de la ciudad */
    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    /** Obtiene el identificador del país al que pertenece */
    public int getIdPais() {
        return idPais;
    }

    /** Establece el identificador del país al que pertenece */
    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    /** Retorna la representación en string de la ciudad */
    @Override
    public String toString() {
        return "Ciudad{" +
                "idCiudad=" + idCiudad +
                ", nombreCiudad='" + nombreCiudad + '\'' +
                ", idPais=" + idPais +
                '}';
    }
}
