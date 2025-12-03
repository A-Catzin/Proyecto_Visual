/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui.Admin;

import com.mycompany.app_reserva_vuelos.model.DetalleReserva;
import com.mycompany.app_reserva_vuelos.model.Pasajero;
import com.mycompany.app_reserva_vuelos.model.Reserva;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import com.mycompany.app_reserva_vuelos.service.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelGestionReservas extends javax.swing.JPanel {

    private ReservaService reservaService;
    private VueloService vueloService;
    private PasajeroService pasajeroService;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PanelGestionReservas
     */
    public PanelGestionReservas() {
        initComponents();
        reservaService = new ReservaServiceImpl();
        vueloService = new VueloServiceImpl();
        pasajeroService = new PasajeroServiceImpl();
        inicializarTabla();
        cargarTodasLasReservas();
    }

    private void inicializarTabla() {
        String[] columnNames = { "ID", "Código", "Pasajero", "Email", "Vuelo(s)", "Estado", "Fecha Reserva" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
        // Ocultar la columna ID pero mantenerla para referencia
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(0).setWidth(0);
    }

    private void cargarTodasLasReservas() {
        try {
            tableModel.setRowCount(0);
            List<Reserva> reservas = reservaService.listarTodasReservas();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (Reserva reserva : reservas) {
                Pasajero pasajero = pasajeroService.obtenerPasajeroPorId(reserva.getIdPasajero());
                String nombrePasajero = pasajero != null ? pasajero.getNombre() + " " + pasajero.getApellido() : "N/A";
                String emailPasajero = pasajero != null ? pasajero.getEmail() : "N/A";

                // Obtener detalles de la reserva
                List<DetalleReserva> detalles = reservaService.obtenerDetallesPorReserva(reserva.getIdReserva());
                String vuelosInfo = detalles.size() + " vuelo(s)";

                String fechaReserva = reserva.getFechaReserva() != null
                        ? reserva.getFechaReserva().format(dateFormatter)
                        : "";

                Object[] row = {
                        reserva.getIdReserva(), // ID oculto
                        reserva.getCodigoReserva(),
                        nombrePasajero,
                        emailPasajero,
                        vuelosInfo,
                        reserva.getEstadoReserva(),
                        fechaReserva
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas: " + e.getMessage());
        }
    }

    private void verDetallesReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para ver los detalles.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
            Reserva reserva = reservaService.obtenerReservaPorId(idReserva);
            List<DetalleReserva> detalles = reservaService.obtenerDetallesPorReserva(idReserva);
            Pasajero pasajero = pasajeroService.obtenerPasajeroPorId(reserva.getIdPasajero());

            StringBuilder mensaje = new StringBuilder();
            mensaje.append("=== DETALLES DE LA RESERVA ===\n\n");
            mensaje.append("Código: ").append(reserva.getCodigoReserva()).append("\n");
            mensaje.append("Estado: ").append(reserva.getEstadoReserva()).append("\n");
            mensaje.append("Fecha: ")
                    .append(reserva.getFechaReserva().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                    .append("\n\n");

            mensaje.append("=== PASAJERO ===\n");
            if (pasajero != null) {
                mensaje.append("Nombre: ").append(pasajero.getNombre()).append(" ").append(pasajero.getApellido())
                        .append("\n");
                mensaje.append("Email: ").append(pasajero.getEmail()).append("\n");
                // Teléfono no disponible en modelo Pasajero
            }

            mensaje.append("=== VUELOS ===\n");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (DetalleReserva detalle : detalles) {
                Vuelo vuelo = vueloService.obtenerVueloPorId(detalle.getIdVuelo());
                if (vuelo != null) {
                    mensaje.append("• Vuelo: ").append(vuelo.getNumeroVuelo()).append("\n");
                    mensaje.append("  Salida: ").append(vuelo.getFechaHoraSalida().format(timeFormatter)).append("\n");
                    mensaje.append("  Llegada: ").append(vuelo.getFechaHoraLlegada().format(timeFormatter))
                            .append("\n");
                    mensaje.append("  Asiento: ").append(detalle.getNumeroAsiento()).append("\n\n");
                }
            }

            JOptionPane.showMessageDialog(this, mensaje.toString(), "Detalles de Reserva",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al obtener detalles: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para cancelar.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String estadoActual = (String) tableModel.getValueAt(selectedRow, 5);
        if ("Cancelada".equals(estadoActual)) {
            JOptionPane.showMessageDialog(this, "Esta reserva ya está cancelada.", "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cancelar esta reserva?",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
                boolean resultado = reservaService.cancelarReserva(idReserva, "Cancelada");

                if (resultado) {
                    JOptionPane.showMessageDialog(this, "Reserva cancelada exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarTodasLasReservas(); // Recargar tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cancelar reserva: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cambiarEstadoReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para cambiar su estado.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] estados = { "Confirmada", "Pendiente", "Cancelada" };
        String estadoActual = (String) tableModel.getValueAt(selectedRow, 5);

        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
                "Estado actual: " + estadoActual + "\nSeleccione el nuevo estado:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                estados,
                estadoActual);

        if (nuevoEstado != null && !nuevoEstado.equals(estadoActual)) {
            try {
                int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
                boolean resultado = reservaService.cancelarReserva(idReserva, nuevoEstado); // Usamos cancelarReserva
                                                                                            // que actualiza estado

                if (resultado) {
                    JOptionPane.showMessageDialog(this, "Estado actualizado exitosamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarTodasLasReservas(); // Recargar tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnVerDetalles = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCambiarEstado = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Gestiona las reservas disponibles");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null }
                },
                new String[] {
                        "ID", "Código", "Pasajero", "Email", "Vuelo(s)", "Estado", "Fecha Reserva"
                }) {
            Class[] types = new Class[] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBackground(new java.awt.Color(240, 240, 240));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones"));

        btnVerDetalles.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnVerDetalles.setText("Ver Detalles");
        btnVerDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerDetallesActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar Reserva");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnCambiarEstado.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCambiarEstado.setText("Cambiar Estado");
        btnCambiarEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarEstadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(btnVerDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCambiarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 160,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnVerDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCambiarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 760,
                                                Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(20, 20, 20)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }// </editor-fold>//GEN-END:initComponents

    private void btnVerDetallesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnVerDetallesActionPerformed
        verDetallesReserva();
    }// GEN-LAST:event_btnVerDetallesActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCancelarActionPerformed
        cancelarReserva();
    }// GEN-LAST:event_btnCancelarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnActualizarActionPerformed
        cargarTodasLasReservas();
        JOptionPane.showMessageDialog(this, "Tabla actualizada.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }// GEN-LAST:event_btnActualizarActionPerformed

    private void btnCambiarEstadoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCambiarEstadoActionPerformed
        cambiarEstadoReserva();
    }// GEN-LAST:event_btnCambiarEstadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCambiarEstado;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnVerDetalles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
