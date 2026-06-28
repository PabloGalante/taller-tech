package com.tallertech.menu;

import com.tallertech.dao.ClienteDAO;
import com.tallertech.dao.VehiculoDAO;
import com.tallertech.model.Cliente;
import com.tallertech.model.Vehiculo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuVehiculos {

    private final VehiculoDAO vDao = new VehiculoDAO();
    private final ClienteDAO  cDao = new ClienteDAO();
    private final Scanner     sc;

    public MenuVehiculos(Scanner sc) { this.sc = sc; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n┌── GESTIÓN DE VEHÍCULOS ──────────────────┐");
            System.out.println("│  1. Listar todos                          │");
            System.out.println("│  2. Buscar por patente                    │");
            System.out.println("│  3. Listar por cliente                    │");
            System.out.println("│  4. Registrar nuevo vehículo              │");
            System.out.println("│  5. Eliminar vehículo                     │");
            System.out.println("│  0. Volver                                │");
            System.out.println("└───────────────────────────────────────────┘");

            switch (Util.leerEntero(sc, "Opción: ")) {
                case 1 -> listar();
                case 2 -> buscarPorPatente();
                case 3 -> listarPorCliente();
                case 4 -> registrar();
                case 5 -> eliminar();
                case 0 -> volver = true;
                default -> System.out.println("  ✗ Opción inválida.");
            }
        }
    }

    private void listar() {
        try {
            ArrayList<Vehiculo> lista = vDao.listarTodos();
            if (lista.isEmpty()) { System.out.println("  Sin vehículos."); return; }
            for (Vehiculo v : lista) System.out.println("  " + v);
            System.out.printf("  Total: %d vehículo(s)%n", lista.size());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void buscarPorPatente() {
        String patente = Util.leerTexto(sc, "Patente: ");
        try {
            Vehiculo v = vDao.buscarPorPatente(patente);
            if (v != null) System.out.println("  " + v);
            else System.out.println("  ✗ Vehículo no encontrado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void listarPorCliente() {
        int idCliente = Util.leerEntero(sc, "ID del cliente: ");
        try {
            ArrayList<Vehiculo> lista = vDao.listarPorCliente(idCliente);
            if (lista.isEmpty()) { System.out.println("  Sin vehículos para ese cliente."); return; }
            for (Vehiculo v : lista) System.out.println("  " + v);
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void registrar() {
        System.out.println("\n  — Nuevo vehículo —");
        try {
            // Mostrar clientes disponibles usando arreglo de IDs
            ArrayList<Cliente> clientes = cDao.listarTodos();
            if (clientes.isEmpty()) {
                System.out.println("  ✗ No hay clientes registrados. Registre uno primero.");
                return;
            }
            int[] ids = new int[clientes.size()];
            for (int i = 0; i < clientes.size(); i++) {
                ids[i] = clientes.get(i).getId();
                System.out.println("  " + clientes.get(i));
            }

            int idCliente = Util.leerEntero(sc, "ID del cliente propietario: ");
            boolean idValido = false;
            for (int id : ids) { if (id == idCliente) { idValido = true; break; } }
            if (!idValido) { System.out.println("  ✗ ID de cliente inválido."); return; }

            Vehiculo v = new Vehiculo();
            v.setIdCliente(idCliente);
            v.setPatente(Util.leerTexto(sc, "Patente: "));
            v.setMarca(Util.leerTexto(sc, "Marca: "));
            v.setModelo(Util.leerTexto(sc, "Modelo: "));
            v.setAnio(Util.leerEntero(sc, "Año: "));
            vDao.insertar(v);
            System.out.println("  ✓ Vehículo registrado con ID " + v.getId());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void eliminar() {
        int id = Util.leerEntero(sc, "ID del vehículo a eliminar: ");
        String conf = Util.leerTexto(sc, "¿Confirmar? (s/n): ");
        if (!conf.equalsIgnoreCase("s")) return;
        try {
            vDao.eliminar(id);
            System.out.println("  ✓ Vehículo eliminado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }
}
