package com.tallertech.model;

/**
 * HERENCIA: extiende Servicio con atributos propios de reparación.
 *
 * POLIMORFISMO: implementa calcularCosto() con lógica propia:
 * aplica un factor de complejidad sobre el precio base.
 * Complejidad 1 = normal, 2 = compleja, 3 = muy compleja.
 */
public class ServicioReparacion extends Servicio {

    private String pieza;
    private int    nivelComplejidad;  // 1, 2 o 3

    public ServicioReparacion(int id, String descripcion,
                               double precioBase, String pieza,
                               int nivelComplejidad) {
        super(id, descripcion, precioBase);
        this.pieza            = pieza;
        this.nivelComplejidad = validarComplejidad(nivelComplejidad);
    }

    // Getters y setters (ENCAPSULAMIENTO)
    public String getPieza()                   { return pieza; }
    public void   setPieza(String p)           { this.pieza = p; }

    public int    getNivelComplejidad()        { return nivelComplejidad; }
    public void   setNivelComplejidad(int n)   { this.nivelComplejidad = validarComplejidad(n); }

    /**
     * Valida que el nivel esté entre 1 y 3. Si no, retorna 1.
     * Ejemplo de estructura condicional y manejo de valores inválidos.
     */
    private int validarComplejidad(int nivel) {
        if (nivel < 1) return 1;
        if (nivel > 3) return 3;
        return nivel;
    }

    /**
     * POLIMORFISMO: costo = precio base * factor de complejidad.
     */
    @Override
    public double calcularCosto() {
        return getPrecioBase() * nivelComplejidad;
    }

    @Override
    public String getResumen() {
        return String.format("[Reparación] %s | Pieza: %s | Complejidad: %d | Total: $%.2f",
            getDescripcion(), pieza, nivelComplejidad, calcularCosto());
    }
}
