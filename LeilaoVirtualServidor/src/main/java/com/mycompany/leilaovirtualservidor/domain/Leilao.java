package com.mycompany.leilaovirtualservidor.domain;
import java.io.Serializable;
import java.util.ArrayList;

public class Leilao implements Serializable {

    public Leilao(Integer id, String nome, double valorInicial, double incrementoMinimo, long tempoEmSegundos) {
        this.id = id;
        this.nome = nome;
        this.valorInicial = valorInicial;
        this.valorAtual = valorInicial;
        this.incrementoMinimo = incrementoMinimo;
        this.tempo = tempoEmSegundos;
    }

    private final Integer id;
    private final String nome;
    private final double valorInicial;
    private final double incrementoMinimo;
    private final long tempo; 

    private ArrayList<OfertaLance> listaOfertaDeLance = new ArrayList<>();
    private double valorAtual;
    private Integer vencedor = null;
    private boolean vendido = false;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public double getIncrementoMinimo() {
        return incrementoMinimo;
    }

    public ArrayList<OfertaLance> getListaOfertaDeLance() {
        return listaOfertaDeLance;
    }
    
    // TODO trocar para multicast
    public void addOfertaLance(OfertaLance oferta) {
        listaOfertaDeLance.add(oferta);
        valorAtual = oferta.getValorOferta();
    }

    public long getTempo() {
        return tempo;
    }

    public Integer getVencedor() {
        return vencedor;
    }
    
    public void setVencedor(Integer vencedor) {
        this.vencedor = vencedor;
    }

    public boolean isVendido() {
        return vendido;
    }

    public void setVendido(boolean vendido) {
        this.vendido = vendido;
    }

}
