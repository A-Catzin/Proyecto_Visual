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
public class Vuelo {
    private int idVuelo;
    private String numeroVuelo;
    private int idAeropuertoOrigen;
    private int idAeropuertoDestino;
    private int idAeronave;
    private LocalDateTime fechaHoraSalida;
    private LocalDateTime fechaHoraLlegada;

    // Constructores
    public Vuelo() {}

    public Vuelo(int idVuelo, String numeroVuelo, int idAeropuertoOrigen, int idAeropuertoDestino, 
                 int idAeronave, LocalDateTime fechaHoraSalida, LocalDateTime fechaHoraLlegada) {
        this.idVuelo = idVuelo;
        this.numeroVuelo = numeroVuelo;
        this.idAeropuertoOrigen = idAeropuertoOrigen;
        this.idAeropuertoDestino = idAeropuertoDestino;
        this.idAeronave = idAeronave;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    // Getters y Setters
    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public int getIdAeropuertoOrigen() {
        return idAeropuertoOrigen;
    }

    public void setIdAeropuertoOrigen(int idAeropuertoOrigen) {
        this.idAeropuertoOrigen = idAeropuertoOrigen;
    }

    public int getIdAeropuertoDestino() {
        return idAeropuertoDestino;
    }

    public void setIdAeropuertoDestino(int idAeropuertoDestino) {
        this.idAeropuertoDestino = idAeropuertoDestino;
    }

    public int getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(int idAeronave) {
        this.idAeronave = idAeronave;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    @Override
    public String toString() {
        return "Vuelo{" +
                "idVuelo=" + idVuelo +
                ", numeroVuelo='" + numeroVuelo + '\'' +
                ", idAeropuertoOrigen=" + idAeropuertoOrigen +
                ", idAeropuertoDestino=" + idAeropuertoDestino +
                ", idAeronave=" + idAeronave +
                ", fechaHoraSalida=" + fechaHoraSalida +
                ", fechaHoraLlegada=" + fechaHoraLlegada +
                '}';
    }
}
