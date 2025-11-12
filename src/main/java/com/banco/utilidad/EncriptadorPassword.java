package com.banco.utilidad;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncriptadorPassword {
    
    public static String encriptar(String contraseña) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Agregar un salt simple
            String contraConSalt = "bancos_sistema_2024_" + contraseña;
            byte[] hash = md.digest(contraConSalt.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verificar(String contraseñaIngresada, String contraseñaEncriptada) {
        String encriptada = encriptar(contraseñaIngresada);
        return encriptada != null && encriptada.equals(contraseñaEncriptada);
    }
}
