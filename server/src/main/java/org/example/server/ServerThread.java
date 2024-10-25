package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.utilities.ServerMessage;

public class ServerThread extends Thread{
    
    public static AtomicInteger clientCounter = new AtomicInteger(20);
    public final int Port;
    private ServerSocket serverSocket;
    public RoomManager roomManager;

    public ServerThread(int port) throws IOException {
        this.Port = port;
        serverSocket = new ServerSocket(port);
        roomManager = new RoomManager();
    }

    public void run() {
        
        try {

            while(true) {
                Socket clientSocket = serverSocket.accept();
                ClientStub client = new ClientStub(clientSocket);
                
                clientCounter.incrementAndGet();
                client.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Connected successfully!"));
                client.sendRooms(roomManager);
                new ClientHandlerThread(client, roomManager).start();
            }
        } catch (IOException e) {
            System.err.println("I/O Exception in server thread while listening on port " + Port + ":\n" + e.getMessage());
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

}
