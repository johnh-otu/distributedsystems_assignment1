package org.example.client;

import org.example.utilities.ServerMessage;

public class ListenerThread extends Thread {
    
    private ServerStub server;

    public ListenerThread(ServerStub server) {
        this.server = server;
    }
    
    public void run() {

        ServerMessage msg;

        while (!Thread.currentThread().isInterrupted()) {

            try { 
                msg = server.getMessage();

                switch (msg.getType()) {
                    case COMPLETED:
                        System.out.println("\u001B[32mServer: " + msg.getContent() + "\u001B[0m");
                        break;
                    case INVALID:
                        System.out.println("\u001B[33mServer: " + msg.getContent() + "\u001B[0m");
                        break;
                    case ERROR:
                        System.out.println("\u001B[31mServer: " + msg.getContent() + "\u001B[0m");
                        break;
                    case ROOM_LIST:
                        printRoomList(msg.getContent());
                        break;
                    case GAME_STATE:
                        printGameState(msg.getContent());
                        break;
                    case SEND_HELP:
                        printHelp(msg.getContent());
                    default:
                        break;
                }

            }
            catch (Exception e) {
                System.err.println("Exception occured while listening for server: " + e.getMessage());
            }
        }
    }

    private void printRoomList(String content) {
        String[] inputArray = content.split(",");
        System.out.println("=== ROOMS LIST ===");
        for (String room : inputArray) {
            System.out.println(room);
        }
    }

    private void printHelp(String content) {
        String[] inputArray = content.split(",");
        System.out.println("=== COMMANDS ===");
        for (String command : inputArray) {
            System.out.println(command);
        }
    }

    private void printGameState(String content) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'printGameState'");
    }
}
