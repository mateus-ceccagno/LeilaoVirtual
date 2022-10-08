/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast.compartilhado.domain;

import com.mycompany.leilaoservidormulticast.compartilhado.dtos.LanceDto;
import com.mycompany.leilaoservidormulticast.utils.CriptografiaUtils;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Base64;

import javax.crypto.SecretKey;

/**
 *
 * @author skmat
 */
public class Leilao implements Serializable {
    public static final int NAO_INICIADO = 1;    
    public static final int INICIADO = 2;
    public static final int FINALIZADO = 3;
    
    private final Produto produto;
    private LanceDto ultimoLance;
    private final InetAddress endereco;
    private final int porta;
    private final Leiloeiro leiloeiro;
    private int status = Leilao.NAO_INICIADO;
    private final SecretKey chaveSimetrica;

    public Leilao(Produto produto, InetAddress endereco, int porta) {
        this.produto = produto;
        this.ultimoLance = new LanceDto(produto.getPreco());
        this.endereco = endereco;
        this.porta = porta;
        this.leiloeiro = new Leiloeiro(this);
        this.chaveSimetrica = CriptografiaUtils.gerarChave();

        System.out.print("Criação endereco leilao: ");
        System.out.println(endereco.getHostAddress());
    }
    
    public void iniciarLeilao(Runnable callback) {
        leiloeiro.entrarLeilao();
        leiloeiro.listenLance(callback);
    }
    
    public void pararLeilao() {
        leiloeiro.sairLeilao();
    }

    public Produto getProduto() {
        return produto;
    }

    public LanceDto getUltimoLance() {
        return ultimoLance;
    }

    public void setUltimoLance(LanceDto ultimoLance) {
        this.ultimoLance = ultimoLance;
    }

    public InetAddress getEndereco() {
        //CriptografiaUtils.cifrarTexto(chavePublica,
        //                              endereco.getEndereco().toString().getBytes());
        return endereco;
    }

    public int getPorta() {
        return porta;
    }
    
    public Leiloeiro getLeiloeiro() {
        return leiloeiro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SecretKey getChaveSimetrica() {
        return chaveSimetrica;
    }
    public String getEnderecoString() {
        return endereco.getHostAddress();
    }
        
    public String getChaveSimetricaString() {
        return Base64.getEncoder().encodeToString(chaveSimetrica.getEncoded());
    }

}
