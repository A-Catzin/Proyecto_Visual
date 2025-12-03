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
    /** Constructor vacío para crear una instancia de Aeropuerto sin parámetros */
    public Aeropuerto() {}

    /** Constructor con parámetros para inicializar un Aeropuerto con datos específicos */
    public Aeropuerto(int idAeropuerto, String codigoIATA, String nombreAeropuerto, int idCiudad) {
        this.idAeropuerto = idAeropuerto;
        this.codigoIATA = codigoIATA;
        this.nombreAeropuerto = nombreAeropuerto;
        this.idCiudad = idCiudad;
    }

    // Getters y Setters
    /** Obtiene el identificador único del aeropuerto */
    public int getIdAeropuerto() {
        return idAeropuerto;
    }

    /** Establece el identificador único del aeropuerto */
    public void setIdAeropuerto(int idAeropuerto) {
        this.idAeropuerto = idAeropuerto;
    }

    /** Obtiene el código IATA del aeropuerto */
    public String getCodigoIATA() {
        return codigoIATA;
    }

    /** Establece el código IATA del aeropuerto */
    public void setCodigoIATA(String codigoIATA) {
        this.codigoIATA = codigoIATA;
    }

    /** Obtiene el nombre del aeropuerto */
    public String getNombreAeropuerto() {
        return nombreAeropuerto;
    }

    /** Establece el nombre del aeropuerto */
    public void setNombreAeropuerto(String nombreAeropuerto) {
        this.nombreAeropuerto = nombreAeropuerto;
    }

    /** Obtiene el identificador de la ciudad donde se ubica */
    public int getIdCiudad() {
        return idCiudad;
    }

    /** Establece el identificador de la ciudad donde se ubica */
    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad;
    }

    /** Retorna la representación en string del aeropuerto */
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
