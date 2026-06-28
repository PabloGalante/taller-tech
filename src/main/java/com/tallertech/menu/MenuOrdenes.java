package com.tallertech.menu;

import com.tallertech.dao.OrdenTrabajoDAO;
import com.tallertech.dao.VehiculoDAO;
import com.tallertech.model.OrdenTrabajo;
import com.tallertech.model.OrdenTrabajo.Estado;
import com.tallertech.model.Vehiculo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuOrdenes {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final OrdenTrabajoDAO oDao = new OrdenTrabajoDAO();
    private final VehiculoDAO     vDao = new VehiculoDAO();
    private final Scanner         sc;

    public MenuOrdenes(Scanner sc) { this.sc = sc; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n┌── GESTIÓN DE ÓRDENES DE TRABAJO ─────────┐");
            System.out.println("│  1. Listar órdenes activas                │");
            System.out.println("│  2. Listar todas las órdenes              │");
            System.out.println("│  3. Crear nueva orden                     │");
            System.out.println("│  4. Avanzar estado de orden               │");
            System.out.println("│  5. Eliminar orden                        │");
            System.out.println("│  0. Volver                                │");
            System.out.println("└───────────────────────────────────────────┘");

            switch (Util.leerEntero(sc, "Opción: ")) {
                case 1 -> listarActivas();
                case 2 -> listarTodas();
                case 3 -> crear();
                case 4 -> avanzarEstado();
                case 5 -> eliminar();
                case 0 -> volver = true;
                default -> System.out.println("  ✗ Opción inválida.");
            }
        }
    }

    private void listarActivas() {
        try {
            ArrayList<OrdenTrabajo> lista = oDao.listarActivas();
            if (lista.isEmpty()) { System.out.println("  No hay órdenes activas."); return; }
            for (OrdenTrabajo o : lista) System.out.println("  " + o);
            System.out.printf("  Total activas: %d%n", lista.size());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void listarTodas() {
        try {
            ArrayList<OrdenTrabajo> lista = oDao.listarTodos();
            if (lista.isEmpty()) { System.out.println("  No hay órdenes registradas."); return; }
            for (OrdenTrabajo o : lista) System.out.println("  " + o);
            System.out.printf("  Total: %d orden(es)%n", lista.size());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void crear() {
        System.out.println("\n  — Nueva orden de trabajo —");
        try {
            ArrayList<Vehiculo> vehiculos = vDao.listarTodos();
            if (vehiculos.isEmpty()) {
                System.out.println("  ✗ No hay vehículos. Registre uno primero.");
                return;
            }
            for (Vehiculo v : vehiculos) System.out.println("  " + v);

            int idVehiculo = Util.leerEntero(sc, "ID del vehículo: ");
            String desc    = Util.leerTexto(sc, "Descripción del trabajo: ");
            String fechaStr = Util.leerTexto(sc, "Fecha estimada de entrega (dd/MM/yyyy): ");

            OrdenTrabajo o = new OrdenTrabajo();
            o.setIdVehiculo(idVehiculo);
            o.setDescripcion(desc);
            o.setEstado(Estado.RECIBIDO);
            o.setFechaIngreso(LocalDate.now());

            try {
                o.setFechaEstimada(LocalDate.parse(fechaStr, FMT));
            } catch (DateTimeParseException e) {
                System.out.println("  ✗ Formato de fecha inválido. Se omite fecha estimada.");
            }

            oDao.insertar(o);
            System.out.println("  ✓ Orden creada con ID " + o.getId() +
                               " — Estado: Recibido");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void avanzarEstado() {
        int id = Util.leerEntero(sc, "ID de la orden: ");
        try {
            OrdenTrabajo o = oDao.buscarPorId(id);
            if (o == null) { System.out.println("  ✗ Orden no encontrada."); return; }
            Estado siguiente = o.getEstado().siguiente();
            if (siguiente == null) {
                System.out.println("  La orden ya está en estado final (Entregado).");
                return;
            }
            System.out.printf("  Estado actual: %s → Nuevo estado: %s%n",
                o.getEstado(), siguiente);
            String conf = Util.leerTexto(sc, "¿Confirmar? (s/n): ");
            if (!conf.equalsIgnoreCase("s")) return;
            oDao.actualizarEstado(id, siguiente);
            System.out.println("  ✓ Estado actualizado a: " + siguiente);
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void eliminar() {
        int id = Util.leerEntero(sc, "ID de la orden a eliminar: ");
        String conf = Util.leerTexto(sc, "¿Confirmar? (s/n): ");
        if (!conf.equalsIgnoreCase("s")) return;
        try {
            oDao.eliminar(id);
            System.out.println("  ✓ Orden eliminada.");
        } catch (SQLException e) { Util.errorDB(e); }
    }
}
