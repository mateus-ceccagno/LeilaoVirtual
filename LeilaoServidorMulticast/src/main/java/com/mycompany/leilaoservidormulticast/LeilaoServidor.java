/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoservidormulticast;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;
import com.mycompany.leilaoservidormulticast.compartilhado.domain.Cliente;
import com.mycompany.leilaoservidormulticast.compartilhado.dtos.StreamDto;
import com.mycompany.leilaoservidormulticast.utils.CriptografiaUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author skmat
 */
public class LeilaoServidor {

    private ServerSocket server;
    private Socket socket;
    private static final int PORT = 3000;
    private ArrayList<Leilao> leiloes = new ArrayList<>();
    private ArrayList<Cliente> clientes = new ArrayList<>();
    
    public void iniciarServer(){
        try {
            server = new ServerSocket(PORT);
            listenRequisicao();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void pararServer() {
        try {
            server.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void listenRequisicao() {
        new Thread(() -> {
            while (true) {
                try {
                    socket = server.accept();
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

                    new Thread(() -> manipulaRequisicao(input, output)).start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }).start();
        
    }

    public void manipulaRequisicao(ObjectInputStream input, ObjectOutputStream output) {
        try {
            while (true) {
                StreamDto requisicao = (StreamDto) input.readObject();
                if (requisicao.getTipo() == StreamDto.REQUISICAO_LEILOES) {
                    enviaLeiloes(output);
                }
                if (requisicao.getTipo() == StreamDto.REQUISICAO_CLIENTES) {
                    enviaClientes(output);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void enviaLeiloes(ObjectOutputStream output) {
        try {
            output.writeObject(new StreamDto(StreamDto.RESPOSTA_LEILOES, leiloes));
            output.reset();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void enviaClientes(ObjectOutputStream output) {
        try {
            output.writeObject(new StreamDto(StreamDto.RESPOSTA_CLIENTES, clientes));
            output.reset();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addLeilao(Leilao leilao) {
        leiloes.add(leilao);
    }

    public void removeLeilao(int index) {
        leiloes.remove(index);
    }

    public ArrayList<Leilao> getLeiloes() {
        return leiloes;
    }
    
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public Socket getSocket() {
        return socket;
    }

}

