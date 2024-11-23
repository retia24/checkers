package main;

import pieces.Piece;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class BoardRenderer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Board board;

    public BoardRenderer(Board board) {
        this.board = board;
    }

    public void render(Graphics2D g2) {
        drawBoard(g2);
        drawHighlights(g2);
        drawPieces(g2);
    }

    private void drawBoard(Graphics2D g2) {
        int rows = board.getRows();
        int cols = board.getCols();
        int tileSize = board.getRowandcolsize();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Color color = board.colorOfField(row, col) == FieldColor.LIGHT ? new Color(179, 179, 179) : new Color(78, 78, 78);
                g2.setColor(color);
                g2.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawPieces(Graphics2D g2) {
        Piece[][] pieces = board.getPieces();

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null) pieces[row][col].paint(g2);
            }
        }
    }

    private void drawHighlights(Graphics2D g2) {
        Piece selectedPiece = board.getSelectedPiece();
        if (selectedPiece != null) {
            List<Move> normalMoves = board.getGameLogic().updateListOfNormalMoves(board.getPieces(), selectedPiece, board.getCurrentPlayer());
            List<Capture> captures = board.getGameLogic().updateListOfCaptures(board.getPieces(), selectedPiece, board.getCurrentPlayer());
            int tileSize = board.getRowandcolsize();

            // Draw normal move highlights
            for (Move move : normalMoves) {
                g2.setColor(new Color(0, 158, 255));
                g2.fillRect(move.getCol() * tileSize, move.getRow() * tileSize, tileSize, tileSize);
            }

            // Draw capture highlights
            for (Move capture : captures) {
                g2.setColor(new Color(255, 104, 0));
                g2.fillRect(capture.getCol() * tileSize, capture.getRow() * tileSize, tileSize, tileSize);
            }
        }

        if (selectedPiece != null) {
            List<Move> listOfMoves = board.getGameLogic().updateListOfNormalMoves(board.getPieces(), selectedPiece, board.getCurrentPlayer());
            List<Capture> listOfCaptures = board.getGameLogic().updateListOfCaptures(board.getPieces(), selectedPiece, board.getCurrentPlayer());
            int tileSize = board.getRowandcolsize();

            if (!board.getCaptureLock()) {
                for (Move move : listOfMoves) {
                    g2.setColor(new Color(0, 158, 255));
                    g2.fillRect(move.getCol() * tileSize, move.getRow() * tileSize, tileSize, tileSize);
                }
            }
            for (Move move : listOfCaptures) {
                g2.setColor(new Color(255, 104, 0));
                g2.fillRect(move.getCol() * tileSize, move.getRow() * tileSize, tileSize, tileSize);
            }
        }
    }
}

