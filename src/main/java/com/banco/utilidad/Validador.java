//Clase validador para validar datos de entrada, que cumplan los criterios necesarios

package com.banco.utilidad; 

public class Validador {
    
    public static boolean esNumeroValido(String numero) {
        // Valida si el texto representa un número válido
        try {
            Double.parseDouble(numero); // Intenta convertir el texto a número
            return true;                // Si funciona, es un número válido
        } catch (NumberFormatException e) {
            return false;               // Si falla, no es un número válido
        }
    }

    public static boolean esUsuarioValido(String usuario) {
        // Valida que el nombre de usuario cumple con estos criterios, valores no nulos, al menos 4 caracteres y solo letras, números y ciertos caracteres especiales
        return usuario != null 
                && usuario.length() >= 4 
                && usuario.matches("[a-zA-Z0-9_]+");
    }

    public static boolean esContraseñaValida(String contraseña) {
        // Verifica que la contraseña no sea nula y tenga al menos 6 caracteres
        return contraseña != null && contraseña.length() >= 6;
    }

    public static boolean esNumeroTarjetaValido(String numero) {
        // Valida que el numero de tarjeta cumpla estos criterios: Valores no nulos, 16 dígitos y solo números del 0 a 9
        return numero != null 
                && numero.length() == 16 
                && numero.matches("[0-9]+");
    }

    public static boolean isSaldoValido(double saldo) {
        // Valida si el saldo es mayor o igual a cero y no puede ser negativo
        return saldo >= 0;
    }
}
