package com.tallertech.model;

public class Vehiculo {
    private int    id;
    private int    idCliente;
    private String marca;
    private String modelo;
    private int    anio;
    private String patente;
    private String nombreCliente;

    public Vehiculo() {}

    public Vehiculo(int id, int idCliente, String marca,
                    String modelo, int anio, String patente) {
        this.id        = id;
        this.idCliente = idCliente;
        this.marca     = marca;
        this.modelo    = modelo;
        this.anio      = anio;
        this.patente   = patente;
    }

    public int    getId()                      { return id; }
    public void   setId(int id)                { this.id = id; }
    public int    getIdCliente()               { return idCliente; }
    public void   setIdCliente(int ic)         { this.idCliente = ic; }
    public String getMarca()                   { return marca; }
    public void   setMarca(String m)           { this.marca = m; }
    public String getModelo()                  { return modelo; }
    public void   setModelo(String m)          { this.modelo = m; }
    public int    getAnio()                    { return anio; }
    public void   setAnio(int a)               { this.anio = a; }
    public String getPatente()                 { return patente; }
    public void   setPatente(String p)         { this.patente = p; }
    public String getNombreCliente()           { return nombreCliente; }
    public void   setNombreCliente(String n)   { this.nombreCliente = n; }

    @Override
    public String toString() {
        return String.format("[%d] %s — %s %s (%d) | Cliente: %s",
            id, patente, marca, modelo, anio,
            nombreCliente != null ? nombreCliente : "N/A");
    }
}
