package org.example.server;

import java.util.Arrays;

import org.example.utilities.ServerMessage;

public class TicTacToeGame {
    
    public enum SquareState {
        X, //guest
        O, //host
        NULL
    }

    private SquareState turn;
    private SquareState[] gameState = new SquareState[9];

    public TicTacToeGame() {
        Arrays.fill(gameState, SquareState.NULL);
        turn = SquareState.O;
    }
    
    public void resetState() {
        gameState = new SquareState[9];
        Arrays.fill(gameState, SquareState.NULL);
    }
    public ServerMessage playMove(SquareState squareState, int square) {
        if (squareState != turn) {
            return new ServerMessage(ServerMessage.Type.INVALID, "Not your turn!");
        }
        if  (square < 0 || square >= gameState.length) {
            return new ServerMessage(ServerMessage.Type.INVALID, "Invalid square location \"" + square + "\".");
        }
        if (gameState[square] != SquareState.NULL) {
            return new ServerMessage(ServerMessage.Type.INVALID, "Square is already full.");
        }
        gameState[square] = squareState;
        if (turn == SquareState.X) {
            turn = SquareState.O;
        }
        else {
            turn = SquareState.X;
        }
        return new ServerMessage(ServerMessage.Type.COMPLETED, "Move completed!");
    }
    public SquareState checkWinner() {
        // Winning combinations
        int[][] winningCombinations = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
        };

        for (int[] combination : winningCombinations) {
            if (gameState[combination[0]] != SquareState.NULL &&
                gameState[combination[0]] == gameState[combination[1]] &&
                gameState[combination[1]] == gameState[combination[2]]) {
                return gameState[combination[0]]; // Return the winner (X or O)
            }
        }

        return SquareState.NULL;
    }
    public boolean isBoardFull() {
        for (SquareState state : gameState) {
            if (state == SquareState.NULL) {
                return false;
            }
        }
        return true;
    }

    public String printState() {
        String str = "";
        for (SquareState squareState : gameState) {
            str += squareState.toString() + ",";
        }
        str.substring(0, str.length() - 1);
        return str;
    }

}
