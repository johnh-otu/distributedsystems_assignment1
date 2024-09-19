package org.example.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandlerThread extends Thread {
    
    private Socket clientSocket;

    public ClientHandlerThread (Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    public void run() {
        
        System.out.println("c: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort() 
            + " [" + ServerThread.clientCounter.incrementAndGet() + "]");

        try {
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
            String inputLine;
            while (!(inputLine = in.readLine()).equals("quit")) {
                out.println(inputLine);
            }
        } catch (Exception e) {
            System.err.println("Exception caught in client handler thread while trying to listen for "
                + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());
            System.err.println(e.getMessage());
        }

        System.out.println("d: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort() 
            + " [" + ServerThread.clientCounter.decrementAndGet() + "]");
    }
}
