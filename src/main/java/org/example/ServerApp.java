package org.example;

import org.example.server.MyServer;

import java.io.IOException;

public class ServerApp  {
    private final static int DEFAULT_PORT = 8188;
    public static void main(String[] args) throws IOException {
        new MyServer(DEFAULT_PORT).start();
    }
}
