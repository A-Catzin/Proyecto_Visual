/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui;

import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import com.mycompany.app_reserva_vuelos.model.DetalleReserva;
import com.mycompany.app_reserva_vuelos.model.Pasajero;
import com.mycompany.app_reserva_vuelos.model.Reserva;
import com.mycompany.app_reserva_vuelos.model.Tarifa;
import com.mycompany.app_reserva_vuelos.model.Usuario;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import com.mycompany.app_reserva_vuelos.service.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Bogard Axel
 */
public class PanelBuscar extends javax.swing.JPanel {

    private VueloService vueloService;
    private AeropuertoService aeropuertoService;
    private ReservaService reservaService;
    private PasajeroService pasajeroService;
    private UsuarioService usuarioService;

    /**
     * Creates new form PanelReservas
     */
    public PanelBuscar() {
        initComponents();
        this.vueloService = new VueloServiceImpl();
        this.aeropuertoService = new AeropuertoServiceImpl();
        this.reservaService = new ReservaServiceImpl();
        this.pasajeroService = new PasajeroServiceImpl();
        this.usuarioService = UsuarioServiceImpl.getInstance();
        cargarAeropuertos();
        configurarEventos();
    }

    private void cargarAeropuertos() {
        cmbOrigen1.removeAllItems();
        cmbOrigen2.removeAllItems();

        List<Aeropuerto> aeropuertos = aeropuertoService.listarTodos();
        for (Aeropuerto aeropuerto : aeropuertos) {
            String item = aeropuerto.getCodigoIATA() + " - " + aeropuerto.getNombreAeropuerto();
            cmbOrigen1.addItem(item);
            cmbOrigen2.addItem(item);
        }
    }

    private void configurarEventos() {
        // Botón Realizar Reserva
        btnrealizar.addActionListener(evt -> realizarReserva());

        // Botón Cancelar
        btncancelar.addActionListener(evt -> limpiarCampos());

        // Botón Limpiar
        btnlmpiar.addActionListener(evt -> limpiarCampos());

        // Evento para tipo de viaje
        cmbOrigen.addActionListener(evt -> {
            String tipoViaje = (String) cmbOrigen.getSelectedItem();
            // Habilitar/deshabilitar fecha de regreso según tipo de viaje
            jDateChooser1.setEnabled("Ida y Vuelta".equals(tipoViaje));
        });
    }

