/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.leilaocliente;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

import com.mycompany.leilaoservidormulticast.compartilhado.domain.Leilao;

/**
 *
 * @author skmat
 */
public class OfertarLanceForm extends javax.swing.JFrame {

    private ClienteLicitante bidder;
    private ClienteLicitanteConcorrente bidderCandidate = new ClienteLicitanteConcorrente();
    private ArrayList<Leilao> leiloes = new ArrayList<Leilao>();
    /**
     * Creates new form BidderForm
     */
    public OfertarLanceForm() {
        initComponents();
        bidderCandidate.enviarListaRequisicoesDoLeilao();
        bidderCandidate.listenListaLeilao(() -> {
            setAuctionList();
        });
        normalMode();
        autoRefresh();
    }

    private void normalMode() {
        txt_chave.setEnabled(true);
        btn_join.setEnabled(true);

        txt_titulo_leilao.setVisible(false);
        txt_last_bid.setVisible(false);
        txt_ultimo_lance_apelido.setVisible(false);
        txt_price.setVisible(false);

        btn_ofertar_lance.setVisible(false);

        lbl_ultima_oferta.setVisible(false);
        lbl_ganhador_atual.setVisible(false);
    }

    private void auctionMode() {
        txt_chave.setEnabled(false);
        btn_join.setEnabled(false);

        txt_titulo_leilao.setVisible(true);
        txt_last_bid.setVisible(true);
        txt_ultimo_lance_apelido.setVisible(true);
        txt_price.setVisible(true);

        btn_ofertar_lance.setVisible(true);

        lbl_ultima_oferta.setVisible(true);
        lbl_ganhador_atual.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_join = new javax.swing.JButton();
        txt_chave = new javax.swing.JTextField();
        panel_auction = new java.awt.Panel();
        txt_titulo_leilao = new javax.swing.JLabel();
        txt_last_bid = new javax.swing.JLabel();
        lbl_ultima_oferta = new javax.swing.JLabel();
        btn_ofertar_lance = new javax.swing.JButton();
        lbl_ganhador_atual = new javax.swing.JLabel();
        txt_ultimo_lance_apelido = new javax.swing.JLabel();
        txt_price = new javax.swing.JFormattedTextField();
        txt_endereco = new javax.swing.JTextField();
        txt_apelido = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_join.setText("Entrar");
        btn_join.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_joinMouseClicked(evt);
            }
        });

        txt_titulo_leilao.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txt_titulo_leilao.setText("Título do leilão");

        txt_last_bid.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txt_last_bid.setText("R$ 5000");

        lbl_ultima_oferta.setText("Última Oferta");

        btn_ofertar_lance.setText("Ofertar Lance");
        btn_ofertar_lance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ofertar_lanceMouseClicked(evt);
            }
        });

        lbl_ganhador_atual.setText("Último a ofertar");

        txt_ultimo_lance_apelido.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txt_ultimo_lance_apelido.setText("Apelido");

        txt_price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        javax.swing.GroupLayout panel_auctionLayout = new javax.swing.GroupLayout(panel_auction);
        panel_auction.setLayout(panel_auctionLayout);
        panel_auctionLayout.setHorizontalGroup(
            panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_auctionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_titulo_leilao)
                    .addGroup(panel_auctionLayout.createSequentialGroup()
                        .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_ofertar_lance))
                    .addGroup(panel_auctionLayout.createSequentialGroup()
                        .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_ultima_oferta)
                            .addComponent(txt_last_bid, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_ultimo_lance_apelido)
                            .addComponent(lbl_ganhador_atual))))
                .addGap(0, 66, Short.MAX_VALUE))
        );
        panel_auctionLayout.setVerticalGroup(
            panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_auctionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_titulo_leilao)
                .addGap(18, 18, 18)
                .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_ultima_oferta)
                    .addComponent(lbl_ganhador_atual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_last_bid)
                    .addComponent(txt_ultimo_lance_apelido))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(panel_auctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ofertar_lance))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_chave)
                    .addComponent(txt_endereco, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel_auction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_apelido, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_join, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_chave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_apelido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btn_join)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_auction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void autoRefresh() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bidderCandidate.enviarListaRequisicoesDoLeilao();
            }
        }, 0, 1500);
    }
    
    private void btn_joinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_joinMouseClicked
        // TODO add your handling code here:
        for (Leilao leilao : leiloes) {
            if(leilao.getStatus() == 2) {
                if(leilao.getEnderecoString().equals(txt_endereco.getText())) {
                    if(leilao.getChaveSimetricaString().equals(txt_chave.getText())){
                        bidder = createBidder(leilao);
                    }
                }
            }
        }
        
        if (bidder.joinAuction()) {
            txt_titulo_leilao.setText(bidder.getAuction().getProduto().getNome());
            updateLastBid();

            bidder.listenBid(() -> {
                updateLastBid();
            }, () -> {
                bidderCandidate.enviarListaRequisicoesDoLeilao();
                normalMode();
            });
            auctionMode();
        } else {
            JOptionPane.showMessageDialog(this, "Auction not started or already finished");
        }
    }//GEN-LAST:event_btn_joinMouseClicked

    private ClienteLicitante createBidder(Leilao leilao) {

        return new ClienteLicitante(txt_apelido.getText(), leilao);
    }

    private void btn_ofertar_lanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ofertar_lanceMouseClicked
        // TODO add your handling code here:
        int price = Integer.parseInt(unformatPrice(txt_price.getText()));
        bidder.fazerLance(price);
    }//GEN-LAST:event_btn_ofertar_lanceMouseClicked

    private void limitText() {
        if(txt_price.getText().length() >= 14)
            txt_price.setText(txt_price.getText().substring(0, 13));
    }
    
    private String unformatPrice(String price) {
        return price.replace(",", "");
    }
    
    private String formatPrice(int price) {
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(price);
    }
    
    private void setAuctionList() {
        System.out.println("set auction");
        leiloes.clear();
        bidderCandidate.getAuctions().forEach((action) -> {
            leiloes.add(action);
        });
    }

    private void updateLastBid() {
        txt_ultimo_lance_apelido.setText(bidder.getAuction().getUltimoLance().getNomeParticipante());
        txt_last_bid.setText("Rp. " + String.valueOf(formatPrice(bidder.getAuction().getUltimoLance().getPreco())));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OfertarLanceForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OfertarLanceForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_join;
    private javax.swing.JButton btn_ofertar_lance;
    private javax.swing.JLabel lbl_ganhador_atual;
    private javax.swing.JLabel lbl_ultima_oferta;
    private java.awt.Panel panel_auction;
    private javax.swing.JTextField txt_apelido;
    private javax.swing.JTextField txt_chave;
    private javax.swing.JTextField txt_endereco;
    private javax.swing.JLabel txt_last_bid;
    private javax.swing.JFormattedTextField txt_price;
    private javax.swing.JLabel txt_titulo_leilao;
    private javax.swing.JLabel txt_ultimo_lance_apelido;
    // End of variables declaration//GEN-END:variables
}
