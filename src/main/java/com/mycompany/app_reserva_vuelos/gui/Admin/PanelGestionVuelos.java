/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui.Admin;

import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import com.mycompany.app_reserva_vuelos.model.Aeronave;
import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import com.mycompany.app_reserva_vuelos.service.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelGestionVuelos extends javax.swing.JPanel {

    private VueloService vueloService;
    private AerolineaService aerolineaService;
    private AeropuertoService aeropuertoService;
    private AeronaveService aeronaveService;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PanelGestionVuelos
     */
    public PanelGestionVuelos() {
        initComponents();
        vueloService = new VueloServiceImpl();
        aerolineaService = new AerolineaServiceImpl();
        aeropuertoService = new AeropuertoServiceImpl();
        aeronaveService = new AeronaveServiceImpl();
        inicializarTabla();
        cargarAerolineas();
        cargarAeropuertos();
        cargarTabla();
        configurarEventos();
    }

    private void inicializarTabla() {
        String[] columnNames = { "ID", "Número Vuelo", "Aerolínea", "Origen", "Destino", "Fecha", "Hora", "Aeronave" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void cargarAerolineas() {
        jComboBox1.removeAllItems();
        List<Aerolinea> aerolineas = aerolineaService.listarAerolineas();
        for (Aerolinea a : aerolineas) {
            jComboBox1.addItem(a.getIdAerolinea() + " - " + a.getNombreAerolinea());
        }
    }

    private void cargarAeropuertos() {
        cmbOrigen.removeAllItems();
        cmbOrigen1.removeAllItems();
        List<Aeropuerto> aeropuertos = aeropuertoService.listarTodos();
        for (Aeropuerto ap : aeropuertos) {
            String item = ap.getIdAeropuerto() + " - " + ap.getCodigoIATA() + " - " + ap.getNombreAeropuerto();
            cmbOrigen.addItem(item);
            cmbOrigen1.addItem(item);
        }
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        List<Vuelo> vuelos = vueloService.listarVuelos();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Vuelo v : vuelos) {
            // Obtener nombres en lugar de IDs
            Aerolinea aerolinea = obtenerAerolineaPorAeronave(v.getIdAeronave());
            String nombreAerolinea = aerolinea != null ? aerolinea.getNombreAerolinea() : "N/A";

            Aeropuerto origen = aeropuertoService.obtenerPorId(v.getIdAeropuertoOrigen());
            String nombreOrigen = origen != null ? origen.getCodigoIATA() : "N/A";

            Aeropuerto destino = aeropuertoService.obtenerPorId(v.getIdAeropuertoDestino());
            String nombreDestino = destino != null ? destino.getCodigoIATA() : "N/A";

            Aeronave aeronave = aeronaveService.obtenerPorId(v.getIdAeronave());
            String modeloAeronave = aeronave != null ? aeronave.getModelo() : "N/A";

            String fecha = v.getFechaHoraSalida() != null ? v.getFechaHoraSalida().format(dateFormatter) : "";
            String hora = v.getFechaHoraSalida() != null ? v.getFechaHoraSalida().format(timeFormatter) : "";

            Object[] row = {
                    v.getIdVuelo(),
                    v.getNumeroVuelo(),
                    nombreAerolinea,
                    nombreOrigen,
                    nombreDestino,
                    fecha,
                    hora,
                    modeloAeronave
            };
            tableModel.addRow(row);
        }
    }

    private Aerolinea obtenerAerolineaPorAeronave(int idAeronave) {
        Aeronave aeronave = aeronaveService.obtenerPorId(idAeronave);
        if (aeronave != null) {
            return aerolineaService.obtenerAerolineaPorId(aeronave.getIdAerolinea());
        }
        return null;
    }

    private void configurarEventos() {
        jButton1.addActionListener(evt -> agregarVuelo());
        jButton2.addActionListener(evt -> modificarVuelo());
        jButton3.addActionListener(evt -> eliminarVuelo());
        jButton4.addActionListener(evt -> limpiarCampos());

        jTable1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void agregarVuelo() {
        try {
            Vuelo v = new Vuelo();

            v.setNumeroVuelo(jTextField1.getText().trim());

            if (v.getNumeroVuelo().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número de vuelo.");
                return;
            }

            // Obtener aeronave de la primera aerolínea (simplificado)
            String aerolineaStr = (String) jComboBox1.getSelectedItem();
            if (aerolineaStr == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una aerolínea.");
                return;
            }
            int idAerolinea = Integer.parseInt(aerolineaStr.split(" - ")[0]);

            // Obtener primera aeronave de esa aerolínea
            List<Aeronave> aeronaves = aeronaveService.listar();
            Aeronave aeronaveSeleccionada = null;
            for (Aeronave a : aeronaves) {
                if (a.getIdAerolinea() == idAerolinea) {
                    aeronaveSeleccionada = a;
                    break;
                }
            }

            if (aeronaveSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "No hay aeronaves disponibles para esta aerolínea.");
                return;
            }

            v.setIdAeronave(aeronaveSeleccionada.getIdAeronave());

            // Origen
            String origenStr = (String) cmbOrigen.getSelectedItem();
            if (origenStr == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de origen.");
                return;
            }
            int idOrigen = Integer.parseInt(origenStr.split(" - ")[0]);
            v.setIdAeropuertoOrigen(idOrigen);

            // Destino
            String destinoStr = (String) cmbOrigen1.getSelectedItem();
            if (destinoStr == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de destino.");
                return;
            }
            int idDestino = Integer.parseInt(destinoStr.split(" - ")[0]);
            v.setIdAeropuertoDestino(idDestino);

            // Fecha y hora
            Date fecha = jDateChooser1.getDate();
            if (fecha == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha.");
                return;
            }

            String horaStr = jTextField5.getText().trim();
            if (horaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una hora (formato HH:mm).");
                return;
            }

            LocalDate localDate = new java.sql.Date(fecha.getTime()).toLocalDate();
            LocalTime localTime = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime fechaHoraSalida = LocalDateTime.of(localDate, localTime);
            v.setFechaHoraSalida(fechaHoraSalida);

            // Hora de llegada (2 horas después por defecto)
            v.setFechaHoraLlegada(fechaHoraSalida.plusHours(2));

            vueloService.registrarVuelo(v);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Vuelo agregado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void modificarVuelo() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vuelo para modificar.");
            return;
        }

        try {
            int idVuelo = (int) tableModel.getValueAt(selectedRow, 0);
            Vuelo v = vueloService.obtenerVueloPorId(idVuelo);

            if (v == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el vuelo.");
                return;
            }

            v.setNumeroVuelo(jTextField1.getText().trim());

            // Aeronave
            String aerolineaStr = (String) jComboBox1.getSelectedItem();
            if (aerolineaStr != null) {
                int idAerolinea = Integer.parseInt(aerolineaStr.split(" - ")[0]);
                List<Aeronave> aeronaves = aeronaveService.listar();
                for (Aeronave a : aeronaves) {
                    if (a.getIdAerolinea() == idAerolinea) {
                        v.setIdAeronave(a.getIdAeronave());
                        break;
                    }
                }
            }

            // Origen
            String origenStr = (String) cmbOrigen.getSelectedItem();
            if (origenStr != null) {
                int idOrigen = Integer.parseInt(origenStr.split(" - ")[0]);
                v.setIdAeropuertoOrigen(idOrigen);
            }

            // Destino
            String destinoStr = (String) cmbOrigen1.getSelectedItem();
            if (destinoStr != null) {
                int idDestino = Integer.parseInt(destinoStr.split(" - ")[0]);
                v.setIdAeropuertoDestino(idDestino);
            }

            // Fecha y hora
            Date fecha = jDateChooser1.getDate();
            String horaStr = jTextField5.getText().trim();
            if (fecha != null && !horaStr.isEmpty()) {
                LocalDate localDate = new java.sql.Date(fecha.getTime()).toLocalDate();
                LocalTime localTime = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
                LocalDateTime fechaHoraSalida = LocalDateTime.of(localDate, localTime);
                v.setFechaHoraSalida(fechaHoraSalida);
                v.setFechaHoraLlegada(fechaHoraSalida.plusHours(2));
            }

            vueloService.modificarVuelo(v);
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Vuelo modificado exitosamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void eliminarVuelo() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vuelo para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este vuelo?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idVuelo = (int) tableModel.getValueAt(selectedRow, 0);
                vueloService.eliminarVuelo(idVuelo);
                cargarTabla();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Vuelo eliminado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar vuelo: " + e.getMessage());
            }
        }
    }

    private void cargarDatosSeleccionados() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int idVuelo = (int) tableModel.getValueAt(selectedRow, 0);
            Vuelo v = vueloService.obtenerVueloPorId(idVuelo);

            if (v != null) {
                jTextField1.setText(v.getNumeroVuelo());

                // Seleccionar aerolínea
                Aeronave aeronave = aeronaveService.obtenerPorId(v.getIdAeronave());
                if (aeronave != null) {
                    for (int i = 0; i < jComboBox1.getItemCount(); i++) {
                        String item = jComboBox1.getItemAt(i);
                        if (item.startsWith(aeronave.getIdAerolinea() + " - ")) {
                            jComboBox1.setSelectedIndex(i);
                            break;
                        }
                    }
                }

                // Seleccionar origen
                for (int i = 0; i < cmbOrigen.getItemCount(); i++) {
                    String item = cmbOrigen.getItemAt(i);
                    if (item.startsWith(v.getIdAeropuertoOrigen() + " - ")) {
                        cmbOrigen.setSelectedIndex(i);
                        break;
                    }
                }

                // Seleccionar destino
                for (int i = 0; i < cmbOrigen1.getItemCount(); i++) {
                    String item = cmbOrigen1.getItemAt(i);
                    if (item.startsWith(v.getIdAeropuertoDestino() + " - ")) {
                        cmbOrigen1.setSelectedIndex(i);
                        break;
                    }
                }

                // Fecha y hora
                if (v.getFechaHoraSalida() != null) {
                    LocalDate fecha = v.getFechaHoraSalida().toLocalDate();
                    jDateChooser1.setDate(java.sql.Date.valueOf(fecha));

                    LocalTime hora = v.getFechaHoraSalida().toLocalTime();
                    jTextField5.setText(hora.format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        }
    }

    private void limpiarCampos() {
        jTextField1.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jComboBox1.setSelectedIndex(-1);
        cmbOrigen.setSelectedIndex(-1);
        cmbOrigen1.setSelectedIndex(-1);
        jDateChooser1.setDate(null);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbOrigen1 = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Vuelo."));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Numero de vuelo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Aerolínea: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel3.setText("Origen: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Destino:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Fecha: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Hora: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Capacidad: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel7, gridBagConstraints);

        jTextField1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField1, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aeroméxico", "Magnicharters",
                "Aeromar", "Interjet", "Volaris", "Viva Aerobus", "Wingo", "Spirit" }));
        jComboBox1.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jComboBox1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jTextField6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jDateChooser1, gridBagConstraints);

        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Cancún",
                "YYZ - Toronto Pearson", "GIG -  Galeão", "SCL - Arturo Merino Benítez",
                "MAD - Adolfo Suárez Madrid-Barajas", "CDG - París-Charles de Gaulle", "BER - Berlín Brandeburgo",
                "FCO - Roma-Fiumicino", "NRT - Narita", "PEK - Pekín Capital", "SYD - Sídney", "CPT - Ciudad del Cabo",
                "DEL - Indira Gandhi", "CAI - El Cairo", "DXB - Dubái" }));
        cmbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(cmbOrigen, gridBagConstraints);

        cmbOrigen1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CUN - Cancún",
                "YYZ - Toronto Pearson", "GIG -  Galeão", "SCL - Arturo Merino Benítez",
                "MAD - Adolfo Suárez Madrid-Barajas", "CDG - París-Charles de Gaulle", "BER - Berlín Brandeburgo",
                "FCO - Roma-Fiumicino", "NRT - Narita", "PEK - Pekín Capital", "SYD - Sídney", "CPT - Ciudad del Cabo",
                "DEL - Indira Gandhi", "CAI - El Cairo", "DXB - Dubái" }));
        cmbOrigen1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigen1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(cmbOrigen1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Vuelos"));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                .addContainerGap()));

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        jButton1.setText("Agregar Vuelo");
        jPanel3.add(jButton1);

        jButton2.setText("Modificar Vuelo");
        jPanel3.add(jButton2);

        jButton3.setText("Eliminar Vuelo");
        jPanel3.add(jButton3);

        jButton4.setText("Limpiar Campos");
        jPanel3.add(jButton4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(34, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 156,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12,
                                        Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)));
    }// </editor-fold>//GEN-END:initComponents

    private void cmbOrigenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigenActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigenActionPerformed

    private void cmbOrigen1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cmbOrigen1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cmbOrigen1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JComboBox<String> cmbOrigen1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
