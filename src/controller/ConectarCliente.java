/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import model.Cliente;
import view.Chat;

/**
 *
 * @author F. William
 */
public class ConectarCliente extends Thread{
    private String nickname;
    private String host;
    private int porta;
    
    private Cliente client;
    private Chat chat;
    private EnviarMensagem enviarMsg;
    
    
    public ConectarCliente(String nickname, String host, int porta){
        this.nickname = nickname;
        this.host = host;
        this.porta = porta;
        
        client = new Cliente();
        chat = new Chat();
        enviarMsg = new EnviarMensagem();
        
    }
    //Método escutar recebe as mensagens do servidor e enviar para todos os clentes conectados
    public void run() {
        try{
            client.conectar(nickname, host, porta);
            enviarMsg.setClient(client);
            enviarMsg.setChat(chat);
            chat.setClient(client); //Setado na classe Chat para utilização do métodoa Sair()
            chat.setEnviarMsg(enviarMsg);
            chat.setVisible(true);//Exibe o form Chat.java
            
            InputStream inputStr = client.getSocket().getInputStream();
            InputStreamReader inputStrReader = new InputStreamReader(inputStr);
            BufferedReader buffReader = new BufferedReader(inputStrReader);
            String msg = "";
        
        
            while(!"Sair".equalsIgnoreCase(msg)){//loop While ficará "escutando" novas mensagens vindas do servidor
                if(buffReader.ready()){
                    msg = buffReader.readLine();

                    if(msg.equals("Sair")){
                        chat.getTextAreaHistorico().append("Servidor caiu! \r\n");
                    }else{
                        chat.getTextAreaHistorico().append(msg + "\r\n");
                    }
                }
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
         
        
    
    
}
