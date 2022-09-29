package com.mycompany.leilaovirtualservidor;

import com.mycompany.leilaovirtualservidor.application.Servidor;
import com.mycompany.leilaovirtualservidor.contratos.IProtocoloRMI;
import com.mycompany.leilaovirtualservidor.domain.Leilao;
import com.mycompany.leilaovirtualservidor.domain.OfertaLance;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServidorConsole {
    public static void main(String[] args) throws RemoteException {
        new Servidor();
        ServidorConsole console = new ServidorConsole();
        console.startInput();
    }

    public ServidorConsole() {
        try {
            registry = LocateRegistry.getRegistry(1099);
            _rmi = (IProtocoloRMI) registry.lookup("ServidorRMI"); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IProtocoloRMI _rmi;
    private Registry registry;

    public void startInput() throws RemoteException {
        try {
            System.out.println("Ola, Leiloeiro!\nUtilize `ajuda` para obter os comandos disponiveis.");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] entrada = input.split("/");

                switch(entrada[0].toLowerCase()) {
                    case "criarleilao":
                        if (entrada.length < 4) {
                            System.out.println("Para criar um Leilao utilize: `criarLeilao/{titulo}/{valorInicial}/{incremento}/{tempoEmSegundos}`");
                        } else {
                            int itemId = _rmi.addLeilao(entrada[1], Double.parseDouble(entrada[2]), Double.parseDouble(entrada[3]), Long.parseLong(entrada[4]));
                            System.out.println("Leilao criado com sucesso, IdLeilao: " + itemId);
                        }
                        break;
                    case "listarleiloes":
                        HashMap<Integer, Leilao> itens = _rmi.getLeiloes();
                        if(itens.isEmpty()){
                            System.out.println("Sem leiloes cadastrados.");
                            break;
                        }
                        else{
                            for (Leilao item : itens.values()) {
                                    System.out.println("ID: " + item.getId() + "; Nome: " + item.getNome());
                            }
                        }
                        break;
                    case "fecharleilao":
                        if (entrada.length < 2) {
                            System.out.println("Para finalizar um leilao manualmente: `fecharLeilao/{leilaoId}`");
                            break;
                        }
                        double codFinalizar = _rmi.finalizarLeilao(Integer.parseInt(entrada[1]));
                        Leilao item = _rmi.getLeilao(Integer.parseInt(entrada[1]));
                        if (codFinalizar == 0) System.out.println("Auction has closed for "+item.getId()+". The winner was "+_rmi.getClientes().get(item.getVencedor()).getNome());
                        else if (codFinalizar == -1) System.out.println("Item ID provided was not valid, use `auction list` to view current auctions");
                        else if (codFinalizar == -2) System.out.println("You are not authorised to close this auction");
                        else if (codFinalizar == -3) System.out.println("There are no bids on the item yet");
                        else if (codFinalizar == -4) System.out.println("Failed to meet reserve price, max bid was: "+item.getValorAtual()+ ". Reserve was "+item.getValorAtual()+item.getIncrementoMinimo());
                        break;
                    case "ofertas":
                        if (entrada.length < 2) {
                            System.out.println("Para ver as ofertas de um leilao utilize: `ofertar/{leilaoId}`");
                            break;
                        }
                        ArrayList<OfertaLance> ofertas = _rmi.getLeiloes().get(Integer.parseInt(entrada[1])).getListaOfertaDeLance();
                        if (ofertas.isEmpty()){
                            System.out.println("Sem ofertas de lances para o leilao informado.");
                            break;
                        }
                        for (OfertaLance oferta : ofertas) {
                            System.out.println("R$: "+oferta.getValorOferta()+ "; Cliente: "+oferta.getCliente()+ "; Leilao: " + oferta.getItemId());
                        }
                    break;
                    default:
                        System.out.println("### Comandos Disponiveis ###\n`criarLeilao`\n`listarLeiloes`\n`ofertas`");
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
