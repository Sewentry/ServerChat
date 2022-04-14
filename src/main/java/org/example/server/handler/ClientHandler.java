package org.example.server.handler;

import org.example.Error;
import org.example.server.authentication.AuthenticationService;
import org.example.server.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {



    private MyServer myServer;
    private Socket cleintSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;


    public ClientHandler(MyServer myServer, Socket socket) {

        this.myServer = myServer;
        cleintSocket = socket;
    }

    public void handler() throws IOException {
        out = new DataOutputStream(cleintSocket.getOutputStream());
        in = new DataInputStream(cleintSocket.getInputStream());

        new Thread(()->{
            try {
                checkAuthOrRegister();
                readMessage();
            } catch (Exception e) {
                try {
                    myServer.unSubscribe(this);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
    }
    private void checkAuthOrRegister() throws SQLException, IOException {
        while (true){
            String message = in.readUTF();
            if(message.startsWith(Error.REGISTER_CMD_PREFIX.getText())){
                registration(message);
            }else if(message.startsWith(Error.AUTH_CMD_PREFIX.getText())){
                authentication(message);
            }
        }
    }

    private  void registration(String message) throws IOException, SQLException {
        while (true){
                boolean isSuccessRegister = processRegister(message);
                if(isSuccessRegister){
                    break;
                }
                else{
                out.writeUTF(Error.REGISERERR_CMD_PREFIX.getText()+" ошибка регистрации");
                System.out.println("неудачная попытка регистрации");
            }
        }
    }

    private  void authentication(String message) throws IOException, SQLException {
          while (true){

                  boolean isSuccessAuth = processAuthentication(message);
                  if(isSuccessAuth){
                      break;
                  }
              else{
                  out.writeUTF(Error.AUTH_CMD_PREFIX.getText()+" ошибка аутентификации");
                  System.out.println("неудачная попытка аутентификации");
              }
          }
    }

    private void readMessage() throws IOException, SQLException {
        while (true){
            String message = in.readUTF();
            System.out.println("message |" + username + ": "+ message);
            if(message.startsWith(Error.STOP_SERVER_CMD_PREFIX.getText())){
                System.exit(0);
            }else if(message.startsWith(Error.END_CLIENT_CMD_PREFIX.getText())){
                return;
            }else if(message.startsWith(Error.PRIVAT_MSG_CMD_PREFIX.getText())){
                myServer.broadcastPrivateMessage(message,this);
            }else if(message.startsWith(Error.SERVER_CHANGE_USERNAME_PREFIX.getText())){
               myServer.changeUsername(message,this);
            }
            else{
                myServer.broadcastMessage(message,this);
            }
        }

    }

    private boolean processAuthentication(String message) throws IOException, SQLException {
        String[] parts = message.split("\\s+");
        if(parts.length !=3){
            out.writeUTF(Error.AUTHERR_CMD_PREFIX.getText() + " ошибка аутентификации");
        }
        String login = parts[1];
        String password = parts[2];

        AuthenticationService auth = myServer.getAuthenticationService();

        username = auth.getUsernameByLoginAndPassword(login,password);


        if(username!=null){
            if(myServer.isUsernameBusy(username)) {
                out.writeUTF(Error.AUTH_CMD_PREFIX.getText() +" Логин уже используется");
                return false;
            }
            out.writeUTF(Error.AUTHOK_CMD_PREFIX.getText() + " " + username);
            myServer.subscribe(this);
            System.out.println("Client: |" + username + ": подключился к чату");
            myServer.broadcastMessage(String.format(">>> %s присоединился к чату", username),this,true);
            return true;
        }else{
            out.writeUTF(Error.AUTH_CMD_PREFIX.getText()+" Логин или пароль не найдены");
            return false;
        }
    }

    private boolean processRegister(String message) throws IOException {
        String[] parts = message.split("\\s+");
        if(parts.length !=4){
            out.writeUTF(Error.REGISERERR_CMD_PREFIX.getText() + " ошибка регистрации");
        }
        String login = parts[1];
        String password = parts[2];
        username = parts[3];

        AuthenticationService reg = myServer.getAuthenticationService();
        if(reg.checkLoginByFree(login)){
            reg.createUser(login,password,username);
            out.writeUTF(Error.REGISTEROK_CMD_PREFIX.getText()+" "+username);
            myServer.subscribe(this);
            System.out.println("Client: |" + username + ": подключился к чату");
            myServer.broadcastMessage(String.format(">>> %s присоединился к чату", username),this,true);
            return true;
        }else{
            out.writeUTF(Error.REGISERERR_CMD_PREFIX.getText()+"логин уже используется");
            return false;
        }
    }
    public void sendMessage(String sender, String message) throws IOException {
        if(sender!=null) {
            out.writeUTF(String.format("%s %s %s", Error.CLIENT_MSG_CMD_PREFIX.getText(), sender, message));
        }else{
            out.writeUTF(String.format("%s %s", Error.SERVER_MSG_CMD_PREFIX.getText(),  message));
        }
    }

    public String getUsername (){
        return username;
    }

    public void sendAddUserOnlineList (List<ClientHandler> clients) throws IOException {
            for(ClientHandler client : clients)
            out.writeUTF(String.format("%s %s", Error.SERVER_ADD_USER_ONLINE_PREFIX.getText(), client.getUsername()));

    }

    public void sendRemoveUser(ClientHandler client) throws IOException {
            out.writeUTF(String.format("%s %s", Error.SERVER_REMOVE_USER_ONLINE_PREFIX.getText(), client.getUsername()));
    }

    public void sendNewUsername(ClientHandler client, String username) throws IOException {
        out.writeUTF(String.format("%s %s %s", Error.SERVER_CHANGE_USERNAME_PREFIX.getText(),client.getUsername(), username));
        myServer.broadcastMessage(String.format(">>> %s сменил ник на %s", client.getUsername(),username),this,true);
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }
}
