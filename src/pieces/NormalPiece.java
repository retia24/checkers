package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NormalPiece extends Piece {
    Image image;

    public NormalPiece(Board board, int r, int c, PieceColor co) {
        super(board);
        col = c;
        row = r;
        color = co;
        xPos = col * board.getRowandcolsize();
        yPos = row * board.getRowandcolsize();
        try {
            if (color == PieceColor.BLACK) image = ImageIO.read(ClassLoader.getSystemResourceAsStream("blacknormal.png")).getScaledInstance(board.getRowandcolsize(), board.getRowandcolsize(), BufferedImage.SCALE_SMOOTH);
            else image = ImageIO.read(ClassLoader.getSystemResourceAsStream("whitenormal.png")).getScaledInstance(board.getRowandcolsize(), board.getRowandcolsize(), BufferedImage.SCALE_SMOOTH);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics2D g) {
        g.drawImage(image, xPos, yPos, null);
    }
}
