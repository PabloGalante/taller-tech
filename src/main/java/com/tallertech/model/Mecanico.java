package com.tallertech.model;

public class Mecanico {
    private int    id;
    private String nombre;
    private String apellido;
    private String especialidad;

    public Mecanico() {}

    public Mecanico(int id, String nombre, String apellido, String especialidad) {
        this.id           = id;
        this.nombre       = nombre;
        this.apellido     = apellido;
        this.especialidad = especialidad;
    }

    public int    getId()                      { return id; }
    public void   setId(int id)                { this.id = id; }

    public String getNombre()                  { return nombre; }
    public void   setNombre(String n)          { this.nombre = n; }

    public String getApellido()                { return apellido; }
    public void   setApellido(String a)        { this.apellido = a; }

    public String getEspecialidad()            { return especialidad; }
    public void   setEspecialidad(String e)    { this.especialidad = e; }

    @Override
    public String toString() {
        return apellido + ", " + nombre;
    }
}
