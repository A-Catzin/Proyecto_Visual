/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui.Admin;

import com.mycompany.app_reserva_vuelos.model.Tarifa;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import com.mycompany.app_reserva_vuelos.service.TarifaService;
import com.mycompany.app_reserva_vuelos.service.TarifaServiceImpl;
import com.mycompany.app_reserva_vuelos.service.VueloService;
import com.mycompany.app_reserva_vuelos.service.VueloServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelPreciosTarifas extends javax.swing.JPanel {

    private TarifaService tarifaService;
    private VueloService vueloService;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PanelPreciosTarifas
     */
    public PanelPreciosTarifas() {
        initComponents();
        tarifaService = new TarifaServiceImpl();
        vueloService = new VueloServiceImpl();
        inicializarTabla();
        cargarVuelos();
        cargarTabla();
        configurarEventos();
    }

    private void inicializarTabla() {
        String[] columnNames = { "ID", "Vuelo", "Clase", "Precio", "Temporada", "Descuento", "Recargo" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void cargarVuelos() {
        jComboBox2.removeAllItems();
        List<Vuelo> vuelos = vueloService.listarVuelos();
        for (Vuelo vuelo : vuelos) {
            jComboBox2.addItem(vuelo.getIdVuelo() + " - " + vuelo.getNumeroVuelo());
        }
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Tarifa> tarifas = tarifaService.listarTarifas();
        for (Tarifa t : tarifas) {
            String clase = obtenerNombreClase(t.getIdClase());
            Object[] row = {
                    t.getIdTarifa(),
                    t.getIdVuelo(),
                    clase,
                    t.getPrecio(),
                    t.getTemporada(),
                    t.getDescuento(),
                    t.getRecargo()
            };
            tableModel.addRow(row);
        }
    }

    private String obtenerNombreClase(int idClase) {
        switch (idClase) {
            case 1:
                return "Económica";
            case 2:
                return "Ejecutiva";
            case 3:
                return "Primera Clase";
            default:
                return "Desconocida";
        }
    }

    private int obtenerIdClase(String nombreClase) {
        if (nombreClase.contains("Económica"))
            return 1;
        if (nombreClase.contains("Ejecutiva"))
            return 2;
        if (nombreClase.contains("Primera Clase"))
            return 3;
        return 1; // Default
    }

    private void configurarEventos() {
        jButton1.addActionListener(evt -> agregarTarifa());
        jButton2.addActionListener(evt -> modificarTarifa());
        jButton3.addActionListener(evt -> eliminarTarifa());
        jButton4.addActionListener(evt -> limpiarCampos());

        jTable1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void agregarTarifa() {
        try {
            Tarifa t = new Tarifa();

            String vueloStr = (String) jComboBox2.getSelectedItem();
            if (vueloStr == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un vuelo.");
                return;
            }
            int idVuelo = Integer.parseInt(vueloStr.split(" - ")[0]);
            t.setIdVuelo(idVuelo);

            String claseStr = (String) jComboBox1.getSelectedItem();
            t.setIdClase(obtenerIdClase(claseStr));

            t.setPrecio(new BigDecimal(jTextField2.getText()));
            t.setTemporada((String) cmbOrigen1.getSelectedItem());
            t.setDescuento(new BigDecimal(jTextField6.getText()));
            t.setRecargo(new BigDecimal(jTextField5.getText()));

            tarifaService.registrarTarifa(t);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Tarifa agregada exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en los campos numéricos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar tarifa: " + e.getMessage());
        }
    }

    private void modificarTarifa() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarifa para modificar.");
            return;
        }

        try {
            int idTarifa = (int) tableModel.getValueAt(selectedRow, 0);
            Tarifa t = tarifaService.obtenerTarifaPorId(idTarifa);

            String vueloStr = (String) jComboBox2.getSelectedItem();
            if (vueloStr != null) {
                int idVuelo = Integer.parseInt(vueloStr.split(" - ")[0]);
                t.setIdVuelo(idVuelo);
            }

            String claseStr = (String) jComboBox1.getSelectedItem();
            t.setIdClase(obtenerIdClase(claseStr));

            t.setPrecio(new BigDecimal(jTextField2.getText()));
            t.setTemporada((String) cmbOrigen1.getSelectedItem());
            t.setDescuento(new BigDecimal(jTextField6.getText()));
            t.setRecargo(new BigDecimal(jTextField5.getText()));

            tarifaService.modificarTarifa(t);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Tarifa modificada exitosamente.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en los campos numéricos: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar tarifa: " + e.getMessage());
        }
    }

    private void eliminarTarifa() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una tarifa para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta tarifa?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idTarifa = (int) tableModel.getValueAt(selectedRow, 0);
                tarifaService.eliminarTarifa(idTarifa);
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Tarifa eliminada exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar tarifa: " + e.getMessage());
            }
        }
    }

    private void cargarDatosSeleccionados() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int idTarifa = (int) tableModel.getValueAt(selectedRow, 0);
            Tarifa t = tarifaService.obtenerTarifaPorId(idTarifa);
            if (t != null) {
                // Seleccionar vuelo en combo
                for (int i = 0; i < jComboBox2.getItemCount(); i++) {
                    String item = jComboBox2.getItemAt(i);
                    if (item.startsWith(t.getIdVuelo() + " - ")) {
                        jComboBox2.setSelectedIndex(i);
                        break;
                    }
                }

                // Seleccionar clase
                String nombreClase = obtenerNombreClase(t.getIdClase());
                // Ajustar selección combo clase (Económica, Ejecutiva, Primera Clase.)
                // Note: The combo box items have specific strings, need to match them.
                for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                    if (jComboBox1.getItemAt(i).contains(nombreClase)) {
                        jComboBox1.setSelectedIndex(i);
                        break;
                    }
                }

                jTextField2.setText(t.getPrecio().toString());
                cmbOrigen1.setSelectedItem(t.getTemporada());
                jTextField6.setText(t.getDescuento().toString());
                jTextField5.setText(t.getRecargo().toString());
            }
        }
    }

    private void limpiarCampos() {
        jComboBox2.setSelectedIndex(-1);
        jComboBox1.setSelectedIndex(0);
        jTextField2.setText("");
        cmbOrigen1.setSelectedIndex(0);
        jTextField6.setText("");
        jTextField5.setText("");
        jTable1.clearSelection();
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
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        cmbOrigen1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Vuelo."));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Vuelo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Clase:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Precio base ($)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Temporada:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Descuento (en %):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Recargo (%):");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel6, gridBagConstraints);

        jComboBox1.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Económica", "Ejecutiva", "Primera Clase." }));
        jComboBox1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField6, gridBagConstraints);

        cmbOrigen1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Baja", "Media", "Alta" }));
        cmbOrigen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigen1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbOrigen1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jComboBox2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jTextField2, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Vuelos"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null },
                        { null, null, null, null, null, null, null, null, null }
                },
                new String[] {
                        "ID", "Número Vuelo", "Aerolínea", "Origen", "Destino", "Fecha", "Hora", "Capacidad",
                        "Disponibles"
                }) {
            Class[] types = new Class[] {
                    java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class,
                    java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class,
                    java.lang.Integer.class
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

        jButton1.setText("Agregar Vuelo");
        jPanel4.add(jButton1);

        jButton2.setText("Modificar Vuelo");
        jPanel4.add(jButton2);

        jButton3.setText("Eliminar Vuelo");
        jPanel4.add(jButton3);

        jButton4.setText("Limpiar Campos");
        jPanel4.add(jButton4);

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

    private void cmbOrigen1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigen1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigen1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbOrigen1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
