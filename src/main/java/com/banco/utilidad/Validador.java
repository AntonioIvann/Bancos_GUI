package com.banco.utilidad;

public class Validador {
    
    public static boolean esNumeroValido(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esUsuarioValido(String usuario) {
        return usuario != null && usuario.length() >= 4 && usuario.matches("[a-zA-Z0-9_]+");
    }

    public static boolean esContraseñaValida(String contraseña) {
        return contraseña != null && contraseña.length() >= 6;
    }

    public static boolean esNumeroTarjetaValido(String numero) {
        return numero != null && numero.length() == 16 && numero.matches("[0-9]+");
    }

    public static boolean isSaldoValido(double saldo) {
        return saldo >= 0;
    }
}
