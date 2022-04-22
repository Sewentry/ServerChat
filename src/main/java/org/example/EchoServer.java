package org.example;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Scanner;

public class EchoServer {
    private static final int SERVER_PORT = 8186;
    private static DataInputStream in;
    private static DataOutputStream out;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            while (true) {
                System.out.println("Ожидание подключения...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Соединение установлено!");


                in = new DataInputStream(clientSocket.getInputStream());
                out = new  DataOutputStream(clientSocket.getOutputStream());
                Thread t1 = new Thread(()->
                {
                    try {
                        while (true) {
                            Scanner scanner = new Scanner(System.in);
                            String serverMessage = scanner.nextLine();
                            if (!serverMessage.isBlank()) {
                                System.out.println("Сообщение от сервера:" + serverMessage);
                                out.writeUTF(serverMessage);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                t1.setDaemon(true);
                t1.start();
                try {
                    while (true) {

                        String message = in.readUTF();

                        if (message.equals("/server-stop")) {
                            System.out.println("Сервер остановлен");
                            System.exit(0);
                        }
                        else{
                            System.out.println("Клиент:" + message);
                        }
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                    clientSocket.close();
                    System.out.println("Клиент отключился!");
                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
