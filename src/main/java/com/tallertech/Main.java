package com.tallertech;

import com.tallertech.db.DBConnection;
import com.tallertech.menu.MenuPrincipal;

import javax.swing.JOptionPane;
import java.sql.SQLException;

/**
 * Punto de entrada del sistema TallerTech S.R.L. — versión final TP4.
 *
 * Integra:
 *  - Patrón Singleton (DBConnection)
 *  - Patrón DAO (ClienteDAO, VehiculoDAO, OrdenTrabajoDAO, ServicioDAO)
 *  - Interface IDao<T> para abstracción del acceso a datos
 *  - Jerarquía POO: Servicio → ServicioMantenimiento / ServicioReparacion
 *  - Persistencia MySQL con manejo de excepciones
 *  - Arreglos y ArrayList de forma complementaria
 *  - Menú de consola con estructuras de control
 */
public class Main {
    public static void main(String[] args) {
        // Verificar conexión antes de iniciar
        try {
            DBConnection.get();
            System.out.println("  ✓ Conexión a MySQL establecida.");
        } catch (SQLException e) {
            System.err.println("  ✗ No se pudo conectar a MySQL.");
            System.err.println("    Verificá que XAMPP esté corriendo.");
            System.err.println("    Detalle: " + e.getMessage());
            System.exit(1);
        }

        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();

        // Cerrar conexión al salir
        DBConnection.close();
    }
}
