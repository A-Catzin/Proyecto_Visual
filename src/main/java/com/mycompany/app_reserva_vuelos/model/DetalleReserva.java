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
    /** Constructor vacío para crear una instancia de DetalleReserva sin parámetros */
    public DetalleReserva() {}

    /** Constructor con parámetros para inicializar DetalleReserva con datos específicos */
    public DetalleReserva(int idDetalle, int idReserva, int idVuelo, int idTarifa, String numeroAsiento) {
        this.idDetalle = idDetalle;
        this.idReserva = idReserva;
        this.idVuelo = idVuelo;
        this.idTarifa = idTarifa;
        this.numeroAsiento = numeroAsiento;
    }

    // Getters y Setters
    /** Obtiene el identificador único del detalle de reserva */
    public int getIdDetalle() {
        return idDetalle;
    }

    /** Establece el identificador único del detalle de reserva */
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    /** Obtiene el identificador de la reserva asociada */
    public int getIdReserva() {
        return idReserva;
    }

    /** Establece el identificador de la reserva asociada */
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    /** Obtiene el identificador del vuelo */
    public int getIdVuelo() {
        return idVuelo;
    }

    /** Establece el identificador del vuelo */
    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    /** Obtiene el identificador de la tarifa */
    public int getIdTarifa() {
        return idTarifa;
    }

    /** Establece el identificador de la tarifa */
    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    /** Obtiene el número de asiento asignado */
    public String getNumeroAsiento() {
        return numeroAsiento;
    }

    /** Establece el número de asiento asignado */
    public void setNumeroAsiento(String numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }

    /** Retorna la representación en string del detalle de reserva */
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
