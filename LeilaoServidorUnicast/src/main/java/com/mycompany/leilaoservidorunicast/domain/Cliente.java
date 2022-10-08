package com.mycompany.leilaoservidorunicast.domain;

import com.mycompany.leilaoservidorunicast.util.CriptografiaUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import java.security.*;

public class Cliente implements Serializable{

    public Cliente(String nome) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        this.nome = nome;
        
        KeyPair parDeChaves = CriptografiaUtils.gerarParDeChaves();

        // Armazena chave pública em arquivo
        CriptografiaUtils.armazenarChave(
            parDeChaves.getPublic().getEncoded(),
            "C:\\chaves\\chavesPublicas",
            "C:\\chaves\\chavesPublicas\\"+nome);
        
        CriptografiaUtils.armazenarChave(
            parDeChaves.getPrivate().getEncoded(),
            "C:\\chaves\\chavesPrivadas",
            "C:\\chaves\\chavesPrivadas\\"+nome);
    }
    
    private final String nome;

    public String getNome() {
        return nome;
    }

}