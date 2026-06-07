package com.tallertech.menu;

import com.tallertech.model.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Menú principal del sistema TallerTech.
 * Demuestra: estructuras condicionales, repetitivas,
 * manejo de excepciones, creación de objetos y constructores.
 */
public class MenuPrincipal {

    private final Scanner        scanner;
    private final GestorServicios gestor;
    private int                  proximoId;

    public MenuPrincipal() {
        this.scanner   = new Scanner(System.in);
        this.gestor    = new GestorServicios();
        this.proximoId = 1;
        cargarDatosEjemplo();
    }

    // ── Datos de ejemplo para demostrar polimorfismo ─────────────────────────
    private void cargarDatosEjemplo() {
        gestor.agregar(new ServicioMantenimiento(
            proximoId++, "Cambio de aceite y filtro", 2500, "Preventivo", 1800));
        gestor.agregar(new ServicioReparacion(
            proximoId++, "Reparación de frenos delanteros", 3000, "Pastillas", 2));
        gestor.agregar(new ServicioMantenimiento(
            proximoId++, "Alineación y balanceo", 1800, "Correctivo", 500));
        gestor.agregar(new ServicioReparacion(
            proximoId++, "Cambio de correa de distribución", 4000, "Correa", 3));
    }

    // ── Bucle principal ───────────────────────────────────────────────────────
    public void iniciar() {
        boolean salir = false;

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   TallerTech S.R.L. — Sistema de Gestión ║");
        System.out.println("╚══════════════════════════════════════════╝");

        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");

            // Estructura condicional switch
            switch (opcion) {
                case 1  -> listarServicios();
                case 2  -> agregarServicioMantenimiento();
                case 3  -> agregarServicioReparacion();
                case 4  -> buscarServicio();
                case 5  -> eliminarServicio();
                case 6  -> listarOrdenadoPorCosto();
                case 7  -> mostrarCostoTotal();
                case 0  -> salir = confirmarSalida();
                default -> System.out.println("  ✗ Opción inválida. Ingrese un número del 0 al 7.\n");
            }
        }

