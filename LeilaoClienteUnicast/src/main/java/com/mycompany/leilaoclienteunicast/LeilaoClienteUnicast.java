/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.leilaoclienteunicast;

import com.mycompany.leilaoservidorunicast.domain.Cliente;
import com.mycompany.leilaoservidorunicast.rmi.IProtocoloRMI;
import com.mycompany.leilaoservidorunicast.util.CriptografiaUtils;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author skmat
 */
public class LeilaoClienteUnicast {
    public static void main(String[] args) throws NoSuchAlgorithmException, RemoteException {        
        LeilaoClienteUnicast cliente = new LeilaoClienteUnicast();
        cliente.startInput();
    }

    public LeilaoClienteUnicast() {
        try {
            Registry registry = LocateRegistry.getRegistry();
            _rmi = (IProtocoloRMI) registry.lookup("ServidorRMI"); 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    private Cliente clienteAtual = null;
    private IProtocoloRMI _rmi;

    private void startInput() throws RemoteException {
        try {
            System.out.println("Ola Cliente!\nDigite `ajuda` para ver os comandos.");
            Scanner scanner = new Scanner(System.in);

            // Laço para ouvir o que o usuário solicita
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] entrada = input.split("/");
                if (entrada.length < 2) {
                    System.out.println("### Comandos disponiveis ###");
                    System.out.println("conta/criar");
                    System.out.println("conta/entrar");
                    continue;
                }

                switch(entrada[0].toLowerCase()) {
                    case "conta":
                        switch(entrada[1].toLowerCase()) {
                            case "entrar":
                                if (entrada.length < 4) {
                                    System.out.println("Para entrar no servidor utilize: `conta/entrar/{nome}/{chavePublica}`.");
                                    break;
                                } else {
                                    _rmi.informarChavePublica(entrada[3]);
                                    PrivateKey chavePrivada = CriptografiaUtils.lerChavePrivada("C:\\chaves\\chavesPrivadas\\"+entrada[2]);

                                    byte[] enderecoCifrado = _rmi.getEnderecoMulticast();
                                    byte[] chaveCifrada = _rmi.getChaveSimetrica();
                                    if (enderecoCifrado != null && chaveCifrada != null) {
                                        byte[] enderecoDecifrado = CriptografiaUtils.decifrarTexto(chavePrivada, enderecoCifrado);
                                        byte[] chaveDecifrada = CriptografiaUtils.decifrarTexto(chavePrivada, chaveCifrada);

                                        // Reconstrução da chaveDecifrada em texto
                                        byte[] decodedKey = Base64.getDecoder().decode(chaveDecifrada);
                                        SecretKey chaveAES = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

                                        // Representação da chave simetrica em texto
                                        System.out.print("Chave Multicast: ");
                                        System.out.println(Base64.getEncoder().encodeToString(chaveAES.getEncoded()));

                                        System.out.print("Endereco Multicast: ");
                                        System.out.println(new String(Base64.getDecoder().decode(enderecoDecifrado)));
                                    }
                                    else {
                                        System.out.println("Nenhum leilão iniciado");
                                        break;
                                    }
                                }
                            break;
                            case "criar":
                                if (entrada.length < 3) {
                                    System.out.println("Para criar contas utilize: `conta/criar/{nome}`");
                                    break;
                                }
                                Cliente cliente = new Cliente(entrada[2]);
                                clienteAtual = cliente;
                                _rmi.addCliente(clienteAtual);
                                System.out.println("Cliente " +entrada[2]+ " gerarado.");
                                break;
                        }
                        break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

