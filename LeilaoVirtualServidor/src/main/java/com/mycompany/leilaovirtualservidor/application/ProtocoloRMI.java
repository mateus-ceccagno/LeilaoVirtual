package com.mycompany.leilaovirtualservidor.application;

import com.mycompany.leilaovirtualservidor.contratos.IProtocoloRMI;
import com.mycompany.leilaovirtualservidor.domain.Leilao;
import com.mycompany.leilaovirtualservidor.domain.OfertaLance;
import com.mycompany.leilaovirtualservidor.domain.Cliente;
import com.mycompany.leilaovirtualservidor.util.CriptografiaUtils;
import java.rmi.RemoteException;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import java.security.*;
import java.util.HashMap;

public class ProtocoloRMI implements IProtocoloRMI {
    private HashMap<Integer, Leilao> leiloes = new HashMap<>();
    private HashMap<Integer, Cliente> clientes = new HashMap<>();
    private PublicKey chavePublica;
    
    public HashMap<Integer, Leilao> getLeiloes() {
        return leiloes;
    }
    
    public Leilao getLeilao(int id) throws RemoteException {
        return leiloes.get(id);
    }

    public HashMap<Integer, Cliente> getClientes() {
        return clientes;
    }

    public Cliente getCliente(int id) {
        return clientes.get(id);
    }
        
    public void addCliente(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
    }

    /**
     * Sealed Object: "This class enables a programmer to create an object and 
     *      protect its confidentiality with a cryptographic algorithm."
    */
    public SealedObject getVenda(int itemId) {
        SealedObject retornoSealed = null;
        try {
            Cipher cifra = Cipher.getInstance("AES");
            cifra.init(cifra.ENCRYPT_MODE, CriptografiaUtils.getChave("chave.txt"));
            retornoSealed = new SealedObject(leiloes.get(itemId), cifra);
            System.out.println("Sealed OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retornoSealed;
    } 

    public int addLeilao(String nome, double valorInicial, double incremento, long tempo ){
        // Autogerar IDs para leilão
        int itemId = 0;
        for (int i = 0; i < leiloes.size(); ++i) {
            if (leiloes.get(i).getId() == i) itemId++;
            else break;
        }
        leiloes.put(itemId, new Leilao(itemId, nome, valorInicial, incremento, tempo));
        return itemId;
    }

    public double finalizarLeilao(int itemId) {
        if (leiloes.get(itemId) == null) return -1; // Não existe item
        if (leiloes.get(itemId).getListaOfertaDeLance().isEmpty()) return -3; // Sem lances para o item
        OfertaLance ofertaAtual = leiloes.get(itemId).getListaOfertaDeLance().get(0);
        for (OfertaLance oferta : leiloes.get(itemId).getListaOfertaDeLance()) {
            if (oferta.getValorOferta()> ofertaAtual.getValorOferta()) {
                System.out.println("Oferta deve ser maior ou igual R$" + 
                        ofertaAtual.getValorOferta()+leiloes.get(itemId).getIncrementoMinimo());
                return -4;
            }
        }

        if (false) { // TODO: Como verificar que leilão terminou?
            return -5; 
        } else {
            leiloes.get(itemId).setVencedor(ofertaAtual.getCliente().getId());
            System.out.println(ofertaAtual.getCliente().getNome()+" venceu. Código Item: "+itemId);
            //log.info("Leilao: "+ itemId+ " vendido para" + ofertaAtual.getCliente().getNome());
            return 0;
        }
    }

    public synchronized double ofertarLance(OfertaLance oferta) {
        Leilao item = leiloes.get(oferta.getItemId());
        if (item == null) return -1;
        else if (item.isVendido()) return -2;
        else if (oferta.getValorOferta()+item.getIncrementoMinimo()<= item.getValorAtual()) return -3; // Check if bid is valid, highestBidAmount is always >= startingPrice
        item.addOfertaLance(oferta);
        for (OfertaLance i : item.getListaOfertaDeLance()) {
            System.out.println("Oferta de Lance: "+i.getValorOferta()+ "  Cliente: "+i.getCliente().getNome() + "  Item Código: " + i.getItemId());
        }
        return 0;
    }

    public byte[] obterMensagemCifrada(byte[] bytesGerados) {
        return CriptografiaUtils.cifrarTexto(chavePublica, bytesGerados);
    }
        
    public byte[] solicitarEnderecoMulticast(int clientID){
        byte[]  obj = null;
        try {
            Cliente clienteSolicitante = clientes.get(clientID);           
            obj = CriptografiaUtils.cifrarTexto(
                                    clienteSolicitante.getChavePublica(),
                                    "123.123".getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    
    public byte[] solicitarChaveAES(int clientID){
        byte[] obj = null;
        try {
            Cliente clienteSolicitante = clientes.get(clientID);
            Key chaveAES = CriptografiaUtils.getChave("C:\\chaves\\chaveAES\\chaveSimetricaServidor");
            obj = CriptografiaUtils.cifrarTexto(
                                    clienteSolicitante.getChavePublica(),
                                    chaveAES.getEncoded());
 
        } catch(Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}