    private void realizarReserva() {
        // Validar campos
        if (cmbOrigen1.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de origen.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cmbOrigen2.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de destino.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jDateChooser2.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de salida.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoViaje = (String) cmbOrigen.getSelectedItem();
        if ("Ida y Vuelta".equals(tipoViaje) && jDateChooser1.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de regreso para viajes de ida y vuelta.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer numPasajeros = (Integer) spinpasajeros.getValue();
        if (numPasajeros == null || numPasajeros <= 0) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un número válido de pasajeros.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Verificar que el usuario esté autenticado
            Usuario usuarioActual = usuarioService.getUsuarioAutenticado();
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar una reserva.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar o crear pasajero
            Pasajero pasajero = obtenerOCrearPasajero(usuarioActual);
            if (pasajero == null) {
                JOptionPane.showMessageDialog(this, "Error al obtener información del pasajero.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener IDs de aeropuertos
            String origenStr = (String) cmbOrigen1.getSelectedItem();
            String destinoStr = (String) cmbOrigen2.getSelectedItem();

            String codigoOrigen = origenStr.split(" - ")[0];
            String codigoDestino = destinoStr.split(" - ")[0];

            // Buscar aeropuertos por código IATA
            List<Aeropuerto> aeropuertos = aeropuertoService.listarTodos();
            int idOrigen = -1, idDestino = -1;

            for (Aeropuerto ap : aeropuertos) {
                if (ap.getCodigoIATA().equals(codigoOrigen)) {
                    idOrigen = ap.getIdAeropuerto();
                }
                if (ap.getCodigoIATA().equals(codigoDestino)) {
                    idDestino = ap.getIdAeropuerto();
                }
            }

            if (idOrigen == -1 || idDestino == -1) {
                JOptionPane.showMessageDialog(this, "Error al obtener los aeropuertos seleccionados.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Convertir fechas
            Date fechaSalida = jDateChooser2.getDate();
            LocalDate localDateSalida = fechaSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Buscar vuelos de ida
            List<Vuelo> vuelosIda = vueloService.buscarVuelos(idOrigen, idDestino, localDateSalida);

            if (vuelosIda.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron vuelos disponibles para la ruta y fecha seleccionadas.",
                        "Sin Vuelos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tomar el primer vuelo disponible de ida
            Vuelo vueloIda = vuelosIda.get(0);

            // Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setIdPasajero(pasajero.getIdPasajero());
            reserva.setFechaReserva(LocalDateTime.now());
            reserva.setEstadoReserva("Confirmada");
            reserva.setCodigoReserva("RES-" + System.currentTimeMillis());

            // Crear detalles de reserva
            List<DetalleReserva> detalles = new ArrayList<>();

            // Detalle para vuelo de ida
            // Obtener la primera tarifa disponible para el vuelo
            TarifaService tarifaService = new TarifaServiceImpl();
            List<Tarifa> tarifasIda = tarifaService.listarTarifasPorVuelo(vueloIda.getIdVuelo());

            if (tarifasIda.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay tarifas disponibles para el vuelo seleccionado.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DetalleReserva detalleIda = new DetalleReserva();
            detalleIda.setIdVuelo(vueloIda.getIdVuelo());
            detalleIda.setNumeroAsiento("A" + (int) (Math.random() * 30 + 1)); // Asiento aleatorio
            detalleIda.setIdTarifa(tarifasIda.get(0).getIdTarifa()); // Primera tarifa disponible
            detalles.add(detalleIda);

            // Si es ida y vuelta, buscar vuelo de regreso
            if ("Ida y Vuelta".equals(tipoViaje)) {
                Date fechaRegreso = jDateChooser1.getDate();
                LocalDate localDateRegreso = fechaRegreso.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                List<Vuelo> vuelosRegreso = vueloService.buscarVuelos(idDestino, idOrigen, localDateRegreso);

                if (!vuelosRegreso.isEmpty()) {
                    Vuelo vueloRegreso = vuelosRegreso.get(0);

                    List<Tarifa> tarifasRegreso = tarifaService.listarTarifasPorVuelo(vueloRegreso.getIdVuelo());

                    if (!tarifasRegreso.isEmpty()) {
                        DetalleReserva detalleRegreso = new DetalleReserva();
                        detalleRegreso.setIdVuelo(vueloRegreso.getIdVuelo());
                        detalleRegreso.setNumeroAsiento("A" + (int) (Math.random() * 30 + 1));
                        detalleRegreso.setIdTarifa(tarifasRegreso.get(0).getIdTarifa());
                        detalles.add(detalleRegreso);
                    }
                }
            }

            // Guardar la reserva
            int idReserva = reservaService.crearReserva(reserva, detalles);

            if (idReserva > 0) {
                String mensaje = "¡Reserva creada exitosamente!\n\n" +
                        "Código de reserva: " + reserva.getCodigoReserva() + "\n" +
                        "Vuelo de ida: " + vueloIda.getNumeroVuelo() + "\n" +
                        "Origen: " + origenStr + "\n" +
                        "Destino: " + destinoStr + "\n" +
                        "Fecha: " + fechaSalida + "\n" +
                        "Pasajeros: " + numPasajeros;

                JOptionPane.showMessageDialog(this, mensaje, "Reserva Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reserva. Intente nuevamente.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al realizar la reserva: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Pasajero obtenerOCrearPasajero(Usuario usuario) {
        try {
            // Buscar pasajero por email
            Pasajero pasajero = pasajeroService.obtenerPasajeroPorEmail(usuario.getEmail());

            if (pasajero == null) {
                // Crear nuevo pasajero
                pasajero = new Pasajero();
                pasajero.setNombre(usuario.getNombre());
                pasajero.setApellido(""); // Podría extraerse del nombre completo
                pasajero.setEmail(usuario.getEmail());
                pasajero.setFechaNacimiento(null); // Opcional

                int idPasajero = pasajeroService.registrarPasajero(pasajero);
                if (idPasajero > 0) {
                    pasajero.setIdPasajero(idPasajero);
                } else {
                    return null;
                }
            }

            return pasajero;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void limpiarCampos() {
        cmbOrigen.setSelectedIndex(0);
        cmbOrigen1.setSelectedIndex(-1);
        cmbOrigen2.setSelectedIndex(-1);
        jDateChooser2.setDate(null);
        jDateChooser1.setDate(null);
        spinpasajeros.setValue(1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel4 = new javax.swing.JPanel();
        btnrealizar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btnlmpiar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblorigen = new javax.swing.JLabel();
        lbldestino = new javax.swing.JLabel();
        lblnombre = new javax.swing.JLabel();
        lblsalida = new javax.swing.JLabel();
        lblregreso = new javax.swing.JLabel();
        lblpasajeros = new javax.swing.JLabel();
        cmbOrigen = new javax.swing.JComboBox<>();
        spinpasajeros = new javax.swing.JSpinner();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        cmbOrigen1 = new javax.swing.JComboBox<>();
        cmbOrigen2 = new javax.swing.JComboBox<>();

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        btnrealizar.setText("Realizar reserva");
        jPanel4.add(btnrealizar);

        btncancelar.setText("Cancelar reserva");
        jPanel4.add(btncancelar);

        btnlmpiar.setText("Limpiar");
        jPanel4.add(btnlmpiar);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la reserva."));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblorigen.setText("Tipo de Viaje:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblorigen, gridBagConstraints);

        lbldestino.setText("Salida:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lbldestino, gridBagConstraints);

        lblnombre.setText("Regreso:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblnombre, gridBagConstraints);

        lblsalida.setText("Origen:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblsalida, gridBagConstraints);

        lblregreso.setText("Destino:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblregreso, gridBagConstraints);

        lblpasajeros.setText("Pasajeros:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblpasajeros, gridBagConstraints);

        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ida y Vuelta", "Sencillo" }));
        cmbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbOrigen, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(spinpasajeros, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jDateChooser1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jDateChooser2, gridBagConstraints);

        cmbOrigen1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Aeropuerto de Cancún",
                "YYZ - Aeropuerto Toronto Pearson", "GIG - Aeropuerto de Galeão",
                "SCL - Aeropuerto Arturo Merino Benítez", "MAD - Aeropuerto Adolfo Suárez Madrid-Barajas",
                "CDG - Aeropuerto de París-Charles de Gaulle", "BER - Aeropuerto de Berlín Brandeburgo",
                "FCO - Aeropuerto de Roma-Fiumicino", "NRT - Aeropuerto de Narita", "PEK - Aeropuerto de Pekín Capital",
                "SYD - Aeropuerto de Sídney", "CPT - Aeropuerto de Ciudad del Cabo", "DEL - Aeropuerto Indira Gandhi",
                "CAI - Aeropuerto de El Cairo", "DXB - Aeropuerto de Dubái" }));
        cmbOrigen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigen1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbOrigen1, gridBagConstraints);

        cmbOrigen2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Aeropuerto de Cancún",
                "YYZ - Aeropuerto Toronto Pearson", "GIG - Aeropuerto de Galeão",
                "SCL - Aeropuerto Arturo Merino Benítez", "MAD - Aeropuerto Adolfo Suárez Madrid-Barajas",
                "CDG - Aeropuerto de París-Charles de Gaulle", "BER - Aeropuerto de Berlín Brandeburgo",
                "FCO - Aeropuerto de Roma-Fiumicino", "NRT - Aeropuerto de Narita", "PEK - Aeropuerto de Pekín Capital",
                "SYD - Aeropuerto de Sídney", "CPT - Aeropuerto de Ciudad del Cabo", "DEL - Aeropuerto Indira Gandhi",
                "CAI - Aeropuerto de El Cairo", "DXB - Aeropuerto de Dubái", " " }));
        cmbOrigen2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigen2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbOrigen2, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 397,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }// </editor-fold>//GEN-END:initComponents

    private void cmbOrigenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigenActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigenActionPerformed

    private void cmbOrigen1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigen1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigen1ActionPerformed

    private void cmbOrigen2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigen2ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigen2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnlmpiar;
    private javax.swing.JButton btnrealizar;
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JComboBox<String> cmbOrigen1;
    private javax.swing.JComboBox<String> cmbOrigen2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbldestino;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lblorigen;
    private javax.swing.JLabel lblpasajeros;
    private javax.swing.JLabel lblregreso;
    private javax.swing.JLabel lblsalida;
    private javax.swing.JSpinner spinpasajeros;
    // End of variables declaration//GEN-END:variables
}
