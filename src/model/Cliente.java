/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.EnviarMensagem;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

/**
 *
 * @author F. William
 */
public class Cliente {
    private  String nickname;
    
    private  Socket socket;
    private  OutputStream outputStr;
    private  Writer outputWriter;
    private  BufferedWriter buffWriter;
    
    
    
    //Método conectar fará a conexão do cliente com o Servidor Socket
    public void conectar(String nickname, String host, int porta) throws IOException{
        this.nickname = nickname;
        
        socket = new Socket(host, porta);
        outputStr = socket.getOutputStream();
        outputWriter = new OutputStreamWriter(outputStr);
        buffWriter = new BufferedWriter(outputWriter);
        
        buffWriter.write(nickname + "\r\n");
        buffWriter.flush();
        
        
    }
    
    //Método para desconectar fechando os streams de comunicação
    public void sair(EnviarMensagem enviarMsg) throws IOException{
        enviarMsg.enviarMensagem("$Sair");
        buffWriter.close();
        outputWriter.close();
        outputStr.close();
        socket.close();
        
        
    }
    
   public String getNickname(){
       return this.nickname;
   }
    
   public Socket getSocket(){
        return this.socket;
   }

   public OutputStream getOutputStr(){
       return this.outputStr;
   }
   
   public BufferedWriter getBuffWriter(){
        return this.buffWriter;
   }
   
   public Writer getOutputWriter(){
       return this.outputWriter;
   }

   
    
   
}
