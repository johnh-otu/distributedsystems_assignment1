package org.example.server;

public interface IHostRoom {
    public boolean init(String id);
    public void leaveAsHost();
    public boolean playMove();
    public boolean switchPlayers();
}
