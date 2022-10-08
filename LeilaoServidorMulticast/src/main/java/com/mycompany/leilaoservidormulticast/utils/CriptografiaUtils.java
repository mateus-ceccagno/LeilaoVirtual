/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import java.util.Random;

/**
 *
 * @author skmat
 */
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

    public static SecretKey gerarChave() {
        try {
            KeyGenerator gerador = KeyGenerator.getInstance("AES");
            gerador.init(256);
            SecretKey chaveAES = gerador.generateKey();

            /* print para conferir chave gerada*/
            String aesBase64 = Base64.getEncoder().encodeToString(chaveAES.getEncoded());
            System.out.println("aesBase64: " + aesBase64);
            return chaveAES;
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public static Key getChave(String endereco) {
        SecretKey chave = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(endereco));
            chave = new SecretKeySpec(encoded, "AES");
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
     
    public static byte[] cifrarTexto(Key chaveAES, byte[] message) {
        byte[] resultado = null;
        try {
            Cipher cifra = Cipher.getInstance("AES");
            cifra.init(Cipher.ENCRYPT_MODE, chaveAES);
            
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
    
    public static byte[] decifrarTexto(Key chaveAES, byte[] message) {
        byte[] resultado = null;
        try {
            Cipher cifra = Cipher.getInstance("AES");
            cifra.init(Cipher.DECRYPT_MODE, chaveAES);
            resultado = cifra.doFinal(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static byte[] gerarHash(byte[] dados) {
        byte[] resultado = new byte[30];
        try {
            MessageDigest instanciaSHA = MessageDigest.getInstance("SHA-256");
            resultado = instanciaSHA.digest(dados);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static byte[] gerarBytesAleatorios() {
        Random rd = new Random();
        byte[] dados = new byte[32];
        rd.nextBytes(dados);
        return dados;
    }
}
