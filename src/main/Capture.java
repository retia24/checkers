package main;

import pieces.Piece;

import java.io.Serializable;

public class Capture extends Move implements Serializable {
    private static final long serialVersionUID = 1L;

    private Piece capturedPiece;

    public Capture(int row, int col, Piece c) {
        super(row, col);
        capturedPiece = c;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
}
