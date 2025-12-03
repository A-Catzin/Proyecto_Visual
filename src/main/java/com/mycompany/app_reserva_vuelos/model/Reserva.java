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
    /** Constructor vacío para crear una instancia de Reserva sin parámetros */
    public Reserva() {}

    /** Constructor con parámetros para inicializar una Reserva con datos específicos */
    public Reserva(int idReserva, String codigoReserva, int idPasajero, LocalDateTime fechaReserva, String estadoReserva) {
        this.idReserva = idReserva;
        this.codigoReserva = codigoReserva;
        this.idPasajero = idPasajero;
        this.fechaReserva = fechaReserva;
        this.estadoReserva = estadoReserva;
    }

    // Getters y Setters
    /** Obtiene el identificador único de la reserva */
    public int getIdReserva() {
        return idReserva;
    }

    /** Establece el identificador único de la reserva */
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    /** Obtiene el código de la reserva */
    public String getCodigoReserva() {
        return codigoReserva;
    }

    /** Establece el código de la reserva */
    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    /** Obtiene el identificador del pasajero que realizó la reserva */
    public int getIdPasajero() {
        return idPasajero;
    }

    /** Establece el identificador del pasajero que realizó la reserva */
    public void setIdPasajero(int idPasajero) {
        this.idPasajero = idPasajero;
    }

    /** Obtiene la fecha y hora en que se realizó la reserva */
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    /** Establece la fecha y hora en que se realizó la reserva */
    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    /** Obtiene el estado actual de la reserva */
    public String getEstadoReserva() {
        return estadoReserva;
    }

    /** Establece el estado actual de la reserva */
    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    /** Retorna la representación en string de la reserva */
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