        System.out.println("\nSistema cerrado. ¡Hasta luego!");
        scanner.close();
    }

    // ── Opciones del menú ─────────────────────────────────────────────────────

    private void listarServicios() {
        System.out.println("\n── Servicios registrados ──────────────────");
        List<Servicio> lista = gestor.getServicios();

        if (lista.isEmpty()) {
            System.out.println("  No hay servicios registrados.");
        } else {
            // Estructura repetitiva
            for (Servicio s : lista) {
                System.out.println("  " + s.getResumen());  // POLIMORFISMO
            }
            System.out.printf("  Total de servicios: %d%n", lista.size());
        }
        System.out.println();
    }

    private void agregarServicioMantenimiento() {
        System.out.println("\n── Nuevo servicio de mantenimiento ────────");
        try {
            String descripcion = leerTexto("Descripción: ");
            double precioBase  = leerDouble("Precio base (mano de obra $): ");
            String tipo        = leerTexto("Tipo de mantenimiento: ");
            double insumos     = leerDouble("Costo de insumos $: ");

            // Creación de objeto con constructor
            ServicioMantenimiento sm = new ServicioMantenimiento(
                proximoId++, descripcion, precioBase, tipo, insumos);
            gestor.agregar(sm);

            System.out.println("  ✓ Servicio registrado: " + sm.getResumen());
        } catch (Exception e) {
            System.out.println("  ✗ Error al registrar servicio: " + e.getMessage());
        }
        System.out.println();
    }

    private void agregarServicioReparacion() {
        System.out.println("\n── Nuevo servicio de reparación ────────────");
        try {
            String descripcion  = leerTexto("Descripción: ");
            double precioBase   = leerDouble("Precio base $: ");
            String pieza        = leerTexto("Pieza a reparar: ");
            int    complejidad  = leerEntero("Nivel de complejidad (1=simple, 2=media, 3=alta): ");

            // Creación de objeto con constructor
            ServicioReparacion sr = new ServicioReparacion(
                proximoId++, descripcion, precioBase, pieza, complejidad);
            gestor.agregar(sr);

            System.out.println("  ✓ Servicio registrado: " + sr.getResumen());
        } catch (Exception e) {
            System.out.println("  ✗ Error al registrar servicio: " + e.getMessage());
        }
        System.out.println();
    }

    private void buscarServicio() {
        System.out.println("\n── Buscar servicio ────────────────────────");
        System.out.println("  1. Buscar por ID");
        System.out.println("  2. Buscar por descripción");
        int opcion = leerEntero("Opción: ");

        if (opcion == 1) {
            int id = leerEntero("ID del servicio: ");
            Servicio s = gestor.buscarPorId(id);
            if (s != null) {
                System.out.println("  Encontrado: " + s.getResumen());
            } else {
                System.out.println("  ✗ No se encontró servicio con ID " + id);
            }
        } else if (opcion == 2) {
            String texto = leerTexto("Texto a buscar: ");
            List<Servicio> resultados = gestor.buscarPorDescripcion(texto);
            if (resultados.isEmpty()) {
                System.out.println("  ✗ Sin resultados para \"" + texto + "\"");
            } else {
                System.out.printf("  %d resultado(s):%n", resultados.size());
                for (Servicio s : resultados) {
                    System.out.println("    " + s.getResumen());
                }
            }
        } else {
            System.out.println("  ✗ Opción inválida.");
        }
        System.out.println();
    }

    private void eliminarServicio() {
        System.out.println("\n── Eliminar servicio ───────────────────────");
        int id = leerEntero("ID del servicio a eliminar: ");
        boolean eliminado = gestor.eliminar(id);
        if (eliminado) {
            System.out.println("  ✓ Servicio eliminado correctamente.");
        } else {
            System.out.println("  ✗ No se encontró servicio con ID " + id);
        }
        System.out.println();
    }

    private void listarOrdenadoPorCosto() {
        System.out.println("\n── Servicios ordenados por costo (ascendente) ──");
        List<Servicio> ordenados = gestor.ordenarPorCosto();
        if (ordenados.isEmpty()) {
            System.out.println("  No hay servicios registrados.");
        } else {
            for (int i = 0; i < ordenados.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, ordenados.get(i).getResumen());
            }
        }
        System.out.println();
    }

    private void mostrarCostoTotal() {
        System.out.println("\n── Costo total de servicios ────────────────");
        System.out.printf("  Servicios registrados: %d%n", gestor.getCantidad());
        System.out.printf("  Costo total acumulado: $%.2f%n", gestor.calcularCostoTotal());
        System.out.println();
    }

    private boolean confirmarSalida() {
        String resp = leerTexto("¿Confirmar salida? (s/n): ");
        return resp.equalsIgnoreCase("s");
    }

    // ── Menú ─────────────────────────────────────────────────────────────────
    private void mostrarMenu() {
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│  MENÚ PRINCIPAL                          │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1. Listar servicios                     │");
        System.out.println("│  2. Agregar servicio de mantenimiento    │");
        System.out.println("│  3. Agregar servicio de reparación       │");
        System.out.println("│  4. Buscar servicio                      │");
        System.out.println("│  5. Eliminar servicio                    │");
        System.out.println("│  6. Listar ordenado por costo            │");
        System.out.println("│  7. Ver costo total                      │");
        System.out.println("│  0. Salir                                │");
        System.out.println("└──────────────────────────────────────────┘");
    }

    // ── Helpers de entrada con manejo de excepciones ─────────────────────────

    private int leerEntero(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = scanner.nextInt();
                scanner.nextLine();  // limpiar buffer
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();  // limpiar entrada inválida
                System.out.println("  ✗ Ingrese un número entero válido.");
            }
        }
    }

    private double leerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double valor = scanner.nextDouble();
                scanner.nextLine();
                if (valor < 0) throw new IllegalArgumentException("El valor no puede ser negativo.");
                return valor;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("  ✗ Ingrese un número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }
    }

    private String leerTexto(String prompt) {
        while (true) {
            System.out.print(prompt);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("  ✗ El campo no puede estar vacío.");
        }
    }
}
