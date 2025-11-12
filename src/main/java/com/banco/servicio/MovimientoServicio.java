package com.banco.servicio;

import com.banco.dao.MovimientoDAO;
import com.banco.dao.TarjetaDAO;
import com.banco.modelo.Movimiento;
import com.banco.modelo.Tarjeta;
import java.util.List;

public class MovimientoServicio {
    private MovimientoDAO movimientoDAO;
    private TarjetaDAO tarjetaDAO;

    public MovimientoServicio() {
        this.movimientoDAO = new MovimientoDAO();
        this.tarjetaDAO = new TarjetaDAO();
    }

    public boolean realizarAbono(int idt, double cantidad, String descripcion) {
        Tarjeta tarjeta = tarjetaDAO.obtenerPorId(idt);
        if (tarjeta == null || !tarjeta.isActiva()) {
            return false;
        }

        tarjeta.setSaldo(tarjeta.getSaldo() + cantidad);
        tarjetaDAO.actualizar(tarjeta);

        Movimiento movimiento = new Movimiento(idt, cantidad, "CREDITO", descripcion);
        return movimientoDAO.crear(movimiento) > 0;
    }

    public boolean realizarRetiro(int idt, double cantidad, String descripcion) {
        Tarjeta tarjeta = tarjetaDAO.obtenerPorId(idt);
        if (tarjeta == null || !tarjeta.isActiva() || tarjeta.getSaldo() < cantidad) {
            return false;
        }

        tarjeta.setSaldo(tarjeta.getSaldo() - cantidad);
        tarjetaDAO.actualizar(tarjeta);

        Movimiento movimiento = new Movimiento(idt, cantidad, "DEBITO", descripcion);
        return movimientoDAO.crear(movimiento) > 0;
    }

    public boolean realizarTransferencia(int idtOrigen, int idtDestino, double cantidad) {
        Tarjeta tarjetaOrigen = tarjetaDAO.obtenerPorId(idtOrigen);
        Tarjeta tarjetaDestino = tarjetaDAO.obtenerPorId(idtDestino);

        if (tarjetaOrigen == null || tarjetaDestino == null || 
            !tarjetaOrigen.isActiva() || !tarjetaDestino.isActiva() ||
            tarjetaOrigen.getSaldo() < cantidad) {
            return false;
        }

        tarjetaOrigen.setSaldo(tarjetaOrigen.getSaldo() - cantidad);
        tarjetaDestino.setSaldo(tarjetaDestino.getSaldo() + cantidad);

        tarjetaDAO.actualizar(tarjetaOrigen);
        tarjetaDAO.actualizar(tarjetaDestino);

        Movimiento movOrigen = new Movimiento(idtOrigen, cantidad, "DEBITO", 
            "Transferencia a tarjeta " + tarjetaDestino.getNumeroTarjeta());
        Movimiento movDestino = new Movimiento(idtDestino, cantidad, "CREDITO", 
            "Transferencia de tarjeta " + tarjetaOrigen.getNumeroTarjeta());

        movimientoDAO.crear(movOrigen);
        movimientoDAO.crear(movDestino);

        return true;
    }

    public boolean realizarTransferenciaInterbancaria(int idtOrigen, int idtDestino, double cantidad) {
        double comision = cantidad * 0.01; // 1% de comisión
        double cantidadTotal = cantidad + comision;

        Tarjeta tarjetaOrigen = tarjetaDAO.obtenerPorId(idtOrigen);
        Tarjeta tarjetaDestino = tarjetaDAO.obtenerPorId(idtDestino);

        if (tarjetaOrigen == null || tarjetaDestino == null || 
            !tarjetaOrigen.isActiva() || !tarjetaDestino.isActiva() ||
            tarjetaOrigen.getSaldo() < cantidadTotal ||
            tarjetaOrigen.getIdb() == tarjetaDestino.getIdb()) {
            return false;
        }

        tarjetaOrigen.setSaldo(tarjetaOrigen.getSaldo() - cantidadTotal);
        tarjetaDestino.setSaldo(tarjetaDestino.getSaldo() + cantidad);

        tarjetaDAO.actualizar(tarjetaOrigen);
        tarjetaDAO.actualizar(tarjetaDestino);

        Movimiento movOrigen = new Movimiento(idtOrigen, cantidadTotal, "DEBITO", 
            "Transferencia interbancaria (comisión: $" + comision + ")");
        Movimiento movDestino = new Movimiento(idtDestino, cantidad, "CREDITO", 
            "Transferencia interbancaria recibida");

        movimientoDAO.crear(movOrigen);
        movimientoDAO.crear(movDestino);

        return true;
    }

    public List<Movimiento> obtenerHistorial(int idt) {
        return movimientoDAO.obtenerPorTarjeta(idt);
    }
}
