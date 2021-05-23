/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.Socket;
import model.Servidor;

/**
 *
 * @author F. William
 */
public class EsperarConexoes extends Thread {
    private int porta;
    private Servidor servidor;
    
    public EsperarConexoes(int porta){
        this.porta = porta;
        servidor = new Servidor();
    }

    public EsperarConexoes(){}
    
    public String verificarServidor(){
        String verificaErro = servidor.instanciarServer(porta);//Chama método da classe Servidor, onde instancia um ServerSocket e um ArrayList para armazenar os BufferedWriter
        
        if(verificaErro.equals("ERRO")){//Se deu erro na instanciação do servidor, é pq já está em conectado, sendo assim 
            System.out.println("Servidor já está rodando!");
            return "ERRO";//Se der ERRO, é pq o servidor já está rodando
        }
        return "SUCESSO";//se der SUCESSO, é pq o servidor não estava ativo, mas agora está
    }
    
    public void run(){
        try{
            while(true){//While ficará aguardando a conexao de novos usuários
                System.out.println("Aguardando Conexão...");
                Socket socket = servidor.getServerSocket().accept();
                servidor.setSocket(socket);//Insere o socket do cliente conectado ao servidor
                System.out.println("Cliente Conectado");
                
                Thread t = new Servidor(socket);
                t.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Servidor getServidor() {
        return servidor;
    }
    
    
    
}
