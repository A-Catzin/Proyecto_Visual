/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui;

import com.mycompany.app_reserva_vuelos.model.*;
import com.mycompany.app_reserva_vuelos.service.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelReservas extends javax.swing.JPanel {

    private ReservaService reservaService;
    private VueloService vueloService;
    private PasajeroService pasajeroService;
    private AeropuertoService aeropuertoService;
    private UsuarioService usuarioService;
    private TarifaService tarifaService;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PanelReservas
     */
    public PanelReservas() {
        initComponents();
        this.reservaService = new ReservaServiceImpl();
        this.vueloService = new VueloServiceImpl();
        this.pasajeroService = new PasajeroServiceImpl();
        this.aeropuertoService = new AeropuertoServiceImpl();
        this.usuarioService = UsuarioServiceImpl.getInstance();
        this.tarifaService = new TarifaServiceImpl();
        inicializarTabla();
        cargarAeropuertos(); // ← CARGA AUTOMÁTICA DE AEROPUERTOS
        configurarEventos();
        cargarReservasUsuario();
    }

    private void inicializarTabla() {
        String[] columnNames = { "Código", "Fecha", "Estado", "Vuelos" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void cargarAeropuertos() {
        // Limpiar los ComboBox
        cmbOrigen.removeAllItems();
        cmbDestino.removeAllItems();

        // Obtener todos los aeropuertos de la base de datos
        List<Aeropuerto> aeropuertos = aeropuertoService.listarTodos();

        // Agregar cada aeropuerto al ComboBox con formato: "CÓDIGO - Nombre"
        for (Aeropuerto aeropuerto : aeropuertos) {
            String item = aeropuerto.getCodigoIATA() + " - " + aeropuerto.getNombreAeropuerto();
            cmbOrigen.addItem(item);
            cmbDestino.addItem(item);
        }
    }

    private void configurarEventos() {
        // Botón Realizar reserva
        btnrealizar.addActionListener(evt -> realizarReserva());

        // Botón Cancelar reserva
        btncancelar.addActionListener(evt -> cancelarReserva());

        // Botón Limpiar
        btnlmpiar.addActionListener(evt -> limpiarCampos());

        // Evento de selección de tabla
        jTable1.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                cargarDatosSeleccionados();
            }
        });
    }

    private void cargarReservasUsuario() {
        tableModel.setRowCount(0);

        // Obtener usuario autenticado
        Usuario usuarioActual = usuarioService.getUsuarioAutenticado();
        if (usuarioActual == null) {
            return;
        }

        // Buscar pasajero asociado al usuario
        Pasajero pasajero = pasajeroService.obtenerPasajeroPorEmail(usuarioActual.getEmail());
        if (pasajero == null) {
            return;
        }

        // Cargar reservas del pasajero
        List<Reserva> reservas = reservaService.listarReservasPorPasajero(pasajero.getIdPasajero());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Reserva reserva : reservas) {
            // Obtener detalles de la reserva
            List<DetalleReserva> detalles = reservaService.obtenerDetallesPorReserva(reserva.getIdReserva());

            String vuelosInfo = detalles.size() + " vuelo(s)";
            String fechaStr = reserva.getFechaReserva() != null ? reserva.getFechaReserva().format(dateFormatter)
                    : "N/A";

            tableModel.addRow(new Object[] {
                    reserva.getCodigoReserva(),
                    fechaStr,
                    reserva.getEstadoReserva(),
                    vuelosInfo
            });
        }
    }

    private void realizarReserva() {
        // Validar campos
        if (txtnombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del cliente.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cmbOrigen.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de origen.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cmbDestino.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un aeropuerto de destino.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jdsalida.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de salida.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer numPasajeros = (Integer) spinpasajeros.getValue();
        if (numPasajeros == null || numPasajeros <= 0) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un número válido de pasajeros.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Obtener usuario autenticado
            Usuario usuarioActual = usuarioService.getUsuarioAutenticado();
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar una reserva.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar o crear pasajero
            Pasajero pasajero = pasajeroService.obtenerPasajeroPorEmail(usuarioActual.getEmail());
            if (pasajero == null) {
                pasajero = new Pasajero();
                pasajero.setNombre(usuarioActual.getNombre());
                pasajero.setApellido("");
                pasajero.setEmail(usuarioActual.getEmail());
                int idPasajero = pasajeroService.registrarPasajero(pasajero);
                pasajero.setIdPasajero(idPasajero);
            }

            // Buscar vuelo disponible
            String origenStr = (String) cmbOrigen.getSelectedItem();
            String destinoStr = (String) cmbDestino.getSelectedItem();

            String codigoOrigen = origenStr.split(" - ")[0];
            String codigoDestino = destinoStr.split(" - ")[0];

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

            Date fechaSalida = jdsalida.getDate();
            LocalDate localDateSalida = fechaSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Vuelo> vuelosDisponibles = vueloService.buscarVuelos(idOrigen, idDestino, localDateSalida);

            if (vuelosDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay vuelos disponibles para la ruta y fecha seleccionadas.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tomar el primer vuelo disponible
            Vuelo vuelo = vuelosDisponibles.getFirst();

            // Obtener tarifas del vuelo
            List<Tarifa> tarifas = tarifaService.listarTarifasPorVuelo(vuelo.getIdVuelo());

            if (tarifas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay tarifas disponibles para este vuelo.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear reserva
            Reserva reserva = new Reserva();
            reserva.setIdPasajero(pasajero.getIdPasajero());
            reserva.setFechaReserva(LocalDateTime.now());
            reserva.setEstadoReserva("Confirmada");
            reserva.setCodigoReserva("RES-" + System.currentTimeMillis());

            // Crear detalle de reserva
            DetalleReserva detalle = new DetalleReserva();
            detalle.setIdVuelo(vuelo.getIdVuelo());
            detalle.setNumeroAsiento("A" + (int) (ThreadLocalRandom.current().nextDouble() * 30 + 1));
            detalle.setIdTarifa(tarifas.getFirst().getIdTarifa());

            List<DetalleReserva> detalles = new ArrayList<>();
            detalles.add(detalle);

            int idReserva = reservaService.crearReserva(reserva, detalles);

            if (idReserva > 0) {
                JOptionPane.showMessageDialog(this,
                        "Reserva creada exitosamente. Código: " + reserva.getCodigoReserva(), "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarReservasUsuario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al realizar la reserva: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cancelarReserva() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para cancelar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar esta reserva?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String codigoReserva = tableModel.getValueAt(selectedRow, 0).toString();

                // Buscar la reserva por código
                Usuario usuarioActual = usuarioService.getUsuarioAutenticado();
                Pasajero pasajero = pasajeroService.obtenerPasajeroPorEmail(usuarioActual.getEmail());
                List<Reserva> reservas = reservaService.listarReservasPorPasajero(pasajero.getIdPasajero());

                Reserva reservaACancelar = null;
                for (Reserva r : reservas) {
                    if (r.getCodigoReserva().equals(codigoReserva)) {
                        reservaACancelar = r;
                        break;
                    }
                }

                if (reservaACancelar != null) {
                    boolean resultado = reservaService.cancelarReserva(reservaACancelar.getIdReserva(), "Cancelada");
                    if (resultado) {
                        JOptionPane.showMessageDialog(this, "Reserva cancelada exitosamente.", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        cargarReservasUsuario();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar la reserva: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarDatosSeleccionados() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            // Cargar datos de la reserva seleccionada en los campos
            // Por ahora solo mostramos la información
        }
    }

    private void limpiarCampos() {
        txtnombre.setText("");
        cmbOrigen.setSelectedIndex(-1);
        cmbDestino.setSelectedIndex(-1);
        jdsalida.setDate(null);
        jdregreso.setDate(null);
        spinpasajeros.setValue(1);
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

        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
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
        txtnombre = new javax.swing.JTextField();
        jdsalida = new com.toedter.calendar.JDateChooser();
        jdregreso = new com.toedter.calendar.JDateChooser();
        cmbOrigen = new javax.swing.JComboBox<>();
        cmbDestino = new javax.swing.JComboBox<>();
        spinpasajeros = new javax.swing.JSpinner();

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Su(s) reserva(s)"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null },
                        { null, null, null, null, null }
                },
                new String[] {
                        "Origen", "Destino", "Pasajeros", "Salida", "Regreso"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class,
                    java.lang.Object.class
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                .addContainerGap()));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        btnrealizar.setText("Realizar reserva");

        btncancelar.setText("Cancelar reserva");

        btnlmpiar.setText("Limpiar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(btnrealizar)
                                .addGap(18, 18, 18)
                                .addComponent(btncancelar)
                                .addGap(18, 18, 18)
                                .addComponent(btnlmpiar)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnrealizar)
                                        .addComponent(btncancelar)
                                        .addComponent(btnlmpiar))
                                .addContainerGap(16, Short.MAX_VALUE)));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la reserva."));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        lblorigen.setText("Origen:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblorigen, gridBagConstraints);

        lbldestino.setText("Destino:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lbldestino, gridBagConstraints);

        lblnombre.setText("Nombre del cliente:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblnombre, gridBagConstraints);

        lblsalida.setText("Salida:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblsalida, gridBagConstraints);

        lblregreso.setText("Regreso:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblregreso, gridBagConstraints);

        lblpasajeros.setText("Pasajeros:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(lblpasajeros, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtnombre, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jdsalida, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jdregreso, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbOrigen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cmbDestino, gridBagConstraints);

        spinpasajeros.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(spinpasajeros, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 790,
                                                Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(34, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
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
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnlmpiar;
    private javax.swing.JButton btnrealizar;
    private javax.swing.JComboBox<String> cmbDestino;
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser jdregreso;
    private com.toedter.calendar.JDateChooser jdsalida;
    private javax.swing.JLabel lbldestino;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lblorigen;
    private javax.swing.JLabel lblpasajeros;
    private javax.swing.JLabel lblregreso;
    private javax.swing.JLabel lblsalida;
    private javax.swing.JSpinner spinpasajeros;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables
}
