package org.example.server;

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
        game = new TicTacToeGame();
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

    public synchronized boolean playMove(TicTacToeGame.SquareState squareState, int square) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playMove'");
    }
    @Override
    public synchronized boolean playMoveX(int square) {
        return playMove(TicTacToeGame.SquareState.X, square);
    }
    @Override
    public synchronized boolean playMoveO(int square) {
        return playMove(TicTacToeGame.SquareState.O, square);
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

    @Override
    public TicTacToeGame getGame() {
        return game;
    }
}
