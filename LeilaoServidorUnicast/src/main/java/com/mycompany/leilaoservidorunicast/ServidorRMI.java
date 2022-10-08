package com.mycompany.leilaoservidorunicast;



import com.mycompany.leilaoservidorunicast.rmi.IProtocoloRMI;
import com.mycompany.leilaoservidorunicast.rmi.ProtocoloRMI;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServidorRMI extends ProtocoloRMI {
    public ServidorRMI(){
        try {
            ProtocoloRMI servidorRMI = new ProtocoloRMI();
            IProtocoloRMI _rmi = (IProtocoloRMI) UnicastRemoteObject.exportObject(servidorRMI, 1099);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServidorRMI", _rmi);
            System.out.println("ServidorRMI OK");
            
        } catch (RemoteException e) {
            System.err.println("Falha no servidor: "+e.toString());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ServidorRMI();
    }

}
