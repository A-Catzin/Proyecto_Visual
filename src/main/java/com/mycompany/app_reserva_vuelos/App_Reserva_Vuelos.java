/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.app_reserva_vuelos;
import com.mycompany.app_reserva_vuelos.gui.VentanaPrincipal;

/**
 *
 * @author tehca
 */
public class App_Reserva_Vuelos {

    public static void main(String[] args) {
        // Iniciar la interfaz gráfica
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}