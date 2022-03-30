package org.example.models;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.shape.StrokeLineCap;
import org.example.Error;
import org.example.controllers.PrimaryController;
import org.example.server.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.SplittableRandom;

public class Network {


    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT =8188;
    private DataOutputStream out;
    private DataInputStream in;

    private final String host;
    private final int port;
    private String username;

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
    public void sendMessage(String message){
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Сообщение не отправлено!");
        }
    }
    public void sendPrivateMessage(String selectedRecipient, String message) {
        sendMessage(String.format("%s %s %s", Error.PRIVAT_MSG_CMD_PREFIX.getText(), selectedRecipient, message));
    }
    public void waitMessage(PrimaryController primaryController){
        Thread t1 = new Thread(()->{
            try {
                while (true) {
                    String message = in.readUTF();
                    if (message.startsWith(Error.CLIENT_MSG_CMD_PREFIX.getText())) {
                        String[] parts = message.split("\\s+", 3);
                        String sender = parts[1];
                        String messageFromSender = parts[2];
                        Platform.runLater(() -> primaryController.appendMessage(String.format("%s %s", sender, messageFromSender)));
                    } else if (message.startsWith(Error.SERVER_MSG_CMD_PREFIX.getText())) {
                        String[] parts = message.split("\\s+", 2);
                        String serverMessage = parts[1];
                        Platform.runLater(() -> primaryController.appendServerMessage(serverMessage));
                    } else if (message.startsWith(Error.SERVER_ADD_USER_ONLINE_PREFIX.getText())) {
                        String[] parts = message.split("\\s+", 2);
                        String userOnline = parts[1];
                        Platform.runLater(() -> primaryController.setAddUsersOnline(userOnline));
                    } else if (message.startsWith(Error.SERVER_REMOVE_USER_ONLINE_PREFIX.getText())) {
                        String[] parts = message.split("\\s+", 2);
                        String userOnline = parts[1];
                        Platform.runLater(() -> primaryController.setRemoveUserOnline(userOnline));
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        t1.setDaemon(true);
        t1.start();
    }


    public String sendAuthMessage(String login, String password) {
        try {
            out.writeUTF(String.format("%s %s %s", Error.AUTH_CMD_PREFIX.getText(),login,password));
            String response = in.readUTF();
            if(response.startsWith(Error.AUTHOK_CMD_PREFIX.getText())){
                this.username = response.split("\\s+", 2)[1];
                return null;
            }else {
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getUsername() {
        return username;
    }

}
