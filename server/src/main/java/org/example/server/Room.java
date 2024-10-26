package org.example.server;

import org.example.server.TicTacToeGame.SquareState;
import org.example.utilities.ServerMessage;

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
            host.sendGameState(game);
            guest.sendGameState(game);
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
        ServerMessage msg = game.playMove(squareState, square);
        
        if (game.checkWinner() == SquareState.O) {
            host.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "YOU WIN!!"));
            host.sendGameState(game);
            guest.sendMessage(new ServerMessage(ServerMessage.Type.ERROR, "YOU LOST!"));
            guest.sendGameState(game);
            host.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            guest.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            game.resetState();
            host.sendGameState(game);
            guest.sendGameState(game);
            return true;
        }
        if (game.checkWinner() == SquareState.X) {
            guest.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "YOU WIN!!"));
            guest.sendGameState(game);
            host.sendMessage(new ServerMessage(ServerMessage.Type.ERROR, "YOU LOST!"));
            host.sendGameState(game);
            guest.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            host.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            game.resetState();
            guest.sendGameState(game);
            host.sendGameState(game);
            return true;
        }
        if (game.isBoardFull()) {
            host.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "IT'S A DRAW!"));
            host.sendGameState(game);
            guest.sendMessage(new ServerMessage(ServerMessage.Type.INVALID, "IT'S A DRAW!"));
            guest.sendGameState(game);
            host.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            guest.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "Starting a new game..."));
            game.resetState();
            host.sendGameState(game);
            guest.sendGameState(game);
            return true;
        }

        switch (squareState) {
            case O: //host
                if (msg.getType() == ServerMessage.Type.COMPLETED) {
                    host.sendGameState(game);
                    guest.sendGameState(game);
                    guest.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "YOUR TURN!"));
                    return true;
                }
                else {
                    host.sendMessage(msg);
                    return false;
                }
            case X: //guest
                if (msg.getType() == ServerMessage.Type.COMPLETED) {
                    guest.sendGameState(game);
                    host.sendGameState(game);
                    host.sendMessage(new ServerMessage(ServerMessage.Type.COMPLETED, "YOUR TURN!"));
                    return true;
                }
                else {
                    guest.sendMessage(msg);
                    return false;
                }
            default:
                return false;
        }
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
