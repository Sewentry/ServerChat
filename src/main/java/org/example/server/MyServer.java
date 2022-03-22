package org.example.server;

import org.example.server.authentication.AuthenticationService;
import org.example.server.authentication.BaseAuthentication;
import org.example.server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final List<ClientHandler> clients;


    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new BaseAuthentication();
        clients = new ArrayList<>();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public synchronized void unSubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }


    public void start(){
        System.out.println("Сервер запущен");
        System.out.println("----------------");

        try {
            while (true){
                waitAndProcessNewClientConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client: clients) {
            if(client == sender){
                continue;
            }
            client.sendMessage(sender.getUsername(),message);
        }
    }
    public synchronized void broadcastPrivateMessage(String message, ClientHandler sender) throws IOException {
        String[] parts = message.split("\\s+");
        String recipient = parts[1];
        String textMessage = parts[2];
        for (ClientHandler client: clients) {
            if(client.getUsername().equals(recipient)) {
                client.sendPrivateMessage(sender.getUsername(), textMessage);
            }
        }
    }
}
