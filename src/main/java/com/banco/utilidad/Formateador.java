package com.banco.utilidad;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public class Formateador {
    
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatearMoneda(double cantidad) {
        return "$" + df.format(cantidad);
    }

    public static String formatearFecha(LocalDateTime fecha) {
        if (fecha == null) return "N/A";
        return fecha.format(dtf);
    }

    public static String formatearNombreCompleto(String nombre, String apellidoP, String apellidoM) {
        return nombre + " " + apellidoP + " " + apellidoM;
    }
}
