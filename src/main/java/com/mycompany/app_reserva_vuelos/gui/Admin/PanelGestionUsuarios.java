/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.app_reserva_vuelos.gui.Admin;

import com.mycompany.app_reserva_vuelos.model.Usuario;
import com.mycompany.app_reserva_vuelos.service.UsuarioService;
import com.mycompany.app_reserva_vuelos.service.UsuarioServiceImpl;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bogard Axel
 */
public class PanelGestionUsuarios extends javax.swing.JPanel {

        private final UsuarioService usuarioService;
        private DefaultTableModel modeloTabla;

        /**
         * Creates new form PanelGestionUsuarios
         */
        public PanelGestionUsuarios() {
                initComponents();
                this.usuarioService = UsuarioServiceImpl.getInstance();
                inicializarTabla();
                cargarTabla();
                configurarEventos();
        }

        private void inicializarTabla() {
                modeloTabla = new DefaultTableModel(
                                new Object[][] {},
                                new String[] {
                                                "ID", "Usuario", "Nombre", "Email", "Teléfono", "Rol", "Estado"
                                }) {
                        boolean[] canEdit = new boolean[] {
                                        false, false, false, false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return canEdit[columnIndex];
                        }
                };
                jTable1.setModel(modeloTabla);
        }

        private void cargarTabla() {
                modeloTabla.setRowCount(0); // Limpiar tabla
                List<Usuario> usuarios = usuarioService.listarUsuarios();
                for (Usuario u : usuarios) {
                        modeloTabla.addRow(new Object[] {
                                        u.getId(),
                                        u.getUsuario(),
                                        u.getNombre(),
                                        u.getEmail(),
                                        u.getTelefono(),
                                        u.getRol(),
                                        u.getEstado()
                        });
                }
        }

        private void configurarEventos() {
                // Evento de selección de tabla para llenar campos
                jTable1.getSelectionModel().addListSelectionListener(e -> {
                        if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                                llenarCamposDesdeTabla();
                        }
                });

                // Botón Agregar
                jButton1.addActionListener(e -> agregarUsuario());

                // Botón Modificar
                jButton2.addActionListener(e -> modificarUsuario());

                // Botón Eliminar
                jButton3.addActionListener(e -> eliminarUsuario());

                // Botón Limpiar
                jButton5.addActionListener(e -> limpiarCampos());

