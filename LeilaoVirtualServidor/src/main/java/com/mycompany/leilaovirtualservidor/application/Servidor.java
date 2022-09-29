package com.mycompany.leilaovirtualservidor.application;

import com.mycompany.leilaovirtualservidor.application.ProtocoloRMI;
import com.mycompany.leilaovirtualservidor.contratos.IProtocoloRMI;
import com.mycompany.leilaovirtualservidor.util.CriptografiaUtils;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;

public class Servidor extends ProtocoloRMI {
    public Servidor(){
        try {
            ProtocoloRMI servidorRMI = new ProtocoloRMI();
            CriptografiaUtils.gerarChave();
            
            IProtocoloRMI _rmi = (IProtocoloRMI) UnicastRemoteObject.exportObject(servidorRMI, 1099);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServidorRMI", _rmi);
            
        } catch (RemoteException e) {
            System.err.println("Falha no servidor: "+e.toString());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Servidor();
    }
}
