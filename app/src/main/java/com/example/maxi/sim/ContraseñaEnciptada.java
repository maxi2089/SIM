package com.example.maxi.sim;

import java.net.PasswordAuthentication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yamila on 16/10/2015.
 */
public class ContraseñaEnciptada {
    ;
    private String passwordEncriptada;

    ContraseñaEnciptada() {
    }

    public String getPasswordEncriptada() {
        return passwordEncriptada;
    }


    public void EncriptarContraseña(String password,String Algortimo){
        passwordEncriptada = getStringMessageDigest(password,Algortimo);
    }

    /***
     * Convierte un arreglo de bytes a String usando valores hexadecimales
     * @param digest arreglo de bytes a convertir
     * @return String creado a partir de <code>digest</code>
     */
    private static String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /***
     * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
     * @param message texto a encriptar
     * @param algorithm algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
     * @return mensaje encriptado
     */
    public static String getStringMessageDigest(String message, String algorithm){
        byte[] digest = null;
        byte[] buffer = message.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(buffer);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error creando Digest");
        }
        return toHexadecimal(digest);
    }
}
