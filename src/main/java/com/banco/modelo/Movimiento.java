//Clase Movimiento para Geters y Seters, obteniendo datos de movimientos realizados

package com.banco.modelo;

import java.time.LocalDateTime;

public class Movimiento {
    private int idm;
    private int idt;
    private double cantidad;
    private String tipo; // CREDITO o DEBITO
    private String descripcion;
    private LocalDateTime fechaMovimiento;

    public Movimiento() {
    }

    public Movimiento(int idt, double cantidad, String tipo, String descripcion) {
        this.idt = idt;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaMovimiento = LocalDateTime.now();
    }

    public int getIdm() {
        return idm;
    }

    public void setIdm(int idm) {
        this.idm = idm;
    }

    public int getIdt() {
        return idt;
    }

    public void setIdt(int idt) {
        this.idt = idt;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    @Override
    public String toString() {
        return "[" + fechaMovimiento + "] " + tipo + ": $" + cantidad + " - " + descripcion;
    }
}
