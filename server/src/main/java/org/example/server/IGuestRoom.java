package org.example.server;

import java.net.Socket;

public interface IGuestRoom {
    public boolean join(ClientStub client);
    public void leaveAsGuest();
    public boolean playMove();
}
