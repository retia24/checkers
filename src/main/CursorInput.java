package main;

import pieces.Piece;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

public class CursorInput implements MouseListener, MouseMotionListener, Serializable {
    private static final long serialVersionUID = 1L;

    Board board;
    private GameLogic gameLogic;
    public CursorInput(Board b, GameLogic g) {
        board = b;
        gameLogic = g;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.getRowandcolsize();
        int row = e.getY() / board.getRowandcolsize();
        Piece pressedPiece = board.getPieces()[row][col];

        // Check if the current selected piece must continue capturing
        if (board.getSelectedPiece() != null) {
            // If captures are still available, prevent selecting a new piece
            return;
        }

        // Otherwise, allow selecting a new piece
        if (pressedPiece != null) {
            board.setSelectedPiece(pressedPiece);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.getSelectedPiece() != null) {
            board.getSelectedPiece().setxPos(e.getX() - board.getRowandcolsize() / 2);
            board.getSelectedPiece().setyPos(e.getY() - board.getRowandcolsize() / 2);
            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.getRowandcolsize();
        int row = e.getY() / board.getRowandcolsize();

        if (board.getSelectedPiece() != null) {
            switch (gameLogic.isValidMove(board, board.getSelectedPiece(), row, col)) {
                case NormalMove -> {
                    board.makeMove(board, board.getSelectedPiece(), row, col);
                    board.setSelectedPiece(null);
                }
                case CaptureMove -> {
                    board.makeCapture(board, board.getSelectedPiece(), row, col);

                    // If there are no more captures, allow turn to end
                    if (board.getCaptures().isEmpty()) {
                        board.setSelectedPiece(null);
                        board.setCaptureLock(false);
                    }
                }
                case IllegalMove -> {
                    board.resetSelectedPiecePos();
                    if (!board.getCaptureLock()) {
                        board.setSelectedPiece(null);
                    }
                }
            }
        }

        board.repaint();

    }





    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
