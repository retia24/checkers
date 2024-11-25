package pieces;

import board.Board;

import java.awt.*;
import java.io.Serializable;

/**
 * Absztrakt osztály, amely a bábuk alapvető attribútumait tartalmazza.
 * Szerializálható, mivel a bábuk 2D tömbjét kell tudni elmenteni.
 */
public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int col, row;
    protected int xPos, yPos;
    protected PieceColor color;
    Board board;

    /**
     * Beállítja a boardot amin a bábu megtalálható.
     * @param b Board objektum.
     */
    public Piece(Board b) {
        board = b;
    }

    /**
     * Absztrakt metódus, amit minden leszármazottnak meg kell valósítania.
     * @param g Graphics2D paraméter.
     */
    public abstract void paint(Graphics2D g);

    /**
     * Absztrakt metódus, amit minden leszármazottnak meg kell valósítania.
     */
    public abstract void loadImage();

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public PieceColor getColor() {
        return color;
    }
}
