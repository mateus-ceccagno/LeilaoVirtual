package com.mycompany.leilaovirtualservidor.domain;

import java.io.Serializable;

public class OfertaLance implements Serializable {
    public OfertaLance(Integer itemId, Cliente participante, double valorOferta) {
        this.itemId = itemId;
        this.cliente = participante;
        this.valorOferta = valorOferta;
    }

    private Integer itemId;
    private Cliente cliente;
    private double valorOferta;

    public Integer getItemId() { 
        return itemId; 
    }
    
    public Cliente getCliente() {
        return cliente;
    }   
    
    public double getValorOferta() {
        return this.valorOferta;
    }
    
}
