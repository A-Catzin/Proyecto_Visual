/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

import java.math.BigDecimal;

/**
 *
 * @author tehca
 */
public class Tarifa {
    private int idTarifa;
    private int idVuelo;
    private int idClase;
    private BigDecimal precio;
    private String temporada;
    private BigDecimal descuento;
    private BigDecimal recargo;

    // Constructores
    /** Constructor vacío para crear una instancia de Tarifa sin parámetros */
    public Tarifa() {
    }

    /** Constructor con parámetros para inicializar una Tarifa con datos específicos */
    public Tarifa(int idTarifa, int idVuelo, int idClase, BigDecimal precio, String temporada, BigDecimal descuento,
            BigDecimal recargo) {
        this.idTarifa = idTarifa;
        this.idVuelo = idVuelo;
        this.idClase = idClase;
        this.precio = precio;
        this.temporada = temporada;
        this.descuento = descuento;
        this.recargo = recargo;
    }

    // Getters y Setters
    /** Obtiene el identificador único de la tarifa */
    public int getIdTarifa() {
        return idTarifa;
    }

    /** Establece el identificador único de la tarifa */
    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    /** Obtiene el identificador del vuelo asociado */
    public int getIdVuelo() {
        return idVuelo;
    }

    /** Establece el identificador del vuelo asociado */
    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    /** Obtiene el identificador de la clase de asiento */
    public int getIdClase() {
        return idClase;
    }

    /** Establece el identificador de la clase de asiento */
    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    /** Obtiene el precio de la tarifa */
    public BigDecimal getPrecio() {
        return precio;
    }

    /** Establece el precio de la tarifa */
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    /** Obtiene la temporada aplicable para la tarifa */
    public String getTemporada() {
        return temporada;
    }

    /** Establece la temporada aplicable para la tarifa */
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    /** Obtiene el descuento aplicable a la tarifa */
    public BigDecimal getDescuento() {
        return descuento;
    }

    /** Establece el descuento aplicable a la tarifa */
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    /** Obtiene el recargo adicional de la tarifa */
    public BigDecimal getRecargo() {
        return recargo;
    }

    /** Establece el recargo adicional de la tarifa */
    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    /** Retorna la representación en string de la tarifa */
    @Override
    public String toString() {
        return "Tarifa{" +
                "idTarifa=" + idTarifa +
                ", idVuelo=" + idVuelo +
                ", idClase=" + idClase +
                ", precio=" + precio +
                ", temporada='" + temporada + '\'' +
                ", descuento=" + descuento +
                ", recargo=" + recargo +
                '}';
    }
}