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
    public Aeronave() {}

    public Aeronave(int idAeronave, String modelo, int capacidadAsientos, int idAerolinea) {
        this.idAeronave = idAeronave;
        this.modelo = modelo;
        this.capacidadAsientos = capacidadAsientos;
        this.idAerolinea = idAerolinea;
    }

    // Getters y Setters
    public int getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(int idAeronave) {
        this.idAeronave = idAeronave;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidadAsientos() {
        return capacidadAsientos;
    }

    public void setCapacidadAsientos(int capacidadAsientos) {
        this.capacidadAsientos = capacidadAsientos;
    }

    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

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