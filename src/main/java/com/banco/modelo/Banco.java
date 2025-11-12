package com.banco.modelo;

public class Banco {
    private int idb;
    private String nombre;

    public Banco() {
    }

    public Banco(String nombre) {
        this.nombre = nombre;
    }

    public int getIdb() {
        return idb;
    }

    public void setIdb(int idb) {
        this.idb = idb;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; // Mostrar solo el nombre en ComboBox
    }
}
