package players;

import pieces.PieceColor;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    String name;
    int movecount;
    private PieceColor color;
    boolean isCurrentPlayer;

    public Player(String n, PieceColor c) {
        name = n;
        movecount = 1;
        color = c;
        if (color == PieceColor.WHITE) {
            isCurrentPlayer = true;
        }
        else {
            isCurrentPlayer = false;
        }
    }

    public String toString() {
        return name;
    }

    public int getMovecount() {
        return movecount;
    }

    public void incrementMovecount() {
        movecount++;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }
}
