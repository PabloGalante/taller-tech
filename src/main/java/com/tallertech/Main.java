package com.tallertech;

import com.tallertech.db.DBConnection;
import com.tallertech.ui.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // Verificar conexión a la base de datos antes de abrir la UI
        try {
            DBConnection.get();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                "No se pudo conectar a la base de datos MySQL.\n\n" +
                "Verificá que XAMPP esté corriendo y que el schema haya sido importado.\n\n" +
                "Detalle: " + ex.getMessage(),
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Iniciar UI en el Event Dispatch Thread de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });

        // Cerrar conexión al salir
        Runtime.getRuntime().addShutdownHook(new Thread(DBConnection::close));
    }
}
