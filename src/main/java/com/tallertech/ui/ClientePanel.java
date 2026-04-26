package com.tallertech.ui;

import com.tallertech.dao.ClienteDAO;
import com.tallertech.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClientePanel extends JPanel {

    private final ClienteDAO dao = new ClienteDAO();

    private final DefaultTableModel tableModel;
    private final JTable            table;
    private final JTextField        txtNombre   = new JTextField(15);
    private final JTextField        txtApellido = new JTextField(15);
    private final JTextField        txtTelefono = new JTextField(12);
    private final JTextField        txtEmail    = new JTextField(20);
    private final JButton           btnGuardar  = new JButton("Guardar");
    private final JButton           btnNuevo    = new JButton("Nuevo");
    private final JButton           btnEliminar = new JButton("Eliminar");

    private int idSeleccionado = -1;

    public ClientePanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Tabla ────────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(new String[]{"ID", "Apellido", "Nombre", "Teléfono", "Email"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // ── Formulario ───────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        addField(form, gc, 0, "Nombre:",   txtNombre);
        addField(form, gc, 1, "Apellido:", txtApellido);
        addField(form, gc, 2, "Teléfono:", txtTelefono);
        addField(form, gc, 3, "Email:",    txtEmail);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        gc.gridx = 0; gc.gridy = 4; gc.gridwidth = 2;
        form.add(botones, gc);

        add(form, BorderLayout.SOUTH);

        // ── Eventos ──────────────────────────────────────────────────────────
        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarTabla();
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            List<Cliente> lista = dao.listarTodos();
            for (Cliente c : lista) {
                tableModel.addRow(new Object[]{c.getId(), c.getApellido(), c.getNombre(), c.getTelefono(), c.getEmail()});
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = table.getSelectedRow();
        if (fila < 0) return;
        idSeleccionado = (int) tableModel.getValueAt(fila, 0);
        txtApellido.setText((String) tableModel.getValueAt(fila, 1));
        txtNombre.setText((String)   tableModel.getValueAt(fila, 2));
        txtTelefono.setText((String) tableModel.getValueAt(fila, 3));
        txtEmail.setText((String)    tableModel.getValueAt(fila, 4));
    }

    private void guardar() {
        if (txtNombre.getText().isBlank() || txtApellido.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Nombre y apellido son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Cliente c = new Cliente();
            c.setNombre(txtNombre.getText().trim());
            c.setApellido(txtApellido.getText().trim());
            c.setTelefono(txtTelefono.getText().trim());
            c.setEmail(txtEmail.getText().trim());

            if (idSeleccionado < 0) {
                dao.insertar(c);
            } else {
                c.setId(idSeleccionado);
                dao.actualizar(c);
            }
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        if (idSeleccionado < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el cliente seleccionado y todos sus vehículos?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            dao.eliminar(idSeleccionado);
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        table.clearSelection();
    }

    private void addField(JPanel panel, GridBagConstraints gc, int row, String label, JTextField field) {
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = row;
        panel.add(new JLabel(label), gc);
        gc.gridx = 1;
        panel.add(field, gc);
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error de base de datos", JOptionPane.ERROR_MESSAGE);
    }
}
