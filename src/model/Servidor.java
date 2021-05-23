/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author F. William
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Servidor extends Thread{
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private String nome;
    private InputStream inputStream;
    private InputStreamReader inputStrReader;
    private BufferedReader buffReader;
    
   
    public Servidor(){}
    public Servidor(Socket socket){
       this.socket = socket;
        try{
           inputStream  = socket.getInputStream();
           inputStrReader = new InputStreamReader(inputStream);
           buffReader = new BufferedReader(inputStrReader);
            
       }catch(IOException e){
           e.printStackTrace();
       }
    }
    
    public void run(){
        try{
            String mensagem;
            
            
            OutputStream outputStr = this.socket.getOutputStream();
            Writer outputWriter = new OutputStreamWriter(outputStr);
            BufferedWriter bufferWriter = new BufferedWriter(outputWriter);
            
            clientes.add(bufferWriter);
            mensagem = buffReader.readLine();
            nome = mensagem;
            
            while(!"$Sair".equalsIgnoreCase(mensagem) && mensagem != null){
                mensagem = buffReader.readLine();
                if(!mensagem.equals("null")){
                    sendToAll(bufferWriter, mensagem);
                }
                System.out.println(mensagem);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void sendToAll(BufferedWriter buffWrtSaida, String mensagem) throws IOException{
        BufferedWriter bwSaida;
        
        for(BufferedWriter bw: clientes){
            bwSaida = (BufferedWriter)bw;
            if(!(buffWrtSaida == bwSaida)){
                bw.write(nome + " -> " + mensagem + "\r\n");
                bw.flush();
            }
        }
    }

    public String instanciarServer(int porta){
        try{
            serverSocket = new ServerSocket(porta);//Instancia um servidor na porta especificada
            clientes = new ArrayList<BufferedWriter>();
        }catch(Exception e){
            //e.printStackTrace();
            //Se já houver um servidor conectado, então entrar no catch
            System.out.println("OI, DEU ERRO! rs");
            return "ERRO";// retorna a String ERRO para futura verificação
        }
        return "SUCESSO";
    }
    
    
    //Getters e Setters
    public static ServerSocket getServerSocket() {
        return serverSocket;
    }
    public Socket getSocket(){
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public static ArrayList<BufferedWriter> getClientes() {
        return clientes;
    }

    public String getNome() {
        return nome;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public InputStreamReader getInputStrReader() {
        return inputStrReader;
    }

    public BufferedReader getBuffReader() {
        return buffReader;
    }
    
    
}
