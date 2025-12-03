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
    /** Constructor vacío para crear una instancia de Aerolinea sin parámetros */
    public Aerolinea() {
    }

    /** Constructor con parámetros para inicializar una Aerolinea con datos específicos */
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
    /** Obtiene el identificador único de la aerolinea */
    public int getIdAerolinea() {
        return idAerolinea;
    }

    /** Establece el identificador único de la aerolinea */
    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea;
    }

    /** Obtiene el nombre de la aerolinea */
    public String getNombreAerolinea() {
        return nombreAerolinea;
    }

    /** Establece el nombre de la aerolinea */
    public void setNombreAerolinea(String nombreAerolinea) {
        this.nombreAerolinea = nombreAerolinea;
    }

    /** Obtiene el código IATA de la aerolinea */
    public String getCodigoIata() {
        return codigoIata;
    }

    /** Establece el código IATA de la aerolinea */
    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata;
    }

    /** Obtiene el país donde opera la aerolinea */
    public String getPais() {
        return pais;
    }

    /** Establece el país donde opera la aerolinea */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /** Obtiene el estado operacional de la aerolinea */
    public String getEstado() {
        return estado;
    }

    /** Establece el estado operacional de la aerolinea */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Obtiene el número de teléfono de la aerolinea */
    public String getTelefono() {
        return telefono;
    }

    /** Establece el número de teléfono de la aerolinea */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** Obtiene el correo electrónico de la aerolinea */
    public String getEmail() {
        return email;
    }

    /** Establece el correo electrónico de la aerolinea */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Retorna la representación en string de la aerolinea con todos sus atributos */
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
