package com.banco.modelo;

public class Cliente {
    private int idc;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre;

    public Cliente() {
    }

    public Cliente(String apellidoPaterno, String apellidoMaterno, String nombre) {
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombre = nombre;
    }

    public int getIdc() {
        return idc;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idc=" + idc +
                ", nombre='" + getNombreCompleto() + '\'' +
                '}';
    }
}
