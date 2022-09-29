package com.mycompany.leilaovirtualcliente;

import com.mycompany.leilaovirtualservidor.contratos.IProtocoloRMI;
import com.mycompany.leilaovirtualservidor.domain.Cliente;
import com.mycompany.leilaovirtualservidor.domain.Leilao;
import com.mycompany.leilaovirtualservidor.domain.OfertaLance;
import com.mycompany.leilaovirtualservidor.util.CriptografiaUtils;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.HashMap;
import java.util.Scanner;

public class ClienteConsole {
    public static void main(String[] args) {
        ClienteConsole cliente = new ClienteConsole();
        cliente.startInput();
    }

    public ClienteConsole() {
        try {
            Registry registry = LocateRegistry.getRegistry();
            _rmi = (IProtocoloRMI) registry.lookup("ServidorRMI"); 
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, Cliente> clientes;
    private Cliente clienteAtual = null;
    private IProtocoloRMI _rmi;

    private void startInput() {
        try {
            System.out.println("Ola Cliente!\nDigite `ajuda` para ver os comandos.");
            Scanner scanner = new Scanner(System.in);

            // Laço para ouvir o que o usuário solicita
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] entrada = input.split("/");
                if (entrada.length < 2) {
                    System.out.println("### Comandos disponiveis ###");
                    System.out.println("conta/entrar");
                    System.out.println("conta/detalhes");
                    System.out.println("leilao/ofertarLance/");
                    System.out.println("leilao/listar");
                    continue;
                }

                this.clientes = _rmi.getClientes();
                switch(entrada[0].toLowerCase()) {
                    case "conta":
                        switch(entrada[1].toLowerCase()) {
                            case "entrar":
                                // enviar participante.ChavePublica para servidor
                                // cifrar endereço+porta e chave simetrica leiloeiro com participante.ChavePublica
                                if (clientes.isEmpty()) {
                                    System.out.println("Nao ha contas. Crie contas com `conta/criar`");
                                    break;
                                }
                                if (entrada.length < 4) {
                                    System.out.println("Para entrar no servidor utilize: `conta/entrar/{idCliente}/{chavePublica}`.");
                                    break;
                                } else {

                                    Cliente clienteAtual = clientes.get(Integer.parseInt(entrada[2]));
                                    clienteAtual.solicitarEntrada(entrada[3], clienteAtual); //OK
                                    if(clientes.get(Integer.parseInt(entrada[2])).getAutorizado()){ //OK
                                        byte[] endereco = _rmi.solicitarEnderecoMulticast(clienteAtual.getId());
                                        byte[] chaveAES = _rmi.solicitarChaveAES(clienteAtual.getId());
                                        byte[] enderecoo = CriptografiaUtils.decifrarTexto(clienteAtual.getChavePrivada(), endereco);
                                        byte[] chaveAESS = CriptografiaUtils.decifrarTexto(clienteAtual.getChavePrivada(), chaveAES);
                                        String enderecoLimpo = new String(enderecoo, StandardCharsets.UTF_8);
                                        System.out.println(enderecoLimpo);
                                        
                                        System.out.println(endereco.toString());
                                        System.out.println(chaveAES.toString());
                                    }
                                    if (!clienteAtual.getAutorizado()) System.out.println("Nao foi possivel autorizar Cliente`.");
                                }
                                break;
                            case "criar":
                                if (entrada.length < 3) {
                                    System.out.println("Para criar contas utilize: `conta/criar/{nome}`");
                                    break;
                                }
                                // Create a new buyer with unique id
                                Integer idGerado = 0;
                                for (int i = 0; i < clientes.size(); ++i) {
                                    if (clientes.get(i).getId() == i) idGerado++;
                                    else break;
                                }
                                Cliente cliente = new Cliente(idGerado, entrada[2]);
                                clienteAtual = cliente;
                                _rmi.addCliente(clienteAtual);
                                System.out.println("Cliente criado com ID: "+idGerado+".");
                                break;
                            case "detalhes":
                                if (clienteAtual == null) {
                                    System.out.println("Nenhum participante logado.");
                                    break;
                                }
                                System.out.println("Participante logado: " + clienteAtual.getNome() + "; "+clienteAtual.getId());
                                break;
                        }
                        break;
                    case "leilao":
                    if (clienteAtual == null) {
                        System.out.println("Realize login antes.");
                        break;
                    }
                    switch(entrada[1].toLowerCase()) {
                        case "ofertarLance":
                            if (entrada.length < 3) {
                                System.out.println("Para Ofertar Lance utilize: `leilao/lance/{idItem}/{9999.99}`");
                                break;
                            }
                            Integer id = Integer.parseInt(entrada[2]);
                            double retorno = _rmi.ofertarLance(new OfertaLance(id, clienteAtual, Double.parseDouble(entrada[2])));
                            if (retorno == -1) {
                                System.out.println("Item ID provided is not valid");
                                break;
                            }
                            else if (retorno == -2) {
                                System.out.println("Item has been sold, cannot bid");
                                break;
                            }
                            else if (retorno == -3) {
                                System.out.println("Bid was not successful. Need to bid more than: "+_rmi.getLeilao(id).getValorAtual());
                                break;
                            }
                            System.out.println("Bid processed successfully");

                            /* verificar tempo
                            int timesBidded = 0;
                            ArrayList<OfertaLance> ofertas = _rmi.getLeilao(id).getListaOfertaDeLance();
                            for (OfertaLance oferta : ofertas) {
                                if (oferta.getCliente().getId().equals(participanteAtual.getId())) {
                                    timesBidded++;
                                }
                            }
                            if (timesBidded <= 1) participanteAtual.finalizaLeilao(_rmi, id); // Polls server every second on a new thread to check if item is won
                            */
                            break;
                        case "listar":
                            HashMap<Integer, Leilao> itens = _rmi.getLeiloes();
                            if (itens.isEmpty()) {
                                System.out.println("No auction items are currently available to bid on");
                                break;
                            }
                            for (Leilao item : itens.values()) {
                                if (item.isVendido()) continue; 
                                System.out.println("ID: " + "`"+item.getId()+"`" + " Nome: " + "`"+item.getNome()+"`" +
                                        " Valor mais alto: "+item.getValorAtual());
                            }
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
