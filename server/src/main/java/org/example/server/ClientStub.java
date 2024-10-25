package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.example.utilities.ClientMessage;
import org.example.utilities.ServerMessage;

public class ClientStub {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientStub(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }

    public InetAddress getInetAddress() {
        return clientSocket.getInetAddress();
    }
    public int getPort() {
        return clientSocket.getPort();
    }
    public void close() throws IOException {
        clientSocket.close();
    }

    public ClientMessage getMessage() throws IOException {
        while (true) {
            String rawInput = in.readLine();
            String[] inputArray = rawInput.split(",", 2);
            ClientMessage.Type type = getMessageTypeFromString(inputArray[0]);
            
            if (type == null) {
                var message = new ServerMessage(ServerMessage.Type.INVALID, "Invalid command name \"" + (inputArray.length > 0 ? inputArray[0] : "") + "\"");
                sendMessage(message);
            }
            else if (inputArray.length == 2) {
                return new ClientMessage(type, inputArray[1]);
            }
            else {
                return new ClientMessage(type, null);
            }
        }
    }
    private ClientMessage.Type getMessageTypeFromString(String str) {
        switch (str) {
            case "LIST_ROOMS":
                return ClientMessage.Type.LIST_ROOMS;
            case "JOIN_ROOM":
                return ClientMessage.Type.JOIN_ROOM;
            case "LEAVE_ROOM":
                return ClientMessage.Type.LEAVE_ROOM;
            case "MAKE_ROOM":
                return ClientMessage.Type.MAKE_ROOM;
            case "PLAY_MOVE":
                return ClientMessage.Type.PLAY_MOVE;
            case "QUIT":
                return ClientMessage.Type.QUIT;
            default:
                return null;
        }
    }

    public void sendMessage(ServerMessage msg)
    {
        String str = getStringFromMessageType(msg.getType());
        str += "," + msg.getContent();
        out.print(str);
    }
    private String getStringFromMessageType(ServerMessage.Type type) {
        switch (type) {
            case COMPLETED:
                return "COMPLETED";
            case INVALID:
                return "INVALID";
            case ERROR:
                return "ERROR";
            case ROOM_LIST:
                return "ROOM_LIST";
            case GAME_STATE:
                return "GAME_STATE";
            default:
                return "";
        }
    }

    public void sendRooms(RoomManager manager) {
        var rooms = manager.getMap();
        var msg = "";
        for (String id: rooms.keySet()) {
            msg += id + ",";
        }
        msg = msg.substring(0, msg.length() - 1);
        this.sendMessage(new ServerMessage(ServerMessage.Type.ROOM_LIST, msg));
    }
}
