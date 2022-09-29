package com.mycompany.leilaovirtualservidor.contratos;

import com.mycompany.leilaovirtualservidor.domain.Leilao;
import com.mycompany.leilaovirtualservidor.domain.Cliente;
import com.mycompany.leilaovirtualservidor.domain.OfertaLance;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.crypto.SealedObject;

public interface IProtocoloRMI extends Remote {
    // Consultas
    public HashMap<Integer, Leilao> getLeiloes() throws RemoteException;
    public Leilao getLeilao(int id) throws RemoteException;
    public HashMap<Integer, Cliente> getClientes() throws RemoteException;
    public Cliente getCliente(int id) throws RemoteException;
    public SealedObject getVenda(int itemId) throws RemoteException;
    
    // Criações
    public int addLeilao(String nome, double valorInicial, double incremento, long tempo) throws RemoteException; 
    public void addCliente(Cliente clientes) throws RemoteException; 
    public double ofertarLance(OfertaLance ofertaLance) throws RemoteException;
    
    // Criptografia
    public byte[] obterMensagemCifrada(byte[] mensagem) throws RemoteException;    
    public byte[] solicitarEnderecoMulticast(int idCliente) throws RemoteException;
    public byte[] solicitarChaveAES(int idCliente) throws RemoteException; 
    
    // Finalizadores
    public double finalizarLeilao(int itemId) throws RemoteException;
}