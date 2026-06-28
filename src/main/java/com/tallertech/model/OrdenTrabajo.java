package com.tallertech.model;

import java.time.LocalDate;

public class OrdenTrabajo {

    public enum Estado {
        RECIBIDO, EN_REPARACION, LISTO, ENTREGADO;

        public Estado siguiente() {
            return switch (this) {
                case RECIBIDO      -> EN_REPARACION;
                case EN_REPARACION -> LISTO;
                case LISTO         -> ENTREGADO;
                case ENTREGADO     -> null;
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case RECIBIDO      -> "Recibido";
                case EN_REPARACION -> "En reparación";
                case LISTO         -> "Listo";
                case ENTREGADO     -> "Entregado";
            };
        }
    }

    private int       id;
    private int       idVehiculo;
    private String    descripcion;
    private Estado    estado;
    private LocalDate fechaIngreso;
    private LocalDate fechaEstimada;
    private LocalDate fechaEntrega;
    private String    patenteVehiculo;
    private String    nombreCliente;

    public OrdenTrabajo() {}

    public int       getId()                       { return id; }
    public void      setId(int id)                 { this.id = id; }
    public int       getIdVehiculo()               { return idVehiculo; }
    public void      setIdVehiculo(int iv)         { this.idVehiculo = iv; }
    public String    getDescripcion()              { return descripcion; }
    public void      setDescripcion(String d)      { this.descripcion = d; }
    public Estado    getEstado()                   { return estado; }
    public void      setEstado(Estado e)           { this.estado = e; }
    public LocalDate getFechaIngreso()             { return fechaIngreso; }
    public void      setFechaIngreso(LocalDate f)  { this.fechaIngreso = f; }
    public LocalDate getFechaEstimada()            { return fechaEstimada; }
    public void      setFechaEstimada(LocalDate f) { this.fechaEstimada = f; }
    public LocalDate getFechaEntrega()             { return fechaEntrega; }
    public void      setFechaEntrega(LocalDate f)  { this.fechaEntrega = f; }
    public String    getPatenteVehiculo()          { return patenteVehiculo; }
    public void      setPatenteVehiculo(String p)  { this.patenteVehiculo = p; }
    public String    getNombreCliente()            { return nombreCliente; }
    public void      setNombreCliente(String n)    { this.nombreCliente = n; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | Estado: %s | Ingreso: %s",
            id, patenteVehiculo != null ? patenteVehiculo : "?",
            descripcion, estado, fechaIngreso);
    }
}
