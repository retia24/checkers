package logic;

import pieces.Piece;

import java.io.Serializable;

/**
 * Az ütés osztály a lépés osztály leszármazottja.
 * Saját attribútuma a leütött bábu.
 */
public class Capture extends Move implements Serializable {
    private static final long serialVersionUID = 1L;

    private Piece capturedPiece; // A leütött bábú.

    /**
     * Meghíjva a Move osztály konstruktorát.
     * Beállítja a leütött bábut.
     *
     * @param row A célsor.
     * @param col A céloszlop.
     * @param c A leütött bábu.
     */
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
