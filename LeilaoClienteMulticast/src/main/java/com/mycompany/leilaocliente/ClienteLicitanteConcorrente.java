/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.leilaocliente;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;
import com.mycompany.leilaoservidormulticast.compartilhado.dtos.StreamDto;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skmat
 */
public class ClienteLicitanteConcorrente {
    
    Socket socket;
    InetAddress serverAddress;
    int serverPort;
    ObjectOutputStream output;
    ObjectInputStream input;
    ArrayList<Leilao> auctions = new ArrayList<>();

    public ClienteLicitanteConcorrente() {
        try {
            serverAddress = InetAddress.getByName("localhost");
            serverPort = 3000;
            socket = new Socket(serverAddress, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClienteLicitanteConcorrente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClienteLicitanteConcorrente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarListaRequisicoesDoLeilao() { //verificar lógica do nome em razão do código
        try {
            StreamDto data = new StreamDto(StreamDto.REQUISICAO_LEILOES);
            output.writeObject(data);
        } catch (IOException ex) {
            Logger.getLogger(ClienteLicitanteConcorrente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void listenListaLeilao(Runnable callback) {
        new Thread(() -> {
            while (true) {
                try {
                    StreamDto data = (StreamDto) input.readObject();
                    
                    if (data.getTipo() == StreamDto.RESPOSTA_LEILOES) {
                        auctions = (ArrayList<Leilao>) data.getPayload();
                        callback.run();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ClienteLicitanteConcorrente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    public ArrayList<Leilao> getAuctions() {
        return auctions;
    }
}
