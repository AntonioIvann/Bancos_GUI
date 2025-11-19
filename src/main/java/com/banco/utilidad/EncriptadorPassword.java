package com.banco.utilidad;

import java.security.MessageDigest;         // Permite crear hashes criptográficos
import java.security.NoSuchAlgorithmException; // Excepción si el algoritmo no existe

public class EncriptadorPassword {
    // Clase encargada de encriptar contraseñas y verificarlas
    // usando hashing con SHA-256 y un salt simple.
    
    public static String encriptar(String contraseña) {
        try {
            // Obtiene una instancia del algoritmo de hash SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Se agrega un "salt" simple (texto adicional) para evitar ataques
            // a contraseñas comunes y dificultar el uso de rainbow tables.
            String contraConSalt = "bancos_sistema_2024_" + contraseña;

            // Convierte la cadena con salt en un arreglo de bytes y genera el hash
            byte[] hash = md.digest(contraConSalt.getBytes());

            // Convertirá los bytes del hash a formato hexadecimal
            StringBuilder sb = new StringBuilder();

            // Recorre cada byte y lo transforma en un string de dos dígitos hexadecimales
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
                // "%02x" → convierte el byte a hex con dos dígitos (ej. 0f, 2a, ff)
            }

            return sb.toString(); // Retorna el hash final como texto hexadecimal
        } catch (NoSuchAlgorithmException e) {
            // Esta excepción es muy rara porque SHA-256 siempre debería existir en Java.
            e.printStackTrace();
            return null; // Si algo falla, devuelve null
        }
    }

    public static boolean verificar(String contraseñaIngresada, String contraseñaEncriptada) {
        // Toma la contraseña ingresada por el usuario y la encripta
        String encriptada = encriptar(contraseñaIngresada);

        // Compara el resultado con la contraseña previamente almacenada
        // Si ambas coinciden, la contraseña es correcta.
        return encriptada != null && encriptada.equals(contraseñaEncriptada);
    }
}
