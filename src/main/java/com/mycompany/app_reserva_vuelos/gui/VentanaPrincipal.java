/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

// Imports para conectar con la capa de servicio
import com.mycompany.app_reserva_vuelos.service.VueloService;
import com.mycompany.app_reserva_vuelos.service.VueloServiceImpl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 *
 * @author Bogard Axel
 */
public class VentanaPrincipal extends javax.swing.JFrame {
private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    // Instancia del servicio de vuelos (capa de negocio)
    private final VueloService vueloService;

    /**
    * Creates new form VentanaPrincipal
    */
    public VentanaPrincipal() {
        // Inicializar el servicio
        this.vueloService = new VueloServiceImpl();
        
        initComponents();
        setLocationRelativeTo(null);
        
        // Inicializar fecha de salida con la fecha actual
        datesalida.setDate(new Date());
        
        // Configurar estado inicial de fecha de regreso
        toggleFechaRegreso();
    }

    
    private void toggleFechaRegreso() {
        String tipoViaje = (String) cmbtipo.getSelectedItem();
        if ("Ida y vuelta".equals(tipoViaje)) {
            dateregreso.setEnabled(true);
            lblRegreso.setForeground(new Color(100, 100, 100));
        } else {
            dateregreso.setEnabled(false);
            dateregreso.setDate(null);
            lblRegreso.setForeground(new Color(200, 200, 200));
        }
    }
        
       
private void buscarVuelos() {
        String origenNombre = (String) cmbOrigen.getSelectedItem();
        String destinoNombre = (String) cmbDestino.getSelectedItem();
        Date fechaSalidaDate = datesalida.getDate();

        // Validaciones básicas
        if (origenNombre == null || destinoNombre == null || fechaSalidaDate == null) {
            JOptionPane.showMessageDialog(this,
                    "Debes seleccionar origen, destino y fecha de salida.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (origenNombre.equals(destinoNombre)) {
            JOptionPane.showMessageDialog(this,
                    "El origen y destino no pueden ser iguales",
                    "Error de validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir java.util.Date -> java.time.LocalDate
        LocalDate fechaSalida = fechaSalidaDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Mapear el texto del combo al ID real de la tabla aeropuertos
        int idAeropuertoOrigen = mapearAeropuertoId(origenNombre);
        int idAeropuertoDestino = mapearAeropuertoId(destinoNombre);

        if (idAeropuertoOrigen == -1 || idAeropuertoDestino == -1) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró el ID de alguno de los aeropuertos seleccionados.\n" +
                    "Revisa el método mapearAeropuertoId().",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<VueloServiceImpl.VueloInfoUI> vuelos =
                    vueloService.buscarVuelosConInfoAeropuertos(
                            idAeropuertoOrigen,
                            idAeropuertoDestino,
                            fechaSalida
                    );

            if (vuelos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron vuelos para los criterios seleccionados.",
                        "Sin resultados",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Por ahora, mostrar resultados en un JOptionPane
            StringBuilder sb = new StringBuilder();
            sb.append("Vuelos encontrados:\n\n");
            for (VueloServiceImpl.VueloInfoUI v : vuelos) {
                sb.append("Vuelo: ").append(v.getVuelo().getNumeroVuelo()).append("\n");
                sb.append("Origen: ")
                  .append(v.getCodigoIataOrigen()).append(" - ").append(v.getNombreAeropuertoOrigen()).append("\n");
                sb.append("Destino: ")
                  .append(v.getCodigoIataDestino()).append(" - ").append(v.getNombreAeropuertoDestino()).append("\n");
                sb.append("Salida: ").append(v.getVuelo().getFechaHoraSalida()).append("\n");
                sb.append("Llegada: ").append(v.getVuelo().getFechaHoraLlegada()).append("\n");
                sb.append("----------------------------------------\n");
            }

            JOptionPane.showMessageDialog(this,
                    sb.toString(),
                    "Resultados de búsqueda",
                    JOptionPane.INFORMATION_MESSAGE);

            // Más adelante: aquí podrías abrir una VentanaResultados con JTable

        } catch (RuntimeException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error al buscar vuelos", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al buscar vuelos:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
        
/**
     * Mapea el texto mostrado en el combo al ID real del aeropuerto en la BD.
     * IMPORTANTE: Ajusta los IDs para que coincidan con tu tabla AEROPUERTO.
     */
private int mapearAeropuertoId(String nombreCombo) {
        switch (nombreCombo) {
            case "CUN - Aeropuerto Internacional de Cancún":
                return 201;
            case "YYZ - Aeropuerto Internacional Toronto Pearson":
                return 202;
            case "GIG - Aeropuerto Internacional de Galeão":
                return 203;
            case "SCL - Aeropuerto Internacional Arturo Merino Benítez":
                return 204;
            case "MAD - Aeropuerto Adolfo Suárez Madrid-Barajas":
                return 205;
            case "CDG - Aeropuerto de París-Charles de Gaulle":
                return 206;
            case "BER - Aeropuerto de Berlín Brandeburgo":
                return 207;
            case "FCO - Aeropuerto de Roma-Fiumicino":
                return 208;
            case "NRT - Aeropuerto Internacional de Narita":
                return 209;
            case "PEK - Aeropuerto Internacional de Pekín Capital":
                return 210;
            case "SYD - Aeropuerto de Sídney":
                return 211;
            case "CPT - Aeropuerto Internacional de Ciudad del Cabo":
                return 212;
            case "DEL - Aeropuerto Internacional Indira Gandhi":
                return 213;
            case "CAI - Aeropuerto Internacional de El Cairo":
                return 214;
            case "DXB - Aeropuerto Internacional de Dubái":
                return 215;
            default:
                return -1;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panprincipal = new javax.swing.JPanel();
        panbusqueda = new javax.swing.JPanel();
        cmbtipo = new javax.swing.JComboBox<>();
        cmbpasajeros = new javax.swing.JComboBox<>();
        datesalida = new com.toedter.calendar.JDateChooser();
        dateregreso = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        lblOrigen = new javax.swing.JLabel();
        lblDestino = new javax.swing.JLabel();
        lblSalida = new javax.swing.JLabel();
        lblRegreso = new javax.swing.JLabel();
        lblOrigen1 = new javax.swing.JLabel();
        lblOrigen2 = new javax.swing.JLabel();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbDestino = new javax.swing.JComboBox<>();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panbusqueda.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        cmbtipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ida y Vuelta", "Viaje Sencillo" }));
        cmbtipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtipoActionPerformed(evt);
            }
        });

        cmbpasajeros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }));

        btnBuscar.setText("Buscar Vuelos");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        lblOrigen.setText("Origen");

        lblDestino.setText("Destino");

        lblSalida.setText("Salida");

        lblRegreso.setText("Regreso");

        lblOrigen1.setText("Tipo de Viaje");

        lblOrigen2.setText("Pasajeros");

        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Aeropuerto Internacional de Cancún", "YYZ - Aeropuerto Internacional Toronto Pearson", "GIG - Aeropuerto Internacional de Galeão", "SCL - Aeropuerto Internacional Arturo Merino Benítez", "MAD - Aeropuerto Adolfo Suárez Madrid-Barajas", "CDG - Aeropuerto de París-Charles de Gaulle", "BER - Aeropuerto de Berlín Brandeburgo", "FCO - Aeropuerto de Roma-Fiumicino", "NRT - Aeropuerto Internacional de Narita", "PEK - Aeropuerto Internacional de Pekín Capital", "SYD - Aeropuerto de Sídney", "CPT - Aeropuerto Internacional de Ciudad del Cabo", "DEL - Aeropuerto Internacional Indira Gandhi", "CAI - Aeropuerto Internacional de El Cairo", "DXB - Aeropuerto Internacional de Dubái" }));
        cmbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigenActionPerformed(evt);
            }
        });

        cmbDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Aeropuerto Internacional de Cancún", "YYZ - Aeropuerto Internacional Toronto Pearson", "GIG - Aeropuerto Internacional de Galeão", "SCL - Aeropuerto Internacional Arturo Merino Benítez", "MAD - Aeropuerto Adolfo Suárez Madrid-Barajas", "CDG - Aeropuerto de París-Charles de Gaulle", "BER - Aeropuerto de Berlín Brandeburgo", "FCO - Aeropuerto de Roma-Fiumicino", "NRT - Aeropuerto Internacional de Narita", "PEK - Aeropuerto Internacional de Pekín Capital", "SYD - Aeropuerto de Sídney", "CPT - Aeropuerto Internacional de Ciudad del Cabo", "DEL - Aeropuerto Internacional Indira Gandhi", "CAI - Aeropuerto Internacional de El Cairo", "DXB - Aeropuerto Internacional de Dubái" }));

        javax.swing.GroupLayout panbusquedaLayout = new javax.swing.GroupLayout(panbusqueda);
        panbusqueda.setLayout(panbusquedaLayout);
        panbusquedaLayout.setHorizontalGroup(
            panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panbusquedaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBuscar)
                .addGap(32, 32, 32))
            .addGroup(panbusquedaLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbtipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrigen1)
                    .addComponent(cmbOrigen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbpasajeros, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDestino)
                    .addComponent(lblOrigen2)
                    .addComponent(cmbDestino, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegreso)
                    .addComponent(datesalida, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(dateregreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        panbusquedaLayout.setVerticalGroup(
            panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panbusquedaLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalida)
                    .addComponent(lblOrigen1)
                    .addComponent(lblOrigen2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datesalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbtipo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbpasajeros, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panbusquedaLayout.createSequentialGroup()
                        .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOrigen)
                            .addComponent(lblDestino))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panbusquedaLayout.createSequentialGroup()
                        .addComponent(lblRegreso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panbusquedaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbDestino, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
                    .addComponent(dateregreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnBuscar)
                .addGap(16, 16, 16))
        );

        lblTitulo.setText("TUP Vuelos");

        lblSubtitulo.setText("Hola");

        javax.swing.GroupLayout panprincipalLayout = new javax.swing.GroupLayout(panprincipal);
        panprincipal.setLayout(panprincipalLayout);
        panprincipalLayout.setHorizontalGroup(
            panprincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panprincipalLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(panprincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitulo)
                    .addComponent(panbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panprincipalLayout.setVerticalGroup(
            panprincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panprincipalLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSubtitulo)
                .addGap(18, 18, 18)
                .addComponent(panbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panprincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbtipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtipoActionPerformed
        // TODO add your handling code here:
        toggleFechaRegreso();
    }//GEN-LAST:event_cmbtipoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        buscarVuelos();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void cmbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbOrigenActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JComboBox<String> cmbDestino;
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JComboBox<String> cmbpasajeros;
    private javax.swing.JComboBox<String> cmbtipo;
    private com.toedter.calendar.JDateChooser dateregreso;
    private com.toedter.calendar.JDateChooser datesalida;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JLabel lblOrigen;
    private javax.swing.JLabel lblOrigen1;
    private javax.swing.JLabel lblOrigen2;
    private javax.swing.JLabel lblRegreso;
    private javax.swing.JLabel lblSalida;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panbusqueda;
    private javax.swing.JPanel panprincipal;
    // End of variables declaration//GEN-END:variables
}
