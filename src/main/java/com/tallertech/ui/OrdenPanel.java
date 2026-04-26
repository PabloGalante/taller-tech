package com.tallertech.ui;

import com.tallertech.dao.MecanicoDAO;
import com.tallertech.dao.OrdenTrabajoDAO;
import com.tallertech.dao.VehiculoDAO;
import com.tallertech.model.Mecanico;
import com.tallertech.model.OrdenTrabajo;
import com.tallertech.model.OrdenTrabajo.Estado;
import com.tallertech.model.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrdenPanel extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final OrdenTrabajoDAO oDao = new OrdenTrabajoDAO();
    private final VehiculoDAO     vDao = new VehiculoDAO();
    private final MecanicoDAO     mDao = new MecanicoDAO();

    private final DefaultTableModel tableModel;
    private final JTable            table;

    // Formulario nueva orden
    private final JComboBox<Vehiculo>  cmbVehiculo    = new JComboBox<>();
    private final JTextField           txtDescripcion = new JTextField(30);
    private final JTextField           txtFechaEst    = new JTextField(10);
    private final JButton              btnCrear       = new JButton("Crear orden");

    // Acciones sobre orden seleccionada
    private final JButton              btnAvanzar     = new JButton("Avanzar estado");
    private final JComboBox<Mecanico>  cmbMecanico    = new JComboBox<>();
    private final JTextField           txtTarea       = new JTextField(30);
    private final JButton              btnAgregarTarea = new JButton("Agregar tarea");
    private final JCheckBox            chkSoloActivas  = new JCheckBox("Solo órdenes activas", true);

    private int idSeleccionado = -1;
    private Estado estadoSeleccionado = null;

    public OrdenPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ── Tabla ────────────────────────────────────────────────────────────
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Cliente", "Patente", "Descripción", "Estado", "Ingreso", "Est. entrega"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.add(chkSoloActivas);
        chkSoloActivas.addActionListener(e -> cargarTabla());
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(topBar, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // ── Panel inferior dividido ───────────────────────────────────────────
        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 0));

        // Panel: nueva orden
        JPanel pNueva = new JPanel(new GridBagLayout());
        pNueva.setBorder(BorderFactory.createTitledBorder("Nueva orden de trabajo"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        addField(pNueva, gc, 0, "Vehículo:",        cmbVehiculo);
        addField(pNueva, gc, 1, "Descripción:",     txtDescripcion);
        addField(pNueva, gc, 2, "Fecha est. (dd/MM/yyyy):", txtFechaEst);
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        pNueva.add(btnCrear, gc);
        bottom.add(pNueva);

        // Panel: acciones sobre orden seleccionada
        JPanel pAccion = new JPanel(new GridBagLayout());
        pAccion.setBorder(BorderFactory.createTitledBorder("Orden seleccionada"));
        gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        pAccion.add(btnAvanzar, gc);
        gc.gridwidth = 1;
        addField(pAccion, gc, 1, "Mecánico:", cmbMecanico);
        addField(pAccion, gc, 2, "Tarea:",    txtTarea);
        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        pAccion.add(btnAgregarTarea, gc);
        bottom.add(pAccion);

        add(bottom, BorderLayout.SOUTH);

        // ── Eventos ──────────────────────────────────────────────────────────
        btnCrear.addActionListener(e -> crearOrden());
        btnAvanzar.addActionListener(e -> avanzarEstado());
        btnAgregarTarea.addActionListener(e -> agregarTarea());

        cargarVehiculos();
        cargarMecanicos();
        cargarTabla();
    }

    private void cargarVehiculos() {
        cmbVehiculo.removeAllItems();
        try {
            for (Vehiculo v : vDao.listarTodos()) cmbVehiculo.addItem(v);
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void cargarMecanicos() {
        cmbMecanico.removeAllItems();
        try {
            for (Mecanico m : mDao.listarTodos()) cmbMecanico.addItem(m);
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void cargarTabla() {
        tableModel.setRowCount(0);
        try {
            List<OrdenTrabajo> lista = chkSoloActivas.isSelected()
                ? oDao.listarActivas() : oDao.listarTodas();
            for (OrdenTrabajo o : lista) {
                tableModel.addRow(new Object[]{
                    o.getId(),
                    o.getNombreCliente(),
                    o.getPatenteVehiculo(),
                    o.getDescripcion(),
                    o.getEstado().toString(),
                    o.getFechaIngreso().format(FMT),
                    o.getFechaEstimada() != null ? o.getFechaEstimada().format(FMT) : "—"
                });
            }
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void cargarSeleccion() {
        int fila = table.getSelectedRow();
        if (fila < 0) { idSeleccionado = -1; return; }
        idSeleccionado = (int) tableModel.getValueAt(fila, 0);
        String estadoStr = (String) tableModel.getValueAt(fila, 4);
        // Reverse-map display string to enum
        for (Estado e : Estado.values()) {
            if (e.toString().equals(estadoStr)) { estadoSeleccionado = e; break; }
        }
        Estado sig = estadoSeleccionado != null ? estadoSeleccionado.siguiente() : null;
        btnAvanzar.setText(sig != null ? "Avanzar a: " + sig : "Estado final (Entregado)");
        btnAvanzar.setEnabled(sig != null);
    }

    private void crearOrden() {
        Vehiculo v = (Vehiculo) cmbVehiculo.getSelectedItem();
        if (v == null || txtDescripcion.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Vehículo y descripción son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            OrdenTrabajo o = new OrdenTrabajo();
            o.setIdVehiculo(v.getId());
            o.setDescripcion(txtDescripcion.getText().trim());
            o.setEstado(Estado.RECIBIDO);
            o.setFechaIngreso(LocalDate.now());
            if (!txtFechaEst.getText().isBlank()) {
                o.setFechaEstimada(LocalDate.parse(txtFechaEst.getText().trim(), FMT));
            }
            oDao.insertar(o);
            txtDescripcion.setText("");
            txtFechaEst.setText("");
            cargarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/MM/yyyy.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void avanzarEstado() {
        if (idSeleccionado < 0 || estadoSeleccionado == null) return;
        Estado siguiente = estadoSeleccionado.siguiente();
        if (siguiente == null) return;
        try {
            oDao.actualizarEstado(idSeleccionado, siguiente);
            cargarTabla();
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void agregarTarea() {
        if (idSeleccionado < 0) {
            JOptionPane.showMessageDialog(this, "Seleccioná una orden primero.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Mecanico m = (Mecanico) cmbMecanico.getSelectedItem();
        if (m == null || txtTarea.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Seleccioná un mecánico e ingresá la descripción de la tarea.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            oDao.insertarTarea(idSeleccionado, m.getId(), txtTarea.getText().trim());
            txtTarea.setText("");
            JOptionPane.showMessageDialog(this, "Tarea registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) { mostrarError(ex); }
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
