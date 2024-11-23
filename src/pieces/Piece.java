package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public abstract class Piece implements Serializable {
    private static final long serialVersionUID = 1L;

    public int col, row;
    public int xPos, yPos;
    protected PieceColor color;
    public String name;
    public int value;
    Board board;

    public Piece(Board b) {
        board = b;
    }

    public abstract void paint(Graphics2D g);

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
