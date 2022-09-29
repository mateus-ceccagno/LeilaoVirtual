package com.mycompany.leilaovirtualservidor.domain;

import com.mycompany.leilaovirtualservidor.contratos.IProtocoloRMI;
import com.mycompany.leilaovirtualservidor.contratos.IServidor;
import com.mycompany.leilaovirtualservidor.util.CriptografiaUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Cliente implements IServidor {

    public Cliente(Integer id, String nome) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        this.id = id;
        this.nome = nome;
        this.autorizado = false;
        
        KeyPair parDeChaves = CriptografiaUtils.gerarParDeChaves();
        chavePublica = parDeChaves.getPublic();
        chavePrivada = parDeChaves.getPrivate();
        String endereco = "C:\\chaves\\chavesPublicas\\"+nome;
        // Armazena chave pública em arquivo
        CriptografiaUtils.armazenarChave(chavePublica.getEncoded(),
                           "C:\\chaves\\chavesPublicas",
                           endereco);
    }
    
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
    
    public boolean getAutorizado() {
        return autorizado;
    }
    
    public PublicKey getChavePublica() { 
        return chavePublica; 
    }
    
    public PrivateKey getChavePrivada() { 
        return chavePrivada; 
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

    public byte[] obterMensagemCifrada(byte[] message) {
        return CriptografiaUtils.cifrarTexto(chavePublica, message);
    }

    public void finalizaLeilao(IProtocoloRMI stub, Integer itemId) {
        new Thread( new Runnable() {
            boolean hasSold = false;
            public void run() {               
                try {
                    Leilao item = stub.getLeilao(itemId);
                    long tempo = item.getTempo()* 1000;
                    Thread.sleep(tempo);
                    if (!item.getListaOfertaDeLance().isEmpty()) {
                        item.setVendido(true);
                    }
                }
                catch (Exception e) {
                    System.err.println("Falha ao gerenciar tempo de leilao: "+e.toString());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}