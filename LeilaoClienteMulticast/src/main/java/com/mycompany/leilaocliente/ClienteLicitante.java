/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.leilaocliente;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;
import com.mycompany.leilaoservidormulticast.compartilhado.dtos.LanceDto;
import com.mycompany.leilaoservidormulticast.compartilhado.dtos.StreamDto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
// import javax.xml.crypto.Data; desnecessario

/**
 *
 * @author skmat
 */
public class ClienteLicitante {    
    private String nomeLicitante;
    private MulticastSocket socket;
    private Leilao leilao;

    public ClienteLicitante(String nomeLicitante, Leilao leilao) {
        try {
            this.nomeLicitante = nomeLicitante;
            this.leilao = leilao;
            this.socket = new MulticastSocket(leilao.getPorta());
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean joinAuction() {
        try {
            if (leilao.getStatus() == Leilao.INICIADO) {
                socket.joinGroup(leilao.getEndereco());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return leilao.getStatus() == Leilao.INICIADO;
    }

    public void sairDoLeilao() {
        try {
            socket.leaveGroup(leilao.getEndereco());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean fazerLance(int preco) {
        if (isPriceEnough(preco)) {
            try {
                ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
                ObjectOutputStream outputObject = new ObjectOutputStream(outputBytes);

                outputObject.writeObject(new StreamDto(StreamDto.REQUISICAO_LANCE, new LanceDto(preco, nomeLicitante)));
                byte[] bytesData = outputBytes.toByteArray();
                DatagramPacket packet = new DatagramPacket(bytesData, bytesData.length, leilao.getEndereco(), leilao.getPorta());
                socket.send(packet);
                outputObject.reset();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return isPriceEnough(preco);
    }

    public boolean isPriceEnough(int preco) {
        return preco > leilao.getUltimoLance().getPreco() + 9;
    }

    public void listenBid(Runnable callbackBid, Runnable callbackEnd) {
        new Thread(() -> {
            while (true) {
                try {
                    byte[] incomingData = new byte[1024];
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    socket.receive(incomingPacket);

                    ByteArrayInputStream inputBytes = new ByteArrayInputStream(incomingPacket.getData());
                    ObjectInputStream inputObject = new ObjectInputStream(inputBytes);

                    StreamDto data = (StreamDto) inputObject.readObject();
                    if (data.getTipo() == StreamDto.RESPOSTA_LANCE) {
                        leilao.setUltimoLance((LanceDto) data.getPayload());
                        callbackBid.run();
                    } else if (data.getTipo() == StreamDto.LEILAO_TERMINO) {
                        sairDoLeilao();
                        callbackEnd.run();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }).start();
    }

    public Leilao getAuction() {
        return leilao;
    }
}
