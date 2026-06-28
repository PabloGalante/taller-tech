package com.tallertech.menu;

import com.tallertech.dao.ServicioDAO;
import com.tallertech.model.Servicio;
import com.tallertech.model.ServicioMantenimiento;
import com.tallertech.model.ServicioReparacion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuServicios {

    private final ServicioDAO dao = new ServicioDAO();
    private final Scanner     sc;

    public MenuServicios(Scanner sc) { this.sc = sc; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n┌── GESTIÓN DE SERVICIOS ──────────────────┐");
            System.out.println("│  1. Listar servicios (ordenado por costo) │");
            System.out.println("│  2. Buscar servicio por ID                │");
            System.out.println("│  3. Registrar servicio de mantenimiento   │");
            System.out.println("│  4. Registrar servicio de reparación      │");
            System.out.println("│  5. Eliminar servicio                     │");
            System.out.println("│  6. Ver costo total de servicios          │");
            System.out.println("│  0. Volver                                │");
            System.out.println("└───────────────────────────────────────────┘");

            switch (Util.leerEntero(sc, "Opción: ")) {
                case 1 -> listarOrdenado();
                case 2 -> buscar();
                case 3 -> registrarMantenimiento();
                case 4 -> registrarReparacion();
                case 5 -> eliminar();
                case 6 -> costoTotal();
                case 0 -> volver = true;
                default -> System.out.println("  ✗ Opción inválida.");
            }
        }
    }

    private void listarOrdenado() {
        try {
            ArrayList<Servicio> lista = dao.listarTodos();
            if (lista.isEmpty()) { System.out.println("  Sin servicios."); return; }

            // Ordenamiento burbuja por costo — POLIMORFISMO en calcularCosto()
            int n = lista.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (lista.get(j).calcularCosto() > lista.get(j+1).calcularCosto()) {
                        Servicio tmp = lista.get(j);
                        lista.set(j, lista.get(j+1));
                        lista.set(j+1, tmp);
                    }
                }
            }

            System.out.println("  (ordenado por costo ascendente)");
            for (int i = 0; i < lista.size(); i++) {
                System.out.printf("  %d. %s%n", i+1, lista.get(i).getResumen());
            }
            System.out.printf("  Total de servicios: %d%n", lista.size());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void buscar() {
        int id = Util.leerEntero(sc, "ID del servicio: ");
        try {
            Servicio s = dao.buscarPorId(id);
            if (s != null) System.out.println("  " + s.getResumen());
            else System.out.println("  ✗ Servicio no encontrado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void registrarMantenimiento() {
        System.out.println("\n  — Nuevo servicio de mantenimiento —");
        String desc    = Util.leerTexto(sc, "Descripción: ");
        double precio  = Util.leerDouble(sc, "Precio base (mano de obra $): ");
        String tipo    = Util.leerTexto(sc, "Tipo de mantenimiento: ");
        double insumos = Util.leerDouble(sc, "Costo de insumos $: ");

        ServicioMantenimiento sm =
            new ServicioMantenimiento(0, desc, precio, tipo, insumos);
        try {
            dao.insertar(sm);
            System.out.println("  ✓ Servicio registrado. ID: " + sm.getId() +
                               " | " + sm.getResumen());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void registrarReparacion() {
        System.out.println("\n  — Nuevo servicio de reparación —");
        String desc       = Util.leerTexto(sc, "Descripción: ");
        double precio     = Util.leerDouble(sc, "Precio base $: ");
        String pieza      = Util.leerTexto(sc, "Pieza a reparar: ");
        int    complejidad = Util.leerEntero(sc, "Nivel de complejidad (1=simple, 2=media, 3=alta): ");

        ServicioReparacion sr =
            new ServicioReparacion(0, desc, precio, pieza, complejidad);
        try {
            dao.insertar(sr);
            System.out.println("  ✓ Servicio registrado. ID: " + sr.getId() +
                               " | " + sr.getResumen());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void eliminar() {
        int id = Util.leerEntero(sc, "ID del servicio a eliminar: ");
        String conf = Util.leerTexto(sc, "¿Confirmar? (s/n): ");
        if (!conf.equalsIgnoreCase("s")) return;
        try {
            dao.eliminar(id);
            System.out.println("  ✓ Servicio eliminado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void costoTotal() {
        try {
            ArrayList<Servicio> lista = dao.listarTodos();
            double total = 0;
            for (Servicio s : lista) total += s.calcularCosto(); // POLIMORFISMO
            System.out.printf("  Servicios registrados: %d | Costo total: $%.2f%n",
                lista.size(), total);
        } catch (SQLException e) { Util.errorDB(e); }
    }
}
