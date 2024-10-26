package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.example.utilities.ClientMessage;
import org.example.utilities.ServerMessage;

public class ServerStub {
    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ServerStub(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        out = new PrintWriter(this.serverSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
    }

    public InetAddress getInetAddress() {
        return serverSocket.getInetAddress();
    }
    public int getPort() {
        return serverSocket.getPort();
    }
    public void close() throws IOException {
        serverSocket.close();
    }

    public ServerMessage getMessage() throws IOException {
        while (true) {
            String rawInput = in.readLine();
            String[] inputArray = rawInput.split(",", 2);
            ServerMessage.Type type = getMessageTypeFromString(inputArray[0]);
            
            if (type == null) {
                //invalid message, continue looking
            }
            else if (inputArray.length == 2) {
                return new ServerMessage(type, inputArray[1]);
            }
            else {
                return new ServerMessage(type, null);
            }
        }
    }
    private ServerMessage.Type getMessageTypeFromString(String str) {
        switch (str) {
            case "COMPLETED":
                return ServerMessage.Type.COMPLETED;
            case "INVALID":
                return ServerMessage.Type.INVALID;
            case "ERROR":
                return ServerMessage.Type.ERROR;
            case "ROOM_LIST":
                return ServerMessage.Type.ROOM_LIST;
            case "GAME_STATE":
                return ServerMessage.Type.GAME_STATE;
            default:
                return null;
        }
    }

    public void sendMessage(ClientMessage msg)
    {
        String str = getStringFromMessageType(msg.getType());
        str += "," + msg.getContent();
        out.println(str);
    }
    public void sendMessage(String type, String content)
    {
        out.println(type.toUpperCase() + "," + content);
    }

    private String getStringFromMessageType(ClientMessage.Type type) {
        switch (type) {
            case LIST_ROOMS:
                return "LIST_ROOMS";
            case JOIN_ROOM:
                return "JOIN_ROOM";
            case LEAVE_ROOM:
                return "LEAVE_ROOM";
            case MAKE_ROOM:
                return "MAKE_ROOM";
            case PLAY_MOVE:
                return "PLAY_MOVE";
            case QUIT:
                return "QUIT";
            default:
                return "NULL";
        }
    }
}