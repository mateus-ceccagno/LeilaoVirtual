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
public class StreamDto implements Serializable {
    private int tipo;
    private Object payload;
    
    public static final int REQUISICAO_LEILOES = 0;    
    public static final int RESPOSTA_LEILOES = 1;
    public static final int REQUISICAO_LANCE = 2;
    public static final int RESPOSTA_LANCE = 3;    
    public static final int LEILAO_TERMINO = 4;
    public static final int REQUISICAO_CLIENTES = 5;
    public static final int RESPOSTA_CLIENTES = 6;

    public StreamDto(int tipo) {
        this.tipo = tipo;
    }
    
    public StreamDto(int tipo, Object payload) {
        this.tipo = tipo;
        this.payload = payload;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}

