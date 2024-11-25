package logic;

import java.io.Serializable;

/**
 * Lépések tárolására szolgáló osztály. A célpozíciót tartalmazza.
 */
public class Move {

    private int row; // A célsor.
    private int col; // A céloszlop

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
