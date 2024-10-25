package org.example.server;

import java.io.IOException;

import org.apache.commons.lang3.NotImplementedException;

import org.example.utilities.ClientMessage;
import org.example.utilities.ServerMessage;

public class ClientHandlerThread extends Thread {

    private enum State {
        DEFAULT,
        GUEST,
        HOST
    }

    private ClientStub client;
    private RoomManager roomManager;
    private IGuestRoom guestRoom;
    private IHostRoom hostRoom;
    private State state = State.DEFAULT;

    public ClientHandlerThread (ClientStub client, RoomManager roomManager) throws IOException {
        this.client = client;
        this.roomManager = roomManager;
    }
    
    public void run() {
        
        System.out.println("c: " + client.getInetAddress().toString() + ":" + client.getPort() 
            + " [" + ServerThread.clientCounter.incrementAndGet() + "]");

        try {
            ClientMessage inputMessage;
            
            while ((inputMessage = client.getMessage()).getType() != ClientMessage.Type.QUIT) {
                // handle input
                switch (inputMessage.getType()) {
                    case JOIN_ROOM:
                        if (state != State.DEFAULT) {
                            client.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "Already in a room."));
                            break;
                        }
                        if (roomManager.hasId(inputMessage.getContent())) {
                            guestRoom = roomManager.getRoom(inputMessage.getContent());
                            if (guestRoom.join(client)) {
                                state = State.GUEST;
                                client.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Room joined successfully."));
                            }
                            else {
                                guestRoom = null;
                                client.sendMessage(new ServerMessage(ServerMessage.Type.ERROR, "Room full."));
                            }
                            break;
                        }
                        else {
                            client.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "Room does not exist."));
                            break;
                        }
                    case LEAVE_ROOM:
                        switch (state) {
                            case GUEST:
                                guestRoom.leaveAsGuest();
                                guestRoom = null;
                                state = State.DEFAULT;
                                break;
                            case HOST:
                                hostRoom.leaveAsHost();
                                hostRoom = null;
                                state = State.DEFAULT;
                                break;
                            default:   
                                client.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "Not in a room."));
                                break;
                        }
                        break;
                    case MAKE_ROOM:
                        if (state != State.DEFAULT) {
                            client.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "Already in a room."));
                            break;
                        }
                        hostRoom = new Room(client, roomManager);
                        if(hostRoom.init(inputMessage.getContent())) {
                            //success
                            state = State.HOST;
                            client.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Room created successfully."));
                        }
                        else {
                            //fail
                            hostRoom = null;
                            client.sendMessage(new ServerMessage(ServerMessage.Type.ERROR, "Room \"" + inputMessage.getContent() + "\" already exists."));
                        }
                    case PLAY_MOVE:
                        client.sendMessage(new ServerMessage(ServerMessage.Type.ERROR, "Not Implemented."));
                        throw new NotImplementedException();
                    case LIST_ROOMS:
                        client.sendRooms(roomManager);
                    default:
                        break;
                }
            }

            //quit
            client.close();

            if (state == State.HOST) {
                hostRoom.leaveAsHost();
            }
            else if (state == State.GUEST) {
                guestRoom.leaveAsGuest();
            }

            System.out.println("d: " + client.getInetAddress().toString() + ":" + client.getPort() 
            + " [" + ServerThread.clientCounter.decrementAndGet() + "]");

        } catch (Exception e) {
            System.err.println("Exception caught in client handler thread while trying to listen for "
                + client.getInetAddress().toString() + ":" + client.getPort());
            System.err.println(e.getMessage());
        }
    }

}
