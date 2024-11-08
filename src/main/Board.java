package main;

import pieces.NormalPiece;
import pieces.Piece;
import pieces.PieceColor;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    int rows = 8;
    int cols = 8;
    int rowandcolsize = 60;
    Piece pieces[][] = new Piece[rows][cols];

    public Board() {
        setPreferredSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMinimumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMaximumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setBackground(Color.BLACK);
        initBoard();
    }

    public void initBoard() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < cols; j++) {
                pieces[i][j] = new NormalPiece(this, i, j, PieceColor.BLACK);
            }
        }
        for (int i = rows - 1; i > rows - 3; i--) {
            for (int j = 0; j < cols; j++) {
                pieces[i][j] = new NormalPiece(this, i, j, PieceColor.WHITE);
            }
        }
    }

    public int getHeightofBoard() {
        return cols * rowandcolsize;
    }

    public int getRowandcolsize() {
        return rowandcolsize;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;


        for (int row = 0; row < rows; row++) {
            boolean white = row % 2 == 0;
            for (int col = 0; col < cols; col++) {
                if (white) {
                    g2.setColor(new Color(179, 179, 179));
                    g2.fillRect(col * rowandcolsize, row * rowandcolsize, rowandcolsize, rowandcolsize);
                    white = false;
                }
                else {
                    g2.setColor(new Color(78, 78, 78));
                    g2.fillRect(col * rowandcolsize, row * rowandcolsize, rowandcolsize, rowandcolsize);
                    white = true;
                }
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (pieces[row][col] != null) pieces[row][col].paint(g2);
            }
        }
    }
}
