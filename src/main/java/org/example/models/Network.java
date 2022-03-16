package org.example.models;

import org.example.controllers.Message;
import org.example.controllers.PrimaryController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT =8186;
    private DataOutputStream out;
    private DataInputStream in;

    private final String host;
    private final int port;

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }
    public Network(){
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public void connect(){
        try  {
            Socket socket = new Socket(host,port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Соединение не установлено");
        }


    }
    public DataOutputStream getOut() {
        return out;
    }
    public void sendMessage(Message  message){
        try {
            out.writeUTF(message.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Сообщение не отправлено!");
        }
    }
    public void waitMessage(PrimaryController primaryController){
        Thread t1 = new Thread(()->{
            try {
                while (true){
                    Message message = new Message("Серверное сообщение: "+in.readUTF());
                    primaryController.appendMessage(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
    }
}
