package com.tallertech.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal con pestañas para cada módulo.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("TallerTech S.R.L. — Sistema de Gestión de Órdenes de Trabajo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Clientes",           new ClientePanel());
        tabs.addTab("Vehículos",          new VehiculoPanel());
        tabs.addTab("Órdenes de Trabajo", new OrdenPanel());

        add(tabs, BorderLayout.CENTER);

        // Barra de estado
        JLabel status = new JLabel(" TallerTech v1.0 — Prototipo operacional");
        status.setBorder(BorderFactory.createEtchedBorder());
        add(status, BorderLayout.SOUTH);
    }
}
