package com.banco.modelo;

import java.time.LocalDateTime;

public class Tarjeta {
    private int idt;
    private int idc;
    private int idb;
    private String numeroTarjeta;
    private double saldo;
    private LocalDateTime fechaCreacion;
    private boolean activa;

    public Tarjeta() {
    }

    public Tarjeta(int idc, int idb, String numeroTarjeta) {
        this.idc = idc;
        this.idb = idb;
        this.numeroTarjeta = numeroTarjeta;
        this.saldo = 0;
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public int getIdc() {
        return idc;
    }

    public void setIdc(int idc) {
        this.idc = idc;
    }

    public int getIdb() {
        return idb;
    }

    public void setIdb(int idb) {
        this.idb = idb;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        String ultimosCuatro = numeroTarjeta.length() >= 4 ? 
            numeroTarjeta.substring(numeroTarjeta.length() - 4) : numeroTarjeta;
        return "**** **** **** " + ultimosCuatro + " - $" + String.format("%.2f", saldo);
    }
}
