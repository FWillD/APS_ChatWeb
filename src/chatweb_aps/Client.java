/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatweb_aps;

/**
 *
 * @author F. William
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;

public class Client extends JFrame implements ActionListener, KeyListener{
    //Componentes Frame
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMessage;
    private JButton btnEnviar;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlConteudo;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;
    
    //Atributos Socket
    private Socket socket;
    private OutputStream outputStr;
    private Writer outputWriter;
    private BufferedWriter buffWriter;
    
    //Método Construtor constrói a tela do chat 
    public Client() throws IOException{
        JLabel lblMessage = new JLabel("Verificar!");
        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("11223");
        txtNome = new JTextField("Cliente");
        
        Object[] texts = {lblMessage, txtIP, txtPorta, txtNome};
        JOptionPane.showMessageDialog(null, texts);
        
        pnlConteudo = new JPanel();
        texto = new JTextArea(10, 20);
        texto.setEditable(false);
        texto.setBackground(new Color(240, 240, 240));
        
        txtMessage = new JTextField(20);
        
        lblHistorico = new JLabel("Histórico");
        lblMsg = new JLabel("Mensagem");
        
        btnEnviar = new JButton("Enviar");
        btnEnviar.setToolTipText("Enviar Messagem");
        btnSair = new JButton("Sair");
        btnSair.setToolTipText("Sair do Chat");
        btnEnviar.addActionListener(this);
        btnSair.addActionListener(this);
        btnEnviar.addKeyListener(this);
        txtMessage.addKeyListener(this);
        
        JScrollPane scroll = new JScrollPane(texto);
        texto.setLineWrap(true);
        
        pnlConteudo.add(lblHistorico);
        pnlConteudo.add(scroll);
        pnlConteudo.add(lblMsg);
        pnlConteudo.add(txtMessage);
        pnlConteudo.add(btnSair);
        pnlConteudo.add(btnEnviar);
        pnlConteudo.setBackground(Color.LIGHT_GRAY);
        
        texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        txtMessage.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        
        setTitle(txtNome.getText());
        setContentPane(pnlConteudo);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    //Método conectar fará a conexão do cliente com o Servidor Socket
    public void conectar() throws IOException{
        
        socket = new Socket(txtIP.getText(), Integer.parseInt(txtPorta.getText()));
        outputStr = socket.getOutputStream();
        outputWriter = new OutputStreamWriter(outputStr);
        buffWriter = new BufferedWriter(outputWriter);
        
        buffWriter.write(txtNome.getText() + "\r\n");
        buffWriter.flush();
    }
    
    
    //Método enviarMensagem enviar as mensagens do cliente para o servidor socket toda vez que
    //o Enter for pressionado
    public void enviarMessagem(String mensagem) throws IOException{
        if(mensagem.equals("Sair")){
            buffWriter.write("Desconectado \r\n");
            texto.append("Desconectado \r\n");
        }else{
            buffWriter.write(mensagem + "\r\n");
            texto.append(txtNome.getText() + " diz -> " + txtMessage.getText() + "\r\n");
            
        }
        buffWriter.flush();
        txtMessage.setText("");
    }
    
    //Método escutar recebe as mensagens do servidor e enviar para todos os clentes conectados
    public void escutar() throws IOException{
        InputStream inputStr = socket.getInputStream();
        InputStreamReader inputStrReader = new InputStreamReader(inputStr);
        BufferedReader buffReader = new BufferedReader(inputStrReader);
        String msg = "";
        
        while(!"Sair".equalsIgnoreCase(msg)){
            if(buffReader.ready()){
                msg = buffReader.readLine();
            
                if(msg.equals("Sair")){
                    texto.append("Servidor caiu! \r\n");
                }else{
                    texto.append(msg + "\r\n");
                }
            }
        } 
    }
    
    //Método para desconectar fechando os streams de comunicação
    public void sair() throws IOException{
        enviarMessagem("Sair");
        buffWriter.close();
        outputWriter.close();
        outputStr.close();
        socket.close();
        dispose();
    }
    
    
    
    //Método que enviará a mensagem caso seja pressionado o botão enviar e sairá do chat
    //caso o botão sair seja pressionado
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getActionCommand().equals(btnEnviar.getActionCommand())){
                enviarMessagem(txtMessage.getText());
            }else{
                if(e.getActionCommand().equals(btnSair.getActionCommand())){
                    sair();
                }
            }
        }catch(IOException e1){
            e1.printStackTrace();
        }
    }

    
    //Implementação Obrigatória
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    //Método que verifica se o Key code é o Enter para enviar a mensagem caso este seja pressionado
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            try{
                enviarMessagem(txtMessage.getText());
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }
    }

    //Implementação Obrigatória
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    
    public static void main(String[] args) throws IOException {
        
        Client app = new Client();
        app.conectar();
        app.escutar();
    }
    
    
}
