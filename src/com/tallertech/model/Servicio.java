package com.tallertech.model;

/**
 * ABSTRACCIÓN: clase abstracta que representa cualquier servicio
 * que el taller puede ofrecer. Define la interfaz común para todas
 * las subclases sin implementar calcularCosto().
 *
 * ENCAPSULAMIENTO: todos los atributos son privados y se acceden
 * únicamente a través de getters y setters.
 */
public abstract class Servicio {

    private int    id;
    private String descripcion;
    private double precioBase;

    // Constructor
    public Servicio(int id, String descripcion, double precioBase) {
        this.id          = id;
        this.descripcion = descripcion;
        this.precioBase  = precioBase;
    }

    // Getters y setters (ENCAPSULAMIENTO)
    public int    getId()                   { return id; }
    public void   setId(int id)             { this.id = id; }

    public String getDescripcion()          { return descripcion; }
    public void   setDescripcion(String d)  { this.descripcion = d; }

    public double getPrecioBase()           { return precioBase; }
    public void   setPrecioBase(double p)   { this.precioBase = p; }

    /**
     * ABSTRACCIÓN: método abstracto que cada subclase debe implementar
     * con su propia lógica de cálculo de costo.
     * POLIMORFISMO: permite tratar distintos tipos de servicio de forma
     * uniforme llamando a calcularCosto() sin conocer el tipo concreto.
     */
    public abstract double calcularCosto();

    /**
     * Método concreto compartido por todas las subclases.
     */
    public String getResumen() {
        return String.format("[%d] %s — Costo: $%.2f", id, descripcion, calcularCosto());
    }

    @Override
    public String toString() {
        return getResumen();
    }
}
