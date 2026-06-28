package com.tallertech.menu;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner        sc  = new Scanner(System.in);
    private final MenuClientes   mc  = new MenuClientes(sc);
    private final MenuVehiculos  mv  = new MenuVehiculos(sc);
    private final MenuOrdenes    mo  = new MenuOrdenes(sc);
    private final MenuServicios  ms  = new MenuServicios(sc);

    public void iniciar() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   TallerTech S.R.L. — Sistema de Gestión v2  ║");
        System.out.println("║   Conectado a MySQL · Patrón Singleton + DAO  ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n┌── MENÚ PRINCIPAL ─────────────────────────┐");
            System.out.println("│  1. Gestión de clientes                   │");
            System.out.println("│  2. Gestión de vehículos                  │");
            System.out.println("│  3. Gestión de órdenes de trabajo         │");
            System.out.println("│  4. Gestión de servicios                  │");
            System.out.println("│  0. Salir                                 │");
            System.out.println("└───────────────────────────────────────────┘");

            switch (Util.leerEntero(sc, "Seleccione una opción: ")) {
                case 1 -> mc.mostrar();
                case 2 -> mv.mostrar();
                case 3 -> mo.mostrar();
                case 4 -> ms.mostrar();
                case 0 -> {
                    String conf = Util.leerTexto(sc, "¿Confirmar salida? (s/n): ");
                    if (conf.equalsIgnoreCase("s")) salir = true;
                }
                default -> System.out.println("  ✗ Opción inválida.");
            }
        }

        System.out.println("\nSistema cerrado. ¡Hasta luego!");
        sc.close();
    }
}
