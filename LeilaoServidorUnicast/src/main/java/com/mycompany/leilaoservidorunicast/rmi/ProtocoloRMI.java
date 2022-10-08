package com.mycompany.leilaoservidorunicast.rmi;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;
import com.mycompany.leilaoservidorunicast.comunicacaoexterna.ComunicaMulticast;
import com.mycompany.leilaoservidorunicast.domain.Cliente;
import com.mycompany.leilaoservidorunicast.util.CriptografiaUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

public class ProtocoloRMI implements IProtocoloRMI {
    private ArrayList<Cliente> clientes = new ArrayList<Cliente>(); 
    
    private ComunicaMulticast comunicacoExterna = new ComunicaMulticast();
    private ArrayList<Leilao> dadosLeiloes = new ArrayList<Leilao>();
    private PublicKey chavePublica;

    
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public Cliente getCliente(int id) {
        return clientes.get(id);
    }
        
    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }


    public boolean informarChavePublica(String endereco) {
        try {
            byte[] dadosChavePublica = Files.readAllBytes(Paths.get(endereco));
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(dadosChavePublica);            
            chavePublica = rsa.generatePublic(spec);
            solicitarDadosSobreLeiloes();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] getChaveSimetrica() throws RemoteException {
        String chaveSimetrica = null;
        for (Leilao auction : dadosLeiloes) {
            if(auction.getStatus() == 2){
                chaveSimetrica = Base64.getEncoder().encodeToString(
                    auction.getChaveSimetrica().getEncoded());
                
                System.out.println("conferencia no get: " + chaveSimetrica);
                
                return CriptografiaUtils.cifrarTexto(chavePublica, chaveSimetrica.getBytes());
            }
        }
        return null;
    }
    
    public byte[] getEnderecoMulticast() throws RemoteException{
        String endereco = null;
        for (Leilao auction : dadosLeiloes) {
            if(auction.getStatus() == 2){
                endereco = Base64.getEncoder().encodeToString(auction.getEndereco().getHostAddress().getBytes());
                System.out.println("conferencia no get: " + endereco);
                System.out.println(CriptografiaUtils.cifrarTexto(chavePublica, endereco.getBytes()));
                return CriptografiaUtils.cifrarTexto(chavePublica, endereco.getBytes());
            }
        }
        return null;
    }
    
    public void solicitarDadosSobreLeiloes() throws IOException, ClassNotFoundException {
        comunicacoExterna.enviarListaRequisicoesDoLeilao();          
        dadosLeiloes = comunicacoExterna.listenListaLeilao();
    }
}