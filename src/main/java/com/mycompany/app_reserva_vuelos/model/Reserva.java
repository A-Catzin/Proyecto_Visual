/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;
import java.time.LocalDateTime;

/**
 *
 * @author tehca
 */
public class Reserva {
    private int idReserva;
    private String codigoReserva;
    private int idPasajero;
    private LocalDateTime fechaReserva;
    private String estadoReserva;

    // Constructores
    public Reserva() {}

    public Reserva(int idReserva, String codigoReserva, int idPasajero, LocalDateTime fechaReserva, String estadoReserva) {
        this.idReserva = idReserva;
        this.codigoReserva = codigoReserva;
        this.idPasajero = idPasajero;
        this.fechaReserva = fechaReserva;
        this.estadoReserva = estadoReserva;
    }

    // Getters y Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public int getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", codigoReserva='" + codigoReserva + '\'' +
                ", idPasajero=" + idPasajero +
                ", fechaReserva=" + fechaReserva +
                ", estadoReserva='" + estadoReserva + '\'' +
                '}';
    }
}