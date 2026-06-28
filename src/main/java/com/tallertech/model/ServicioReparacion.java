package com.tallertech.model;

/**
 * HERENCIA: extiende Servicio con atributos propios de reparación.
 * POLIMORFISMO: calcularCosto() = precioBase * nivelComplejidad.
 */
public class ServicioReparacion extends Servicio {

    private String pieza;
    private int    nivelComplejidad; // 1, 2 o 3

    public ServicioReparacion(int id, String descripcion,
                               double precioBase,
                               String pieza,
                               int nivelComplejidad) {
        super(id, descripcion, precioBase);
        this.pieza            = pieza;
        this.nivelComplejidad = validar(nivelComplejidad);
    }

    public String getPieza()                 { return pieza; }
    public void   setPieza(String p)         { this.pieza = p; }
    public int    getNivelComplejidad()      { return nivelComplejidad; }
    public void   setNivelComplejidad(int n) { this.nivelComplejidad = validar(n); }

    private int validar(int n) {
        if (n < 1) return 1;
        if (n > 3) return 3;
        return n;
    }

    @Override
    public double calcularCosto() { return getPrecioBase() * nivelComplejidad; }

    @Override
    public String getTipo() { return "Reparación - " + pieza + " (complejidad " + nivelComplejidad + ")"; }
}
