package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Piece {
    public int col, row;
    public int xPos, yPos;
    PieceColor color;
    public String name;
    public int value;
    Board board;

    public Piece(Board b) {
        board = b;
    }

    public abstract void paint(Graphics2D g);
}
