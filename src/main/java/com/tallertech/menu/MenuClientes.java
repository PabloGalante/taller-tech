package com.tallertech.menu;

import com.tallertech.dao.ClienteDAO;
import com.tallertech.model.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuClientes {

    private final ClienteDAO dao = new ClienteDAO();
    private final Scanner    sc;

    public MenuClientes(Scanner sc) { this.sc = sc; }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n┌── GESTIÓN DE CLIENTES ───────────────────┐");
            System.out.println("│  1. Listar todos                          │");
            System.out.println("│  2. Buscar por ID                         │");
            System.out.println("│  3. Registrar nuevo cliente               │");
            System.out.println("│  4. Actualizar cliente                    │");
            System.out.println("│  5. Eliminar cliente                      │");
            System.out.println("│  0. Volver al menú principal              │");
            System.out.println("└───────────────────────────────────────────┘");

            switch (Util.leerEntero(sc, "Opción: ")) {
                case 1 -> listar();
                case 2 -> buscar();
                case 3 -> registrar();
                case 4 -> actualizar();
                case 5 -> eliminar();
                case 0 -> volver = true;
                default -> System.out.println("  ✗ Opción inválida.");
            }
        }
    }

    private void listar() {
        try {
            ArrayList<Cliente> lista = dao.listarTodos();
            if (lista.isEmpty()) {
                System.out.println("  No hay clientes registrados.");
                return;
            }
            // Uso de arreglo para mostrar encabezados de columna
            String[] headers = {"ID", "Apellido", "Nombre", "Teléfono", "Email"};
            System.out.printf("  %-4s %-15s %-15s %-15s %-25s%n",
                (Object[]) headers);
            System.out.println("  " + "-".repeat(74));
            // Uso de ArrayList para iterar resultados
            for (Cliente c : lista) {
                System.out.printf("  %-4d %-15s %-15s %-15s %-25s%n",
                    c.getId(), c.getApellido(), c.getNombre(),
                    c.getTelefono(), c.getEmail());
            }
            System.out.printf("  Total: %d cliente(s)%n", lista.size());
        } catch (SQLException e) {
            Util.errorDB(e);
        }
    }

    private void buscar() {
        int id = Util.leerEntero(sc, "ID del cliente: ");
        try {
            Cliente c = dao.buscarPorId(id);
            if (c != null) System.out.println("  " + c);
            else System.out.println("  ✗ No encontrado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void registrar() {
        System.out.println("\n  — Nuevo cliente —");
        Cliente c = new Cliente();
        c.setNombre(Util.leerTexto(sc, "Nombre: "));
        c.setApellido(Util.leerTexto(sc, "Apellido: "));
        c.setTelefono(Util.leerTexto(sc, "Teléfono: "));
        c.setEmail(Util.leerTexto(sc, "Email: "));
        try {
            dao.insertar(c);
            System.out.println("  ✓ Cliente registrado con ID " + c.getId());
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void actualizar() {
        int id = Util.leerEntero(sc, "ID del cliente a actualizar: ");
        try {
            Cliente c = dao.buscarPorId(id);
            if (c == null) { System.out.println("  ✗ No encontrado."); return; }
            System.out.println("  Datos actuales: " + c);
            c.setNombre(Util.leerTexto(sc, "Nuevo nombre (" + c.getNombre() + "): "));
            c.setApellido(Util.leerTexto(sc, "Nuevo apellido (" + c.getApellido() + "): "));
            c.setTelefono(Util.leerTexto(sc, "Nuevo teléfono (" + c.getTelefono() + "): "));
            c.setEmail(Util.leerTexto(sc, "Nuevo email (" + c.getEmail() + "): "));
            dao.actualizar(c);
            System.out.println("  ✓ Cliente actualizado correctamente.");
        } catch (SQLException e) { Util.errorDB(e); }
    }

    private void eliminar() {
        int id = Util.leerEntero(sc, "ID del cliente a eliminar: ");
        String conf = Util.leerTexto(sc, "¿Confirmar eliminación? (s/n): ");
        if (!conf.equalsIgnoreCase("s")) return;
        try {
            dao.eliminar(id);
            System.out.println("  ✓ Cliente eliminado.");
        } catch (SQLException e) { Util.errorDB(e); }
    }
}
