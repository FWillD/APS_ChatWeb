/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import model.Cliente;
import view.Chat;

/**
 *
 * @author F. William
 */
public class EnviarMensagem {
    private Chat chat;
    private Cliente client;
    
     //Método enviarMensagem enviar as mensagens do cliente para o servidor socket toda vez que
    //o Enter for pressionado
    public void enviarMensagem(String mensagem) throws IOException{
        
        if(mensagem.equals("$Sair")){
            client.getBuffWriter().write("Desconectado \r\n");
            chat.getTextAreaHistorico().append("Desconectado \r\n");
        }else{
            client.getBuffWriter().write(mensagem + "\r\n");
            chat.getTextAreaHistorico().append(client.getNickname() + " -> " + mensagem + "\r\n");
            
        }
        client.getBuffWriter().flush();
        chat.getTextFieldMensagem().setText("");
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    
    
    
}
