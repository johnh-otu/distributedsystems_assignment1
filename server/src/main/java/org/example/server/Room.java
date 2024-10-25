package org.example.server;

import java.net.Socket;

import org.apache.commons.lang3.NotImplementedException;

public class Room implements IHostRoom, IGuestRoom{

    private String id;

    private ClientStub host;
    private ClientStub guest;
    private RoomManager manager;
    private TicTacToeGame game;

    public Room(ClientStub host, RoomManager manager) {
        this.host = host;
        this.manager = manager;
    }

    @Override
    public boolean init(String id) {
        this.id = id;
        return manager.addRoom(this, id);
    }

    @Override
    public synchronized boolean join(ClientStub guest) {
        if (this.guest == null) {
            this.guest = guest;
            return true;
        }
        return false;
    }

    @Override
    public void leaveAsGuest() {
        guest = null;
    }

    @Override
    public void leaveAsHost() {
        if (guest != null) { 
            switchPlayers();
            leaveAsGuest();
        }
        else {
            host = null;
            endRoom();
        }
    }

    @Override
    public synchronized boolean playMove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playMove'");
    }

    @Override
    public synchronized boolean switchPlayers() {
        if (host != null && guest != null) {
            ClientStub temp = guest;
            guest = host;
            host = temp;
            return true;
        }
        return false;
    }

    private void endRoom() {
        manager.delRoom(id);
    }
}
