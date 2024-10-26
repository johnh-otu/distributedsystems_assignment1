package org.example.server;

public interface IGuestRoom {
    public boolean join(ClientStub client);
    public void leaveAsGuest();
    public boolean playMoveX(int square);
    public TicTacToeGame getGame();
}
