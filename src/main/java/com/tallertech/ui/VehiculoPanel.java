package com.tallertech.ui;

import com.tallertech.dao.ClienteDAO;
import com.tallertech.dao.VehiculoDAO;
import com.tallertech.model.Cliente;
import com.tallertech.model.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VehiculoPanel extends JPanel {

    private final VehiculoDAO vDao = new VehiculoDAO();
    private final ClienteDAO  cDao = new ClienteDAO();

    private final DefaultTableModel tableModel;
    private final JTable            table;
    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final JTextField txtMarca   = new JTextField(12);
    private final JTextField txtModelo  = new JTextField(12);
    private final JTextField txtAnio    = new JTextField(6);
    private final JTextField txtPatente = new JTextField(10);
    private final JButton    btnGuardar  = new JButton("Guardar");
    private final JButton    btnNuevo    = new JButton("Nuevo");
    private final JButton    btnEliminar = new JButton("Eliminar");

    private int idSeleccionado = -1;

    public VehiculoPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Patente", "Marca", "Modelo", "Año", "Cliente"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del vehículo"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        addField(form, gc, 0, "Cliente:", cmbCliente);
        addField(form, gc, 1, "Patente:", txtPatente);
        addField(form, gc, 2, "Marca:",   txtMarca);
        addField(form, gc, 3, "Modelo:",  txtModelo);
        addField(form, gc, 4, "Año:",     txtAnio);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        form.add(botones, gc);

        add(form, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());

        cargarClientes();
        cargarTabla();
    }

    private void cargarClientes() {
        cmbCliente.removeAllItems();
        try {
            for (Cliente c : cDao.listarTodos()) cmbCliente.addItem(c);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            for (Vehiculo v : vDao.listarTodos()) {
                tableModel.addRow(new Object[]{
                    v.getId(), v.getPatente(), v.getMarca(),
                    v.getModelo(), v.getAnio(), v.getNombreCliente()
                });
            }
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void cargarSeleccion() {
        int fila = table.getSelectedRow();
        if (fila < 0) return;
        idSeleccionado = (int) tableModel.getValueAt(fila, 0);
        txtPatente.setText((String) tableModel.getValueAt(fila, 1));
        txtMarca.setText((String)   tableModel.getValueAt(fila, 2));
        txtModelo.setText((String)  tableModel.getValueAt(fila, 3));
        txtAnio.setText(String.valueOf(tableModel.getValueAt(fila, 4)));
    }

    private void guardar() {
        if (txtPatente.getText().isBlank() || txtMarca.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Patente y marca son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente clienteSeleccionado = (Cliente) cmbCliente.getSelectedItem();
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccioná un cliente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Vehiculo v = new Vehiculo();
            v.setIdCliente(clienteSeleccionado.getId());
            v.setPatente(txtPatente.getText().trim());
            v.setMarca(txtMarca.getText().trim());
            v.setModelo(txtModelo.getText().trim());
            v.setAnio(txtAnio.getText().isBlank() ? 0 : Integer.parseInt(txtAnio.getText().trim()));

            if (idSeleccionado < 0) {
                vDao.insertar(v);
            } else {
                v.setId(idSeleccionado);
                vDao.actualizar(v);
            }
            limpiarFormulario();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void eliminar() {
        if (idSeleccionado < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el vehículo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            vDao.eliminar(idSeleccionado);
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            mostrarError(ex);
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtPatente.setText(""); txtMarca.setText("");
        txtModelo.setText(""); txtAnio.setText("");
        table.clearSelection();
    }

    private void addField(JPanel p, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = row;
        p.add(new JLabel(label), gc);
        gc.gridx = 1;
        p.add(field, gc);
    }

    private void mostrarError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
