package com.banco.utilidad;

public class Validador {
    //Declara una variable booleana llamada esNumeroValido donde validara el numero
    public static boolean esNumeroValido(String numero) {
        try {
            Double.parseDouble(numero);
            return true;
            //Excepcion donde si entra un numero, regresa y devuelve en falso.
        } catch (NumberFormatException e) {
            return false;
        }
    }
    //Usuario
    //Metodo booleano "esUsuarioValido" con String usuario.
    public static boolean esUsuarioValido(String usuario) {
        //Verificacion del usuario
        return usuario != null && usuario.length() >= 4 && usuario.matches("[a-zA-Z0-9_]+");
    }
    //Contraseña
    //Metodo booleano "esContraseñaValida" que recibe String contraseña
    public static boolean esContraseñaValida(String contraseña) {
        return contraseña != null && contraseña.length() >= 6;
    }
    //Tarjeta
    //Metodo booleano "esNumeroTarjetaValido" que recibe String numero.
    public static boolean esNumeroTarjetaValido(String numero) {
        return numero != null && numero.length() == 16 && numero.matches("[0-9]+");
    }
    //Saldo
    //Metodo booleano "isSaldoValido" que recibe double saldo.
    public static boolean isSaldoValido(double saldo) {
        return saldo >= 0;
    }
}
