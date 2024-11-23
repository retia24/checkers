package main;

import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements Serializable {

    public GameLogic() {}

    public MoveType isValidMove(Board board, Piece p, int r, int c) {
        List<Move> listOfMoves = updateListOfNormalMoves(board.getPieces(), p, board.getCurrentPlayer());
        List<Capture> listOfCaptures = updateListOfCaptures(board.getPieces(), p, board.getCurrentPlayer());
        for (Move m : listOfMoves) {
            if (m.getRow() == r && m.getCol() == c) return MoveType.NormalMove;
        }
        for (Move m : listOfCaptures) {
            if (m.getRow() == r && m.getCol() == c) return MoveType.CaptureMove;
        }
        return MoveType.IllegalMove;
    }

    public List<Move> updateListOfNormalMoves(Piece[][] pieces, Piece selectedPiece, PieceColor currentPlayer) {
        List<Move> listOfMoves = new ArrayList<Move>();

        if (selectedPiece == null || selectedPiece.getColor() != currentPlayer) {
            return listOfMoves;
        }

        int row = selectedPiece.getRow();
        int col = selectedPiece.getCol();

        if (selectedPiece.getColor() == PieceColor.WHITE || selectedPiece instanceof QueenPiece) {
            // Top-left diagonal
            if (row - 1 >= 0 && col - 1 >= 0 && pieces[row - 1][col - 1] == null) {
                listOfMoves.add(new Move(row - 1, col - 1));
            }
            // Top-right diagonal
            if (row - 1 >= 0 && col + 1 < 8 && pieces[row - 1][col + 1] == null) {
                listOfMoves.add(new Move(row - 1, col + 1));
            }
        }

        if (selectedPiece.getColor() == PieceColor.BLACK || selectedPiece instanceof QueenPiece) {
            // Bottom-left diagonal
            if (row + 1 < 8 && col - 1 >= 0 && pieces[row + 1][col - 1] == null) {
                listOfMoves.add(new Move(row + 1, col - 1));
            }
            // Bottom-right diagonal
            if (row + 1 < 8 && col + 1 < 8 && pieces[row + 1][col + 1] == null) {
                listOfMoves.add(new Move(row + 1, col + 1));
            }
        }
        return listOfMoves;
    }

    public List<Capture> updateListOfCaptures(Piece[][] pieces, Piece selectedPiece, PieceColor currentPlayer) {
        List<Capture> listOfCaptures = new ArrayList<>();

        if (selectedPiece == null || selectedPiece.getColor() != currentPlayer) {
            return listOfCaptures;
        }

        int row = selectedPiece.getRow();
        int col = selectedPiece.getCol();

        if (selectedPiece.getColor() == PieceColor.WHITE || selectedPiece instanceof QueenPiece) {
            // Top-left capture
            if (row - 2 >= 0 && col - 2 >= 0 && pieces[row - 1][col - 1] != null
                    && pieces[row - 1][col - 1].getColor() != currentPlayer
                    && pieces[row - 2][col - 2] == null) {
                listOfCaptures.add(new Capture(row - 2, col - 2, pieces[row - 1][col - 1]));
            }
            // Top-right capture
            if (row - 2 >= 0 && col + 2 < 8 && pieces[row - 1][col + 1] != null
                    && pieces[row - 1][col + 1].getColor() != currentPlayer
                    && pieces[row - 2][col + 2] == null) {
                listOfCaptures.add(new Capture(row - 2, col + 2, pieces[row - 1][col + 1]));
            }
        }

        if (selectedPiece.getColor() == PieceColor.BLACK || selectedPiece instanceof QueenPiece) {
            // Bottom-left capture
            if (row + 2 < 8 && col - 2 >= 0 && pieces[row + 1][col - 1] != null
                    && pieces[row + 1][col - 1].getColor() != currentPlayer
                    && pieces[row + 2][col - 2] == null) {
                listOfCaptures.add(new Capture(row + 2, col - 2, pieces[row + 1][col - 1]));
            }
            // Bottom-right capture
            if (row + 2 < 8 && col + 2 < 8 && pieces[row + 1][col + 1] != null
                    && pieces[row + 1][col + 1].getColor() != currentPlayer
                    && pieces[row + 2][col + 2] == null) {
                listOfCaptures.add(new Capture(row + 2, col + 2, pieces[row + 1][col + 1]));
            }
        }

        return listOfCaptures;
    }

    public boolean checkGameOver(Piece[][] pieces, outputArea outputArea) {
        boolean whiteHasMoves = false;
        boolean blackHasMoves = false;

        Piece selectedPiece;
        PieceColor currentPlayer;

        outerLoop:
        for (int r = 0; r < pieces.length; r++) {
            for (int c = 0; c < pieces[r].length; c++) {
                if (pieces[r][c] != null) {
                    selectedPiece = pieces[r][c];
                    currentPlayer = selectedPiece.getColor();
                    List<Move> listOfMoves = updateListOfNormalMoves(pieces, selectedPiece, currentPlayer);
                    List<Capture> listOfCaptures = updateListOfCaptures(pieces, selectedPiece, currentPlayer);

                    if (selectedPiece.getColor() == PieceColor.WHITE &&
                            (!listOfMoves.isEmpty() || !listOfCaptures.isEmpty())) {
                        whiteHasMoves = true;
                    }
                    if (selectedPiece.getColor() == PieceColor.BLACK &&
                            (!listOfMoves.isEmpty() || !listOfCaptures.isEmpty())) {
                        blackHasMoves = true;
                    }
                    if (whiteHasMoves && blackHasMoves) {
                        break outerLoop;
                    }
                }
            }
        }
        if (!blackHasMoves) {
            outputArea.whiteWon();
            return true;
        }
        if (!whiteHasMoves) {
            outputArea.blackWon();
            return true;
        }
        return false;
    }


}
