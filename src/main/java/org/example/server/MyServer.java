package org.example.server;

import org.example.server.authentication.AuthenticationService;
import org.example.server.authentication.DBAuthentification;
import org.example.server.handler.ClientHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final List<ClientHandler> clients;
    private File chatHistory = new File("src/main/resources/org/example/history.txt");



    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new DBAuthentification();
        clients = new ArrayList<>();
    }

    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        clients.add(clientHandler);
        for(ClientHandler client : clients)
        client.sendAddUserOnlineList(clients);
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        for(ClientHandler client: clients)
        client.sendRemoveUser(clientHandler);
        clientHandler.sendMessage(null,clientHandler.getUsername()+" отключился");
    }


    public void start() throws IOException {
        System.out.println("Сервер запущен");
        System.out.println("----------------");
        authenticationService.startAuthentication();
        try {
            while (true){
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            authenticationService.endAuthentication();
        }
    }

    private void waitAndProcessNewClientConnection() throws IOException {
        System.out.println("Ожидание клиента");
        Socket socket = serverSocket.accept();
        System.out.println("Клиент подключился");

        processClientConnection(socket);
    }

    private void processClientConnection(Socket socket) throws IOException {
        ClientHandler handler = new ClientHandler(this, socket);
        handler.handler();
    }
    public synchronized boolean isUsernameBusy(String username){
        for(ClientHandler client : clients){
            if(client.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender, boolean isServerMessage) throws IOException {
        for (ClientHandler client: clients) {
            if(client == sender){
                continue;
            }
            client.sendMessage(isServerMessage ? null: sender.getUsername(),message);
        }
    }

    private synchronized void writeMessageInToHistory(String message, String sender) throws IOException {
       try (BufferedWriter writer = new BufferedWriter(new FileWriter(chatHistory,true))) {
           writer.write(String.format("%s: %s",sender,message+"\n"));
           }
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
       broadcastMessage(message,sender,false);
        writeMessageInToHistory(message,sender.getUsername());
    }
    public synchronized void broadcastPrivateMessage(String message, ClientHandler sender) throws IOException {
        String[] parts = message.split("\\s+");
        String recipient = parts[1];
        String textMessage = parts[2];
        for (ClientHandler client: clients) {
            if(client.getUsername().equals(recipient)) {
                client.sendMessage(sender.getUsername(), textMessage);
            }
        }
    }
    public synchronized void changeUsername(String message, ClientHandler sender) throws SQLException, IOException {
        String [] parts = message.split("\\s+");
        String newUsername = parts[1];
        authenticationService.changeUsername(sender.getUsername(), newUsername);
        for (ClientHandler client: clients) {
            client.sendNewUsername(sender, newUsername);
        }
        sender.setUsername(newUsername);
    }
}


