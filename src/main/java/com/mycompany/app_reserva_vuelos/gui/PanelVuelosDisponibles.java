/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui;

import com.mycompany.app_reserva_vuelos.model.Aerolinea;
import com.mycompany.app_reserva_vuelos.model.Aeronave;
import com.mycompany.app_reserva_vuelos.model.Aeropuerto;
import com.mycompany.app_reserva_vuelos.model.Tarifa;
import com.mycompany.app_reserva_vuelos.model.Vuelo;
import com.mycompany.app_reserva_vuelos.service.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelVuelosDisponibles extends javax.swing.JPanel {

    private VueloService vueloService;
    private AeropuertoService aeropuertoService;
    private AerolineaService aerolineaService;
    private AeronaveService aeronaveService;
    private TarifaService tarifaService;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PanelReservas
     */
    public PanelVuelosDisponibles() {
        initComponents();
        this.vueloService = new VueloServiceImpl();
        this.aeropuertoService = new AeropuertoServiceImpl();
        this.aerolineaService = new AerolineaServiceImpl();
        this.aeronaveService = new AeronaveServiceImpl();
        this.tarifaService = new TarifaServiceImpl();
        inicializarTabla();
        cargarVuelosDisponibles();
    }

    private void inicializarTabla() {
        String[] columnNames = {
                "Número Vuelo",
                "Aerolínea",
                "Origen",
                "Destino",
                "Fecha Salida",
                "Hora Salida",
                "Fecha Llegada",
                "Hora Llegada",
                "Precio Desde",
                "Asientos Disp."
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(tableModel);
    }

    private void cargarVuelosDisponibles() {
        tableModel.setRowCount(0);

        List<Vuelo> vuelos = vueloService.listarVuelos();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Vuelo vuelo : vuelos) {
            // Obtener aerolínea
            Aeronave aeronave = aeronaveService.obtenerPorId(vuelo.getIdAeronave());
            String nombreAerolinea = "N/A";
            if (aeronave != null) {
                Aerolinea aerolinea = aerolineaService.obtenerAerolineaPorId(aeronave.getIdAerolinea());
                if (aerolinea != null) {
                    nombreAerolinea = aerolinea.getNombreAerolinea();
                }
            }

            // Obtener aeropuertos
            Aeropuerto origen = aeropuertoService.obtenerPorId(vuelo.getIdAeropuertoOrigen());
            String nombreOrigen = origen != null ? origen.getCodigoIATA() + " - " + origen.getNombreAeropuerto()
                    : "N/A";

            Aeropuerto destino = aeropuertoService.obtenerPorId(vuelo.getIdAeropuertoDestino());
            String nombreDestino = destino != null ? destino.getCodigoIATA() + " - " + destino.getNombreAeropuerto()
                    : "N/A";

            // Obtener precio mínimo de tarifas
            List<Tarifa> tarifas = tarifaService.listarTarifasPorVuelo(vuelo.getIdVuelo());
            BigDecimal precioMinimo = BigDecimal.ZERO;
            if (!tarifas.isEmpty()) {
                precioMinimo = tarifas.get(0).getPrecio();
                for (Tarifa t : tarifas) {
                    if (t.getPrecio().compareTo(precioMinimo) < 0) {
                        precioMinimo = t.getPrecio();
                    }
                }
            }

            // Formatear fechas
            String fechaSalida = vuelo.getFechaHoraSalida() != null ? vuelo.getFechaHoraSalida().format(dateFormatter)
                    : "";
            String horaSalida = vuelo.getFechaHoraSalida() != null ? vuelo.getFechaHoraSalida().format(timeFormatter)
                    : "";
            String fechaLlegada = vuelo.getFechaHoraLlegada() != null
                    ? vuelo.getFechaHoraLlegada().format(dateFormatter)
                    : "";
            String horaLlegada = vuelo.getFechaHoraLlegada() != null ? vuelo.getFechaHoraLlegada().format(timeFormatter)
                    : "";

            // Asientos disponibles (simplificado - capacidad total de la aeronave)
            int asientosDisponibles = aeronave != null ? aeronave.getCapacidadAsientos() : 0;

            tableModel.addRow(new Object[] {
                    vuelo.getNumeroVuelo(),
                    nombreAerolinea,
                    nombreOrigen,
                    nombreDestino,
                    fechaSalida,
                    horaSalida,
                    fechaLlegada,
                    horaLlegada,
                    "$" + precioMinimo.toString(),
                    asientosDisponibles
            });
        }
    }

    /**
     * Método público para actualizar la tabla desde otros paneles
     */
    public void actualizarVuelos() {
        cargarVuelosDisponibles();
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

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la reserva."));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                                .addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
