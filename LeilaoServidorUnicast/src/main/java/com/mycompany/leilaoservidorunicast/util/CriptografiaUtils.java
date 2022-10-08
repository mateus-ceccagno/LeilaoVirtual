package com.mycompany.leilaoservidorunicast.util;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CriptografiaUtils {
    
    public static KeyPair gerarParDeChaves() {
        KeyPair parDeChaves = null;
        try {
            SecureRandom random = new SecureRandom();
            KeyPairGenerator gerador = KeyPairGenerator.getInstance("RSA");
            gerador.initialize(2048, random);
            parDeChaves = gerador.generateKeyPair();
        } catch(Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
        return parDeChaves;
    }
    
    public static void armazenarChave(byte[] chave, String endereco, String nome) {
        try {
            Files.createDirectories(Paths.get(endereco));
            FileOutputStream keyfos = new FileOutputStream(nome);
            keyfos.write(chave);
            keyfos.close();
            System.out.println("OK - Chave gerada em: "+nome+".");
        } catch(IOException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }

    public static PrivateKey lerChavePrivada(String endereco) {
        PrivateKey chave = null;
        try {            
            byte[] keyBytes = Files.readAllBytes(Paths.get(endereco));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            chave = kf.generatePrivate(keySpec);
        } catch(Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
        return chave;
    }
    
    public static PublicKey lerChavePublica(String endereco) {
        PublicKey chave = null;
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(endereco));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            chave = kf.generatePublic(spec);
        } catch(Exception e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }
        return chave;
    }

     public static byte[] cifrarTexto(PublicKey chavePublica, byte[] message) {
        byte[] resultado = null;
        try {
            Cipher cifra = Cipher.getInstance("RSA");
            cifra.init(Cipher.ENCRYPT_MODE, chavePublica);
            
            resultado = cifra.doFinal(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
     
    public static byte[] decifrarTexto(PrivateKey chavePrivada, byte[] message) {
        byte[] resultado = null;
        try {
            Cipher cifra = Cipher.getInstance("RSA");
            cifra.init(Cipher.DECRYPT_MODE, chavePrivada);
            resultado = cifra.doFinal(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

}
