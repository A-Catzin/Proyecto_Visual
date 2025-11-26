/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class DetalleReserva {
    private int idDetalle;
    private int idReserva;
    private int idVuelo;
    private int idTarifa;
    private String numeroAsiento;

    // Constructores
    public DetalleReserva() {}

    public DetalleReserva(int idDetalle, int idReserva, int idVuelo, int idTarifa, String numeroAsiento) {
        this.idDetalle = idDetalle;
        this.idReserva = idReserva;
        this.idVuelo = idVuelo;
        this.idTarifa = idTarifa;
        this.numeroAsiento = numeroAsiento;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getNumeroAsiento() {
        return numeroAsiento;
    }

    public void setNumeroAsiento(String numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    @Override
    public String toString() {
        return "DetalleReserva{" +
                "idDetalle=" + idDetalle +
                ", idReserva=" + idReserva +
                ", idVuelo=" + idVuelo +
                ", idTarifa=" + idTarifa +
                ", numeroAsiento='" + numeroAsiento + '\'' +
                '}';
    }
}
