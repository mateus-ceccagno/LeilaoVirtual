/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast.compartilhado.domain;

import com.mycompany.leilaoservidormulticast.compartilhado.dtos.LanceDto;
import com.mycompany.leilaoservidormulticast.compartilhado.dtos.StreamDto;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.io.Serializable;

/**
 *
 * @author skmat
 */
public class Leiloeiro implements Serializable {

    private transient MulticastSocket socket;
    private Leilao leilao;

    public Leiloeiro(Leilao leilao) {
        try {
            this.leilao = leilao;
            socket = new MulticastSocket(leilao.getPorta());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void entrarLeilao() {
        try {
            socket.joinGroup(leilao.getEndereco());
            leilao.setStatus(Leilao.INICIADO);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void sairLeilao() {
        try {
            enviarFimDoLeilao();
            socket.leaveGroup(leilao.getEndereco());
            leilao.setStatus(Leilao.FINALIZADO);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listenLance(Runnable callback) {
        new Thread(() -> {
            while (true) {
                try {
                    byte[] incomingData = new byte[1024];
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    socket.receive(incomingPacket);
                    
                    ByteArrayInputStream inputBytes = new ByteArrayInputStream(incomingPacket.getData());
                    ObjectInputStream inputObject = new ObjectInputStream(inputBytes);
                    
                    StreamDto data = (StreamDto) inputObject.readObject();
                    LanceDto lanceRequisicao = (LanceDto) data.getPayload();
                    
                    if (data.getTipo() == StreamDto.REQUISICAO_LANCE) {
                        if (lanceRequisicao.getPreco() > leilao.getUltimoLance().getPreco()) {
                            leilao.setUltimoLance(lanceRequisicao);
                            enviarUltimoLance();
                            callback.run();
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }).start();
    }

    public void enviarUltimoLance() {
        try {
            ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
            ObjectOutputStream outputObject = new ObjectOutputStream(outputBytes);

            outputObject.writeObject(new StreamDto(StreamDto.RESPOSTA_LANCE, leilao.getUltimoLance()));
            byte[] bytesData = outputBytes.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytesData, bytesData.length, leilao.getEndereco(), leilao.getPorta());
            socket.send(packet);
            System.out.println("Envio do ultimo lance");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void enviarFimDoLeilao() {
        try {
            ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
            ObjectOutputStream outputObject = new ObjectOutputStream(outputBytes);

            outputObject.writeObject(new StreamDto(StreamDto.LEILAO_TERMINO));
            byte[] bytesData = outputBytes.toByteArray();
            DatagramPacket packet = new DatagramPacket(bytesData, bytesData.length, leilao.getEndereco(), leilao.getPorta());
            socket.send(packet);
            System.out.println("Envio do ultimo lance"); // enviar ultima leilao
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
