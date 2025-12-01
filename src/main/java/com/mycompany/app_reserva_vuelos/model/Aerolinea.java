/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.model;

/**
 *
 * @author tehca
 */
public class Aerolinea {
    private int idAerolinea;
    private String nombreAerolinea;
    private String codigoIata;
    private String pais;
    private String estado;
    private String telefono;
    private String email;

    // Constructores
    public Aerolinea() {
    }

    public Aerolinea(int idAerolinea, String nombreAerolinea, String codigoIata, String pais, String estado,
            String telefono, String email) {
        this.idAerolinea = idAerolinea;
        this.nombreAerolinea = nombreAerolinea;
        this.codigoIata = codigoIata;
        this.pais = pais;
        this.estado = estado;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y Setters
    public int getIdAerolinea() {
        return idAerolinea;
    }

    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    public String getNombreAerolinea() {
        return nombreAerolinea;
    }

    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }

    public String getCodigoIata() {
        return codigoIata;
    }

    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Aerolinea{" +
                "idAerolinea=" + idAerolinea +
                ", nombreAerolinea='" + nombreAerolinea + '\'' +
                ", codigoIata='" + codigoIata + '\'' +
                ", pais='" + pais + '\'' +
                ", estado='" + estado + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
