package com.banco.servicio;

import com.banco.dao.TarjetaDAO;
import com.banco.modelo.Tarjeta;
import java.util.List;
import java.util.Random;

public class TarjetaServicio {
    private TarjetaDAO tarjetaDAO;

    public TarjetaServicio() {
        this.tarjetaDAO = new TarjetaDAO();
    }

    public String generarNumeroTarjeta() {
        StringBuilder numero = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            numero.append(random.nextInt(10));
        }
        return numero.toString();
    }

    public boolean crearTarjeta(int idc, int idb) {
        String numeroTarjeta = generarNumeroTarjeta();
        
        // Verificar que no exista
        while (tarjetaDAO.obtenerPorNumero(numeroTarjeta) != null) {
            numeroTarjeta = generarNumeroTarjeta();
        }

        Tarjeta tarjeta = new Tarjeta(idc, idb, numeroTarjeta);
        return tarjetaDAO.crear(tarjeta) > 0;
    }

    public List<Tarjeta> obtenerTarjetasCliente(int idc) {
        return tarjetaDAO.obtenerPorCliente(idc);
    }

    public Tarjeta obtenerTarjeta(int idt) {
        return tarjetaDAO.obtenerPorId(idt);
    }

    public boolean actualizarTarjeta(Tarjeta tarjeta) {
        return tarjetaDAO.actualizar(tarjeta);
    }
}
