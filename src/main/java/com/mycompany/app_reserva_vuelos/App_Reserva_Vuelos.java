/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.app_reserva_vuelos;
import com.mycompany.app_reserva_vuelos.gui.VentanaLogin;
import com.mycompany.app_reserva_vuelos.db.ConexionBD;
import java.sql.Connection;

/**
 *
 * @author tehca
 */
public class App_Reserva_Vuelos {
    /** Punto de entrada de la aplicación - inicializa la BD y abre la ventana de login */
    public static void main(String[] args) {
        // 1. Inicializar la base de datos
        ConexionBD.inicializarSiNoExiste(); // <-- CORREGIDO: 'i' minúscula

        // 2. Probar la conexión
        Connection conn = ConexionBD.getConnection(); // <-- CORREGIDO: 'g' minúscula
        if (conn != null) {
            System.out.println("✅ Conexión exitosa a la base de datos!");
            ConexionBD.cerrarConexion(conn); // <-- CORREGIDO: 'c' minúscula
        } else {
            System.out.println("❌ Error al conectar");
        }

        // 3. Aquí va el resto de tu aplicación
        new VentanaLogin().setVisible(true);
    }
}