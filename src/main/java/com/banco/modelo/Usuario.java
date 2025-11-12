package com.banco.modelo;

import java.time.LocalDateTime;

public class Usuario {
    private int idu;
    private String usuario;
    private String contraseña;
    private Cliente cliente;
    private boolean esAdmin;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    public Usuario() {
    }

    public Usuario(String usuario, String contraseña, Cliente cliente, boolean esAdmin) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.cliente = cliente;
        this.esAdmin = esAdmin;
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public int getIdu() {
        return idu;
    }

    public void setIdu(int idu) {
        this.idu = idu;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idu=" + idu +
                ", usuario='" + usuario + '\'' +
                ", esAdmin=" + esAdmin +
                ", activo=" + activo +
                '}';
    }
}
