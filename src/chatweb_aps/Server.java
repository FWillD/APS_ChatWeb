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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

        
public class Server extends Thread {
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream inputStream;
    private InputStreamReader inputStrReader;
    private BufferedReader buffReader;

    
    public Server(Socket con){
        this.con = con;
        
        try {
            inputStream  = con.getInputStream();
            inputStrReader = new InputStreamReader(inputStream);
            buffReader = new BufferedReader(inputStrReader);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void run(){
        try{
            String message;
            
            OutputStream outputStr = this.con.getOutputStream();
            Writer outputWriter = new OutputStreamWriter(outputStr);
            BufferedWriter bufferWriter = new BufferedWriter(outputWriter);
            clientes.add(bufferWriter);
            nome = message = buffReader.readLine();
             
            while(!"Sair".equalsIgnoreCase(message) && message != null){
                message = buffReader.readLine();
                sendToAll(bufferWriter, message);
                System.out.println(message);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void sendToAll(BufferedWriter buffWrtSaida, String message) throws IOException{
        BufferedWriter bwSaida;
        
        for(BufferedWriter bw: clientes){
            bwSaida = (BufferedWriter)bw;
            if(!(buffWrtSaida == bwSaida)){
                bw.write(nome + " -> " + message + "\r\n");
                bw.flush();
            }
        }
    }
    
    public static void main(String[] args) {
        try{
            //Objetos necessário para instânciar o servidor
            JLabel lblMessage = new JLabel("Porta do Servidor:");
            JTextField txtPorta = new JTextField("11223");
            Object[] textos = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, textos);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null, "Servidor ativo na Porta: " + txtPorta.getText());
            
            
            while(true){
                System.out.println("Aguardando Conexão...");
                Socket con = server.accept();
                System.out.println("Cliente Conectado");
                Thread t = new Server(con);
                t.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
