/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Pais {
    private int idPais;
    private String nombrePais;

    // Constructores
    /** Constructor vacío para crear una instancia de Pais sin parámetros */
    public Pais() {}

    /** Constructor con parámetros para inicializar un Pais con datos específicos */
    public Pais(int idPais, String nombrePais) {
        this.idPais = idPais;
        this.nombrePais = nombrePais;
    }

    // Getters y Setters
    /** Obtiene el identificador único del país */
    public int getIdPais() {
        return idPais;
    }

    /** Establece el identificador único del país */
    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    /** Obtiene el nombre del país */
    public String getNombrePais() {
        return nombrePais;
    }

    /** Establece el nombre del país */
    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    /** Retorna la representación en string del país */
    @Override
    public String toString() {
        return "Pais{" +
                "idPais=" + idPais +
                ", nombrePais='" + nombrePais + '\'' +
                '}';
    }
}
