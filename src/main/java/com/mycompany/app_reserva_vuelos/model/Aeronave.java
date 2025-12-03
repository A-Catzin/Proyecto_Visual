/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Aeronave {
    private int idAeronave;
    private String modelo;
    private int capacidadAsientos;
    private int idAerolinea;

    // Constructores
    /** Constructor vacío para crear una instancia de Aeronave sin parámetros */
    public Aeronave() {}

    /** Constructor con parámetros para inicializar una Aeronave con datos específicos */
    public Aeronave(int idAeronave, String modelo, int capacidadAsientos, int idAerolinea) {
        this.idAeronave = idAeronave;
        this.modelo = modelo;
        this.capacidadAsientos = capacidadAsientos;
        this.idAerolinea = idAerolinea;
    }

    // Getters y Setters
    /** Obtiene el identificador único de la aeronave */
    public int getIdAeronave() {
        return idAeronave;
    }

    /** Establece el identificador único de la aeronave */
    public void setIdAeronave(int idAeronave) {
        this.idAeronave = idAeronave;
    }

    /** Obtiene el modelo de la aeronave */
    public String getModelo() {
        return modelo;
    }

    /** Establece el modelo de la aeronave */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /** Obtiene la capacidad de asientos de la aeronave */
    public int getCapacidadAsientos() {
        return capacidadAsientos;
    }

    /** Establece la capacidad de asientos de la aeronave */
    public void setCapacidadAsientos(int capacidadAsientos) {
        this.capacidadAsientos = capacidadAsientos;
    }

    /** Obtiene el identificador de la aerolinea a la que pertenece */
    public int getIdAerolinea() {
        return idAerolinea;
    }

    /** Establece el identificador de la aerolinea a la que pertenece */
    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    /** Retorna la representación en string de la aeronave */
    @Override
    public String toString() {
        return "Aeronave{" +
                "idAeronave=" + idAeronave +
                ", modelo='" + modelo + '\'' +
                ", capacidadAsientos=" + capacidadAsientos +
                ", idAerolinea=" + idAerolinea +
                '}';
    }
}