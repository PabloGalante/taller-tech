package com.tallertech;

import com.tallertech.menu.MenuPrincipal;

/**
 * Punto de entrada del prototipo TallerTech — TP3.
 * Demuestra la aplicación de los pilares de POO en Java:
 *   - Encapsulamiento: atributos privados + getters/setters
 *   - Abstracción:     clase abstracta Servicio
 *   - Herencia:        ServicioMantenimiento y ServicioReparacion
 *   - Polimorfismo:    calcularCosto() resuelto en runtime
 */
public class Main {
    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.iniciar();
    }
}
