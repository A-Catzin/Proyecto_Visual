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

    // Constructores
    public Tarifa() {}

    public Tarifa(int idTarifa, int idVuelo, int idClase, BigDecimal precio) {
        this.idTarifa = idTarifa;
        this.idVuelo = idVuelo;
        this.idClase = idClase;
        this.precio = precio;
    }

    // Getters y Setters
    public int getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(int idTarifa) {
        this.idTarifa = idTarifa;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Tarifa{" +
                "idTarifa=" + idTarifa +
                ", idVuelo=" + idVuelo +
                ", idClase=" + idClase +
                ", precio=" + precio +
                '}';
    }
}