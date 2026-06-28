package com.tallertech.menu;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Métodos utilitarios de lectura con manejo de excepciones centralizado.
 * Evita duplicación de código try/catch en todos los menús.
 */
public class Util {

    public static int leerEntero(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                int val = sc.nextInt();
                sc.nextLine();
                return val;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("  ✗ Ingrese un número entero válido.");
            }
        }
    }

    public static double leerDouble(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                double val = sc.nextDouble();
                sc.nextLine();
                if (val < 0) throw new IllegalArgumentException("No puede ser negativo.");
                return val;
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.println("  ✗ Ingrese un número válido.");
            } catch (IllegalArgumentException e) {
                System.out.println("  ✗ " + e.getMessage());
            }
        }
    }

    public static String leerTexto(Scanner sc, String prompt) {
        while (true) {
            System.out.print("  " + prompt);
            String val = sc.nextLine().trim();
            if (!val.isEmpty()) return val;
            System.out.println("  ✗ El campo no puede estar vacío.");
        }
    }

    public static void errorDB(SQLException e) {
        System.out.println("  ✗ Error de base de datos: " + e.getMessage());
    }
}
