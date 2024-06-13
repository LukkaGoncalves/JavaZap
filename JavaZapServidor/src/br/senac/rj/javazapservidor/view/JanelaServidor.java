package br.senac.rj.javazapservidor.view;

import br.senac.rj.javazapservidor.model.Servidor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JanelaServidor extends JFrame {
    public static JFrame criarJanelaServidor(Servidor servidor) {

        JFrame janelaServidor = new JFrame("Server Side");
        janelaServidor.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        janelaServidor.setSize(800, 800);

        Container caixa = janelaServidor.getContentPane();
        caixa.setLayout(null);

        String ipAddress = servidor.verificaIp();
        int port = servidor.getPORT();

        // Criando o título com IP e Porta
        JLabel titulo = new JLabel("IP: " + ipAddress + "    Porta: " + port);
        titulo.setBounds(45, 20, 700, 20);
        janelaServidor.add(titulo);

        JTextArea conversa = new JTextArea();
        conversa.setEditable(false);
        conversa.setBackground(Color.WHITE);
        janelaServidor.add(conversa);
        JScrollPane scrollConversa = new JScrollPane(conversa);
        scrollConversa.setBounds(45, 50, 700, 500);
        janelaServidor.add(scrollConversa);
        conversa.append("Aguardando conexão com o cliente\n");

        JTextArea mensagem = new JTextArea();
        mensagem.setLineWrap(true);
        mensagem.setWrapStyleWord(true);
        mensagem.setEditable(false);
        mensagem.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollMensagem = new JScrollPane(mensagem);
        scrollMensagem.setBounds(35, 575, 610, 150);
        caixa.add(scrollMensagem);

        JButton botaoEnviarMensagem = new JButton("Enviar");
        botaoEnviarMensagem.setBounds(655, 575, 100, 150);
        janelaServidor.add(botaoEnviarMensagem);
        botaoEnviarMensagem.setEnabled(false);
        botaoEnviarMensagem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                try {
                    String conteudoMensagem = mensagem.getText();
                    if (!conteudoMensagem.isEmpty()) {
                        conversa.append("Servidor: " + conteudoMensagem + "\n");
                        mensagem.setText("");
                        servidor.enviarMensagem(conteudoMensagem);
                    }



                } catch (Exception erro) {
                    JOptionPane.showMessageDialog(janelaServidor, "Erro ao iniciar o chat: " + erro);
                }
            }
        });

        new Thread(() -> {
            try {
                servidor.conectarCliente(conversa, mensagem, botaoEnviarMensagem);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(janelaServidor, "Erro ao conectar ao servidor: " + e.getMessage()));
            } catch (IOException e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(janelaServidor, "Erro de I/O: " + e.getMessage()));
            }
        }).start();

        janelaServidor.setVisible(true);
        return janelaServidor;
    }

    public static void main(String[] args) {
        Servidor server = new Servidor();
        JFrame testFrame = criarJanelaServidor(server);

    }
}
