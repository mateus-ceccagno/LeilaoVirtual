package com.mycompany.leilaoservidorunicast.rmi;

import com.mycompany.leilaoservidorunicast.domain.Cliente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IProtocoloRMI extends Remote {
    // Cliente
    public void addCliente(Cliente clientes) throws RemoteException; 
    public ArrayList<Cliente> getClientes() throws RemoteException;
    
    // Comunicação
    public boolean informarChavePublica(String endereco) throws RemoteException; 
    public byte[] getEnderecoMulticast() throws RemoteException;
    public byte[] getChaveSimetrica() throws RemoteException;
}