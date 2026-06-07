package com.tallertech.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona una colección de servicios registrados en el sistema.
 * Aplica algoritmos de búsqueda lineal y ordenamiento burbuja.
 * Usa POLIMORFISMO: opera sobre Servicio sin conocer el tipo concreto.
 */
public class GestorServicios {

    private List<Servicio> servicios;

    public GestorServicios() {
        this.servicios = new ArrayList<>();
    }

    public void agregar(Servicio s) {
        servicios.add(s);
    }

    public boolean eliminar(int id) {
        // Estructura repetitiva + condicional
        for (int i = 0; i < servicios.size(); i++) {
            if (servicios.get(i).getId() == id) {
                servicios.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Búsqueda lineal por ID.
     * Retorna null si no encuentra el servicio.
     */
    public Servicio buscarPorId(int id) {
        for (Servicio s : servicios) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    /**
     * Búsqueda por texto en descripción (case-insensitive).
     */
    public List<Servicio> buscarPorDescripcion(String texto) {
        List<Servicio> resultados = new ArrayList<>();
        for (Servicio s : servicios) {
            if (s.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                resultados.add(s);
            }
        }
        return resultados;
    }

    /**
     * Ordenamiento burbuja por costo ascendente.
     * POLIMORFISMO: llama calcularCosto() que se resuelve en runtime
     * según el tipo concreto de cada Servicio.
     */
    public List<Servicio> ordenarPorCosto() {
        List<Servicio> copia = new ArrayList<>(servicios);
        int n = copia.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (copia.get(j).calcularCosto() > copia.get(j + 1).calcularCosto()) {
                    Servicio temp = copia.get(j);
                    copia.set(j, copia.get(j + 1));
                    copia.set(j + 1, temp);
                }
            }
        }
        return copia;
    }

    public List<Servicio> getServicios() {
        return new ArrayList<>(servicios);
    }

    public int getCantidad() {
        return servicios.size();
    }

    public double calcularCostoTotal() {
        double total = 0;
        for (Servicio s : servicios) {
            total += s.calcularCosto();  // POLIMORFISMO en acción
        }
        return total;
    }
}
