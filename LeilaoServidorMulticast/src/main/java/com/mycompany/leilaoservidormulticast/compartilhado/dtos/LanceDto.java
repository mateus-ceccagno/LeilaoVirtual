/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast.compartilhado.dtos;

import java.io.Serializable;

/**
 *
 * @author skmat
 */
public class LanceDto  implements Serializable {
    private int preco;
    private String nomeParticipante;

    public LanceDto(int preco) {
        this.preco = preco;
    }
    
    public LanceDto(int preco, String nomeParticipante) {
        this.preco = preco;
        this.nomeParticipante = nomeParticipante;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public void setNomeParticipante(String nomeParticipante) {
        this.nomeParticipante = nomeParticipante;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }
}

