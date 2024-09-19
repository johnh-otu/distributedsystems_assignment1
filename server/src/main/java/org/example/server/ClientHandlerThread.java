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
        
        System.out.println(clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());

        try {
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }
        } catch (Exception e) {
            System.out.println("Exception caught when trying to listen on port "
                + clientSocket.getPort() + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
