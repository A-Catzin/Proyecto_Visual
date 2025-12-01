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
     * Creates new form PanelGestiondeReservas
     */
    public PanelGestionReservas() {
        initComponents();
        reservaService = new ReservaServiceImpl();
        vueloService = new VueloServiceImpl();
        pasajeroService = new PasajeroServiceImpl();
        inicializarTabla();
        cargarEstados();
        configurarEventos();
    }

    private void inicializarTabla() {
        String[] columnNames = { "Código", "Pasajero", "Email", "Vuelo(s)", "Estado", "Fecha Reserva" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void cargarEstados() {
        jComboBox2.removeAllItems();
        jComboBox2.addItem("Confirmada");
        jComboBox2.addItem("Pendiente");
        jComboBox2.addItem("Cancelada");
    }

    private void configurarEventos() {
        jButton1.addActionListener(evt -> buscarReserva());
        jButton2.addActionListener(evt -> mostrarMensajeNuevaReserva());
        jButton3.addActionListener(evt -> modificarEstadoReserva());
        jButton4.addActionListener(evt -> cancelarReserva());
        jButton5.addActionListener(evt -> limpiarCampos());

        jTable1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void buscarReserva() {
        String codigoReserva = jTextField1.getText().trim();
        if (codigoReserva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código de reserva para buscar.");
            return;
        }

        try {
            int idReserva = Integer.parseInt(codigoReserva);
            Reserva reserva = reservaService.obtenerReservaPorId(idReserva);

            if (reserva == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la reserva con código: " + codigoReserva);
                return;
            }

            // Limpiar tabla y mostrar solo esta reserva
            tableModel.setRowCount(0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            Pasajero pasajero = pasajeroService.obtenerPasajeroPorId(reserva.getIdPasajero());
            String nombrePasajero = pasajero != null ? pasajero.getNombre() + " " + pasajero.getApellido() : "N/A";
            String emailPasajero = pasajero != null ? pasajero.getEmail() : "N/A";

            // Obtener detalles de la reserva
            List<DetalleReserva> detalles = reservaService.obtenerDetallesPorReserva(idReserva);
            String vuelosInfo = detalles.size() + " vuelo(s)";

            String fechaReserva = reserva.getFechaReserva() != null ? reserva.getFechaReserva().format(dateFormatter)
                    : "";

            Object[] row = {
                    reserva.getCodigoReserva(),
                    nombrePasajero,
                    emailPasajero,
                    vuelosInfo,
                    reserva.getEstadoReserva(),
                    fechaReserva
            };
            tableModel.addRow(row);

            // Mostrar detalles en los campos
            jTextField2.setText(nombrePasajero);
            jTextField3.setText(emailPasajero);
            jComboBox2.setSelectedItem(reserva.getEstadoReserva());

            // Mostrar información de vuelos en el combo
            jComboBox1.removeAllItems();
            for (DetalleReserva detalle : detalles) {
                Vuelo vuelo = vueloService.obtenerVueloPorId(detalle.getIdVuelo());
                if (vuelo != null) {
                    jComboBox1.addItem(vuelo.getNumeroVuelo() + " - Asiento: " + detalle.getNumeroAsiento());
                }
            }
            jTextField5.setText(String.valueOf(detalles.size()));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código de reserva debe ser un número.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensajeNuevaReserva() {
        JOptionPane.showMessageDialog(this,
                "Para crear una nueva reserva, use el panel de búsqueda de vuelos en la interfaz de cliente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void modificarEstadoReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para modificar su estado.");
            return;
        }

        try {
            String codigoReserva = jTextField1.getText().trim();
            if (codigoReserva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay reserva seleccionada.");
                return;
            }

            int idReserva = Integer.parseInt(codigoReserva);
            String nuevoEstado = (String) jComboBox2.getSelectedItem();

            if (nuevoEstado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un estado.");
                return;
            }

            boolean resultado = reservaService.cancelarReserva(idReserva, nuevoEstado);
            if (resultado) {
                buscarReserva(); // Recargar la reserva
                JOptionPane.showMessageDialog(this, "Estado de reserva actualizado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el estado de la reserva.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar estado: " + e.getMessage());
        }
    }

    private void cancelarReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para cancelar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar esta reserva?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String codigoReserva = jTextField1.getText().trim();
                if (!codigoReserva.isEmpty()) {
                    int idReserva = Integer.parseInt(codigoReserva);
                    boolean resultado = reservaService.cancelarReserva(idReserva, "Cancelada");
                    if (resultado) {
                        limpiarCampos();
                        JOptionPane.showMessageDialog(this, "Reserva cancelada exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar reserva: " + e.getMessage());
            }
        }
    }

    private void cargarDatosSeleccionados() {
        // Los datos ya se cargan en buscarReserva()
    }

    private void limpiarCampos() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        jComboBox1.removeAllItems();
        jComboBox2.setSelectedIndex(0);
        jTable1.clearSelection();
        tableModel.setRowCount(0);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la reserva."));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Código de Reserva:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Estado:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Nombre de Cliente:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Email:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Vuelo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Asientos:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel6, gridBagConstraints);

        jTextField1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField1, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aeroméxico", "Magnicharters",
                "Aeromar", "Interjet", "Volaris", "Viva Aerobus", "Wingo", "Spirit" }));
        jComboBox1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jComboBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField3, gridBagConstraints);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aeroméxico", "Magnicharters",
                "Aeromar", "Interjet", "Volaris", "Viva Aerobus", "Wingo", "Spirit" }));
        jComboBox2.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jComboBox2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Reservas"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "Codigo", "Cliente", "EMail", "Vuelo", "Asientos", "Estado", "Fecha Reserva", "Total"
                }) {
            Class[] types = new Class[] {
                    java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jTable1.setToolTipText("");
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap()));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                .addContainerGap()));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        jButton1.setText("Buscar reserva");
        jPanel4.add(jButton1);

        jButton2.setText("Nueva reserva");
        jPanel4.add(jButton2);

        jButton3.setText("Modificar");
        jPanel4.add(jButton3);

        jButton4.setText("Cancelar reserva");
        jPanel4.add(jButton4);

        jButton5.setText("Limpiar");
        jPanel4.add(jButton5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(34, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12,
                                        Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 848, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 481, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
