/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast.compartilhado.domain;

import com.mycompany.leilaoservidormulticast.utils.CriptografiaUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import javax.crypto.SecretKey;

/**
 *
 * @author skmat
 */
public class Cliente {

    public Cliente(Integer id, String nome) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        this.id = id;
        this.nome = nome;
        this.autorizado = false;
        this.chaveSessao = null;
        
        KeyPair parDeChaves = CriptografiaUtils.gerarParDeChaves();
        chavePublica = parDeChaves.getPublic();
        chavePrivada = parDeChaves.getPrivate();
        String endereco = "C:\\chaves\\chavesPublicas\\"+nome;
        // Armazena chave pública em arquivo
        CriptografiaUtils.armazenarChave(chavePublica.getEncoded(),
                           "C:\\chaves\\chavesPublicas",
                           endereco);
    }
    
    private SecretKey chaveSessao;
    private boolean autorizado;
    private final Integer id;
    private final String nome;
    private final PrivateKey chavePrivada;
    private final PublicKey chavePublica;

    public String getNome() {
        return nome;
    }

    public Integer getId() {
        return id;
    }
    
    public boolean isAutorizado() {
        return autorizado;
    }
    
    public PublicKey getChavePublica() { 
        return chavePublica; 
    }
    
    public PrivateKey getChavePrivada() { 
        return chavePrivada; 
    }

    public SecretKey getChaveSessao() {
        return chaveSessao;
    }

    public void setChaveSessao(SecretKey chaveSessao) {
        this.chaveSessao = chaveSessao;
    }

    /**
     * Verifica veracidade da chave pública fornecida
     * 
     * Lógica de verificação:
     *      Se a chave pública fornecida pertence ao usuário, logo,
     *      somente a chave privada do usuário poderá decifrar uma mensagem cifrada com a chave pública fornecida.
     * 
     * @param endereco
     * @param cliente
     * @return boolean 
     */
    public boolean solicitarEntrada(String endereco, Cliente cliente) {
        try {
            // Leitura do arquivo (chave pública) que o cliente fornece no console
            byte[] dadosChavePublica = Files.readAllBytes(Paths.get(endereco));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(dadosChavePublica);
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            PublicKey chaveInput = rsa.generatePublic(spec);

            // bytes aleatórios
            byte[] hashEmClaro = CriptografiaUtils.gerarHash(CriptografiaUtils.gerarBytesAleatorios());

            // Cifrar bytes aleatórios com chave pública informada
            byte[] textoCifrado = CriptografiaUtils.cifrarTexto(chaveInput, hashEmClaro);
            // Decifrar textoCifradao com chave privada do cliente
            byte[] textoDecifrado = CriptografiaUtils.decifrarTexto(cliente.getChavePrivada(), textoCifrado);
            
            if(Arrays.equals(hashEmClaro, textoDecifrado)){
                cliente.autorizado = true;
                System.out.println("A chave fornecida esta correta, cliente: " + cliente.getNome());
            } else {
                System.out.println("Falha na validaçao! A chave nao confere, "+ cliente.getNome());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
         return cliente.autorizado;
    }

    public byte[] obterMensagemCifrada(byte[] mensagem) {
        return CriptografiaUtils.cifrarTexto(chavePublica, mensagem);
    }

    public boolean getAutorizado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}