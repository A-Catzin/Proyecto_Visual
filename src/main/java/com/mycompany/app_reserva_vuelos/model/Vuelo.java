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
    /** Constructor vacío para crear una instancia de Vuelo sin parámetros */
    public Vuelo() {}

    /** Constructor con parámetros para inicializar un Vuelo con datos específicos */
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
    /** Obtiene el identificador único del vuelo */
    public int getIdVuelo() {
        return idVuelo;
    }

    /** Establece el identificador único del vuelo */
    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    /** Obtiene el número del vuelo */
    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    /** Establece el número del vuelo */
    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    /** Obtiene el identificador del aeropuerto de origen */
    public int getIdAeropuertoOrigen() {
        return idAeropuertoOrigen;
    }

    /** Establece el identificador del aeropuerto de origen */
    public void setIdAeropuertoOrigen(int idAeropuertoOrigen) {
        this.idAeropuertoOrigen = idAeropuertoOrigen;
    }

    /** Obtiene el identificador del aeropuerto de destino */
    public int getIdAeropuertoDestino() {
        return idAeropuertoDestino;
    }

    /** Establece el identificador del aeropuerto de destino */
    public void setIdAeropuertoDestino(int idAeropuertoDestino) {
        this.idAeropuertoDestino = idAeropuertoDestino;
    }

    /** Obtiene el identificador de la aeronave asignada */
    public int getIdAeronave() {
        return idAeronave;
    }

    /** Establece el identificador de la aeronave asignada */
    public void setIdAeronave(int idAeronave) {
        this.idAeronave = idAeronave;
    }

    /** Obtiene la fecha y hora de salida del vuelo */
    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    /** Establece la fecha y hora de salida del vuelo */
    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    /** Obtiene la fecha y hora de llegada del vuelo */
    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    /** Establece la fecha y hora de llegada del vuelo */
    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    /** Retorna la representación en string del vuelo */
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
