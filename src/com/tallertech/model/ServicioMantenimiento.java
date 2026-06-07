package com.tallertech.model;

/**
 * HERENCIA: extiende Servicio heredando id, descripcion y precioBase.
 * Agrega atributos propios del mantenimiento preventivo.
 *
 * POLIMORFISMO: implementa calcularCosto() con lógica propia:
 * el costo incluye el precio base más el costo de los insumos.
 */
public class ServicioMantenimiento extends Servicio {

    private String tipoMantenimiento;  // ej: "Cambio de aceite", "Alineación"
    private double costoInsumos;

    public ServicioMantenimiento(int id, String descripcion,
                                  double precioBase, String tipoMantenimiento,
                                  double costoInsumos) {
        super(id, descripcion, precioBase);
        this.tipoMantenimiento = tipoMantenimiento;
        this.costoInsumos      = costoInsumos;
    }

    // Getters y setters propios (ENCAPSULAMIENTO)
    public String getTipoMantenimiento()           { return tipoMantenimiento; }
    public void   setTipoMantenimiento(String t)   { this.tipoMantenimiento = t; }

    public double getCostoInsumos()                { return costoInsumos; }
    public void   setCostoInsumos(double c)        { this.costoInsumos = c; }

    /**
     * POLIMORFISMO: costo = precio base (mano de obra) + insumos.
     */
    @Override
    public double calcularCosto() {
        return getPrecioBase() + costoInsumos;
    }

    @Override
    public String getResumen() {
        return String.format("[Mantenimiento] %s | Tipo: %s | Insumos: $%.2f | Total: $%.2f",
            getDescripcion(), tipoMantenimiento, costoInsumos, calcularCosto());
    }
}
