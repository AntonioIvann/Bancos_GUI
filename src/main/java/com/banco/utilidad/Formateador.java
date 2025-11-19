package com.banco.utilidad;

import java.time.LocalDateTime;                // Clase para manejar fechas y horas
import java.time.format.DateTimeFormatter;     // Para dar formato personalizado a fechas
import java.text.DecimalFormat;                // Permite formatear números con decimales

public class Formateador {
    // Clase con métodos estáticos para formatear valores como moneda, fecha y nombres.

    // Crea un formateador de números decimales con el formato:
    // "#,##0.00" — esto hace:
    //   - Separador de miles: ","
    //   - Siempre dos decimales
    //   - Muestra 0 si la cantidad no tiene parte entera
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    // Formateador de fechas con el patrón:
    // "dd/MM/yyyy HH:mm:ss"
    // Ejemplo: 17/11/2025 18:42:10
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatearMoneda(double cantidad) {
        // Devuelve la cantidad con formato de moneda mexicana:
        // "$1,234.50"
        return "$" + df.format(cantidad);
    }

    public static String formatearFecha(LocalDateTime fecha) {
        // Si la fecha es nula, evita error y devuelve "N/A"
        if (fecha == null) return "N/A";

        // Convierte el LocalDateTime a texto usando el formato definido arriba
        return fecha.format(dtf);
    }

    public static String formatearNombreCompleto(String nombre, String apellidoP, String apellidoM) {
        // Une los tres campos para formar el nombre completo de un usuario
        // Ejemplo: formatearNombreCompleto("Juan", "Pérez", "López")
        // Resultado: "Juan Pérez López"
        return nombre + " " + apellidoP + " " + apellidoM;
    }
}