                // Botón Reestablecer Contraseña (Opcional, por ahora solo limpia o muestra
                // mensaje)
                jButton4.addActionListener(
                                e -> JOptionPane.showMessageDialog(this, "Funcionalidad no implementada aún."));
        }

        private void llenarCamposDesdeTabla() {
                int fila = jTable1.getSelectedRow();
                if (fila != -1) {
                        // ID, Usuario, Nombre, Email, Teléfono, Rol, Estado
                        jTextField1
                                        .setText(modeloTabla.getValueAt(fila, 1) != null
                                                        ? modeloTabla.getValueAt(fila, 1).toString()
                                                        : ""); // Usuario
                        jTextField5
                                        .setText(modeloTabla.getValueAt(fila, 2) != null
                                                        ? modeloTabla.getValueAt(fila, 2).toString()
                                                        : ""); // Nombre
                        jTextField4
                                        .setText(modeloTabla.getValueAt(fila, 3) != null
                                                        ? modeloTabla.getValueAt(fila, 3).toString()
                                                        : ""); // Email
                        jTextField3
                                        .setText(modeloTabla.getValueAt(fila, 4) != null
                                                        ? modeloTabla.getValueAt(fila, 4).toString()
                                                        : ""); // Teléfono

                        String rol = modeloTabla.getValueAt(fila, 5) != null
                                        ? modeloTabla.getValueAt(fila, 5).toString()
                                        : "Cliente";
                        jComboBox2.setSelectedItem(rol);

                        String estado = modeloTabla.getValueAt(fila, 6) != null
                                        ? modeloTabla.getValueAt(fila, 6).toString()
                                        : "Activo";
                        jComboBox3.setSelectedItem(estado);

                        // No llenamos la contraseña por seguridad
                        jPasswordField1.setText("");
                }
        }

        private void agregarUsuario() {
                String usuario = jTextField1.getText().trim();
                String nombre = jTextField5.getText().trim();
                String email = jTextField4.getText().trim();
                String telefono = jTextField3.getText().trim();
                String password = new String(jPasswordField1.getPassword());
                String rol = (String) jComboBox2.getSelectedItem();
                String estado = (String) jComboBox3.getSelectedItem();

                if (usuario.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Usuario, Nombre y Contraseña son obligatorios.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                Usuario u = new Usuario();
                u.setUsuario(usuario);
                u.setNombre(nombre);
                u.setEmail(email);
                u.setTelefono(telefono);
                u.setContraseña(password);
                u.setRol(rol);
                u.setEstado(estado);

                int id = usuarioService.registrarUsuario(u);
                if (id > 0) {
                        JOptionPane.showMessageDialog(this, "Usuario agregado con éxito.");
                        limpiarCampos();
                        cargarTabla();
                } else if (id == -1) {
                        JOptionPane.showMessageDialog(this, "El usuario ya existe.", "Error",
                                        JOptionPane.WARNING_MESSAGE);
                } else {
                        JOptionPane.showMessageDialog(this, "Error al agregar usuario.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }

        private void modificarUsuario() {
                int fila = jTable1.getSelectedRow();
                if (fila == -1) {
                        JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int id = (int) modeloTabla.getValueAt(fila, 0);
                String usuario = jTextField1.getText().trim();
                String nombre = jTextField5.getText().trim();
                String email = jTextField4.getText().trim();
                String telefono = jTextField3.getText().trim();
                String password = new String(jPasswordField1.getPassword());
                String rol = (String) jComboBox2.getSelectedItem();
                String estado = (String) jComboBox3.getSelectedItem();

                Usuario u = new Usuario();
                u.setId(id);
                u.setUsuario(usuario);
                u.setNombre(nombre);
                u.setEmail(email);
                u.setTelefono(telefono);
                u.setContraseña(password.isEmpty() ? null : password); // Si está vacía, no cambiar (lógica a manejar en
                                                                       // DAO/Service si se desea, o aquí)
                // Nota: El DAO actual actualiza todo. Si la contraseña es null/vacía, la
                // borraría o pondría vacía.
                // Deberíamos manejar esto mejor. Por ahora, obligamos a poner contraseña o
                // recuperamos la vieja si pudiéramos.
                // Dado que no podemos recuperar la vieja fácilmente sin consultar, y el DAO
                // hace update de todo...
                // Lo ideal es que si el campo password está vacío, no se actualice.
                // Pero el DAO 'modificar' hace UPDATE contraseña = ?.
                // Vamos a asumir que si se deja vacío, se mantiene la actual (requiere cambio
                // en DAO o consultar primero).
                // Para simplificar: Si está vacío, avisar que se requiere contraseña o
                // implementar lógica de "mantener anterior".
                // Voy a consultar el usuario actual para mantener la contraseña si el campo
                // está vacío.

                if (password.isEmpty()) {
                        // Recuperar usuario actual para no perder la contraseña
                        // Esto es ineficiente pero seguro para este nivel de implementación.
                        // Sin embargo, no tengo método 'obtenerPorId' en UsuarioService expuesto en la
                        // interfaz (solo obtenerPorNombreUsuario).
                        // Usaré obtenerPorNombreUsuario con el nombre original de la tabla? No, el
                        // nombre puede haber cambiado.
                        // Necesito obtenerPorId en el servicio.
                        // Por ahora, exigiré contraseña para modificar, o asumiré que se quiere
                        // cambiar.
                        // O mejor: Si está vacía, NO la cambio. Pero el DAO la cambiará a vacía.
                        // Voy a dejarlo así: Si está vacía, se guarda vacía (mala práctica) o aviso.
                        // Avisaré.
                        JOptionPane.showMessageDialog(this,
                                        "Para modificar, reingrese la contraseña o ingrese una nueva.", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                u.setContraseña(password);
                u.setRol(rol);
                u.setEstado(estado);

                usuarioService.modificarUsuario(u);
                JOptionPane.showMessageDialog(this, "Usuario modificado con éxito.");
                limpiarCampos();
                cargarTabla();
        }

        private void eliminarUsuario() {
                int fila = jTable1.getSelectedRow();
                if (fila == -1) {
                        JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Aviso",
                                        JOptionPane.WARNING_MESSAGE);
                        return;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario?", "Confirmar",
                                JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                        int id = (int) modeloTabla.getValueAt(fila, 0);
                        usuarioService.eliminarUsuario(id);
                        JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                        limpiarCampos();
                        cargarTabla();
                }
        }

        private void limpiarCampos() {
                jTextField1.setText("");
                jTextField5.setText("");
                jTextField4.setText("");
                jTextField3.setText("");
                jPasswordField1.setText("");
                jComboBox2.setSelectedIndex(0);
                jComboBox3.setSelectedIndex(0);
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
                jLabel7 = new javax.swing.JLabel();
                jTextField1 = new javax.swing.JTextField();
                jTextField3 = new javax.swing.JTextField();
                jComboBox2 = new javax.swing.JComboBox<>();
                jComboBox3 = new javax.swing.JComboBox<>();
                jTextField4 = new javax.swing.JTextField();
                jTextField5 = new javax.swing.JTextField();
                jPasswordField1 = new javax.swing.JPasswordField();
                jPanel3 = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                jTable1 = new javax.swing.JTable();
                jPanel4 = new javax.swing.JPanel();
                jButton1 = new javax.swing.JButton();
                jButton2 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();
                jButton4 = new javax.swing.JButton();
                jButton5 = new javax.swing.JButton();

                jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Usuario."));
                jPanel2.setLayout(new java.awt.GridBagLayout());

                jLabel1.setText("Usuario:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel1, gridBagConstraints);

                jLabel2.setText("Contraseña:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel2, gridBagConstraints);

                jLabel3.setText("Nombre completo:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel3, gridBagConstraints);

                jLabel4.setText("EMail:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 6;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel4, gridBagConstraints);

                jLabel5.setText("Teléfono:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel5, gridBagConstraints);

                jLabel6.setText("Rol:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel6, gridBagConstraints);

                jLabel7.setText("Estado:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jLabel7, gridBagConstraints);

                jTextField1.setToolTipText("");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jTextField1, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jTextField3, gridBagConstraints);

                jComboBox2.setModel(
                                new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "Cliente", "Administrador", "Operador" }));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jComboBox2, gridBagConstraints);

                jComboBox3
                                .setModel(new javax.swing.DefaultComboBoxModel<>(
                                                new String[] { "Activo", "Inactivo", "Suspendido" }));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jComboBox3, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 6;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jTextField4, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 4;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jTextField5, gridBagConstraints);
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
                jPanel2.add(jPasswordField1, gridBagConstraints);

                jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de Usuarios"));

                jTable1.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {},
                                new String[] {
                                                "ID", "Usuario", "Nombre", "Email", "Teléfono", "Rol", "Estado"
                                }));
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
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                181, Short.MAX_VALUE)
                                                                .addContainerGap()));

                jPanel4.setBackground(new java.awt.Color(153, 153, 153));

                jButton1.setText("Agregar Usuario");
                jPanel4.add(jButton1);

                jButton2.setText("Modificar");
                jPanel4.add(jButton2);

                jButton3.setText("Eliminar");
                jPanel4.add(jButton3);

                jButton4.setText("Reestablecer Contraseña");
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
                                                                                .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                false)
                                                                                .addComponent(jPanel3,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                790, Short.MAX_VALUE)
                                                                                .addComponent(jPanel4,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addContainerGap(34, Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addComponent(jPanel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                156,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jPanel3,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                12,
                                                                                Short.MAX_VALUE)
                                                                .addComponent(jPanel4,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)));

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 848, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 481, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(0, 0, Short.MAX_VALUE))));
        }// </editor-fold>//GEN-END:initComponents

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_jButton1ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton5;
        private javax.swing.JComboBox<String> jComboBox2;
        private javax.swing.JComboBox<String> jComboBox3;
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
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPasswordField jPasswordField1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField3;
        private javax.swing.JTextField jTextField4;
        private javax.swing.JTextField jTextField5;
        // End of variables declaration//GEN-END:variables
}
