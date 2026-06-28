package com.tallertech.model;

/**
 * HERENCIA: extiende Servicio con atributos propios de mantenimiento.
 * POLIMORFISMO: implementa calcularCosto() = precioBase + costoInsumos.
 */
public class ServicioMantenimiento extends Servicio {

    private String tipoMantenimiento;
    private double costoInsumos;

    public ServicioMantenimiento(int id, String descripcion,
                                  double precioBase,
                                  String tipoMantenimiento,
                                  double costoInsumos) {
        super(id, descripcion, precioBase);
        this.tipoMantenimiento = tipoMantenimiento;
        this.costoInsumos      = costoInsumos;
    }

    public String getTipoMantenimiento()         { return tipoMantenimiento; }
    public void   setTipoMantenimiento(String t) { this.tipoMantenimiento = t; }
    public double getCostoInsumos()              { return costoInsumos; }
    public void   setCostoInsumos(double c)      { this.costoInsumos = c; }

    @Override
    public double calcularCosto() { return getPrecioBase() + costoInsumos; }

    @Override
    public String getTipo() { return "Mantenimiento (" + tipoMantenimiento + ")"; }
}
