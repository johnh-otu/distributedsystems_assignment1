package org.example.server;

public interface IHostRoom {
    public boolean init(String id);
    public void leaveAsHost();
    public boolean playMoveO(int square);
    public boolean switchPlayers();
    public TicTacToeGame getGame();
}
