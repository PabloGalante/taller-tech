package com.tallertech.model;

/**
 * ABSTRACCIÓN: clase abstracta base para todos los servicios del taller.
 * ENCAPSULAMIENTO: atributos privados con getters/setters.
 * POLIMORFISMO: calcularCosto() resuelto en runtime por cada subclase.
 */
public abstract class Servicio {

    private int    id;
    private String descripcion;
    private double precioBase;

    public Servicio(int id, String descripcion, double precioBase) {
        this.id          = id;
        this.descripcion = descripcion;
        this.precioBase  = precioBase;
    }

    public int    getId()                  { return id; }
    public void   setId(int id)            { this.id = id; }
    public String getDescripcion()         { return descripcion; }
    public void   setDescripcion(String d) { this.descripcion = d; }
    public double getPrecioBase()          { return precioBase; }
    public void   setPrecioBase(double p)  { this.precioBase = p; }

    /** Método abstracto — cada subclase define su lógica de costo */
    public abstract double calcularCosto();

    /** Método abstracto — cada subclase retorna su tipo como String */
    public abstract String getTipo();

    public String getResumen() {
        return String.format("[%d] %-40s | %s | Total: $%.2f",
            id, descripcion, getTipo(), calcularCosto());
    }

    @Override
    public String toString() { return getResumen(); }
}
