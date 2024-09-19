package org.example.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerThread extends Thread{
    
    public static AtomicInteger clientCounter = new AtomicInteger(0);
    public final int Port;
    private ServerSocket serverSocket;

    public ServerThread(int port) throws IOException {
        this.Port = port;
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        
        try {

            while(true) {

                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
    
                if (clientCounter.get() >= Server.MAX_CLIENTS) {
                    System.out.println("Server is busy, rejecting client: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());
                    out.println("Server is busy. Try again later.");
                    clientSocket.close();
                }
                else {
                    out.println("Server connected!");
                    new ClientHandlerThread(clientSocket).start();
                }
    
            }
        } catch (IOException e) {
            System.err.println("I/O Exception in server thread while listening on port " + Port + ":\n" + e.getMessage());
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
