package logic;

import board.Board;
import main.BoardOutput;
import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A játékmenet logikáját megvalósító osztály.
 * Az osztály kezeli a lépések validálását, az ütések kezelését és a játék végének ellenőrzését.
 */
public class GameLogic implements Serializable {

    public GameLogic() {}

    /**
     * Ellenőrzi, hogy egy adott bábuval végrehajtott lépés érvényes-e.
     * Ehhez az updateListOf... metódusok által visszaadott listákat használja..
     * A lépés típusát enumként adja vissza: normál lépés, ütés vagy szabálytalan lépés.
     * Ha a játék már véget ért, minden lépés érvénytelen.
     *
     * @param board A játék táblája.
     * @param p A kiválasztott bábu.
     * @param r A célsor.
     * @param c A céloszlop.
     * @return A lépés típusa (MoveType).
     */
    public MoveType isValidMove(Board board, Piece p, int r, int c) {
        if (checkGameOver(board.getPieces(), board.getOutputArea())) return MoveType.IllegalMove;
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

    /**
     * Frissíti a kiválasztott bábuhoz tartozó lehetséges normál lépések listáját.
     * Ha a kiválasztott bábu null vagy a színe nem egyezik meg a jelenlegi játékossal, akkor üres listát ad vissza.
     * Ha a jelenlegi játékos fehér bábuval van, akkor felfelé, ha feketével, akkor lefelé átlós lépések engedélyezettek.
     * Ha királynő az adott bábu, akkor fel és lefele átlós lépések is.
     * Továbbá az is szükséges, hogy a célmezőben ne szerepeljen bábu.
     *
     * @param pieces A tábla bábujainak 2D tömbje.
     * @param selectedPiece A jelenleg kiválasztott bábu.
     * @param currentPlayer Az aktuális játékos színe.
     * @return A lehetséges normál lépések listája.
     */
    public List<Move> updateListOfNormalMoves(Piece[][] pieces, Piece selectedPiece, PieceColor currentPlayer) {
        List<Move> listOfMoves = new ArrayList<Move>();

        if (selectedPiece == null || selectedPiece.getColor() != currentPlayer) {
            return listOfMoves;
        }

        int row = selectedPiece.getRow();
        int col = selectedPiece.getCol();

        if (selectedPiece.getColor() == PieceColor.WHITE || selectedPiece instanceof QueenPiece) {
            // Bal felső átlós lépés
            if (row - 1 >= 0 && col - 1 >= 0 && pieces[row - 1][col - 1] == null) {
                listOfMoves.add(new Move(row - 1, col - 1));
            }
            // Jobb felső átlós lépés
            if (row - 1 >= 0 && col + 1 < 8 && pieces[row - 1][col + 1] == null) {
                listOfMoves.add(new Move(row - 1, col + 1));
            }
        }

        if (selectedPiece.getColor() == PieceColor.BLACK || selectedPiece instanceof QueenPiece) {
            // Bal alsó átlós lépés
            if (row + 1 < 8 && col - 1 >= 0 && pieces[row + 1][col - 1] == null) {
                listOfMoves.add(new Move(row + 1, col - 1));
            }
            // Jobb alsó átlós lépés
            if (row + 1 < 8 && col + 1 < 8 && pieces[row + 1][col + 1] == null) {
                listOfMoves.add(new Move(row + 1, col + 1));
            }
        }
        return listOfMoves;
    }

    /**
     * Frissíti a kiválasztott bábuhoz tartozó lehetséges ütési lépések listáját.
     * Ha a kiválasztott bábu null vagy a színe nem egyezik meg a jelenlegi játékossal, akkor üres listát ad vissza.
     * Ha a jelenlegi játékos fehér bábuval van, akkor felfelé, ha feketével, akkor lefelé átlós ütések engedélyezettek.
     * Ha királynő az adott bábu, akkor fel és lefele átlós ütések is.
     * Az ütéshez szükséges, hogy a célmező üres legyen, és az útban lévő bábu ellenkező színű legyen.
     *
     * @param pieces A tábla bábujainak 2D tömbje.
     * @param selectedPiece A jelenleg kiválasztott bábu.
     * @param currentPlayer Az aktuális játékos színe.
     * @return A lehetséges ütési lépések listája.
     */
    public List<Capture> updateListOfCaptures(Piece[][] pieces, Piece selectedPiece, PieceColor currentPlayer) {
        List<Capture> listOfCaptures = new ArrayList<>();

        if (selectedPiece == null || selectedPiece.getColor() != currentPlayer) {
            return listOfCaptures;
        }

        int row = selectedPiece.getRow();
        int col = selectedPiece.getCol();

        if (selectedPiece.getColor() == PieceColor.WHITE || selectedPiece instanceof QueenPiece) {
            // Bal felső átlós ütés
            if (row - 2 >= 0 && col - 2 >= 0 && pieces[row - 1][col - 1] != null
                    && pieces[row - 1][col - 1].getColor() != currentPlayer
                    && pieces[row - 2][col - 2] == null) {
                listOfCaptures.add(new Capture(row - 2, col - 2, pieces[row - 1][col - 1]));
            }
            // Jobb felső átlós ütés
            if (row - 2 >= 0 && col + 2 < 8 && pieces[row - 1][col + 1] != null
                    && pieces[row - 1][col + 1].getColor() != currentPlayer
                    && pieces[row - 2][col + 2] == null) {
                listOfCaptures.add(new Capture(row - 2, col + 2, pieces[row - 1][col + 1]));
            }
        }

        if (selectedPiece.getColor() == PieceColor.BLACK || selectedPiece instanceof QueenPiece) {
            // Balra alsó átlós ütés
            if (row + 2 < 8 && col - 2 >= 0 && pieces[row + 1][col - 1] != null
                    && pieces[row + 1][col - 1].getColor() != currentPlayer
                    && pieces[row + 2][col - 2] == null) {
                listOfCaptures.add(new Capture(row + 2, col - 2, pieces[row + 1][col - 1]));
            }
            // Jobbra alsó átlós ütés
            if (row + 2 < 8 && col + 2 < 8 && pieces[row + 1][col + 1] != null
                    && pieces[row + 1][col + 1].getColor() != currentPlayer
                    && pieces[row + 2][col + 2] == null) {
                listOfCaptures.add(new Capture(row + 2, col + 2, pieces[row + 1][col + 1]));
            }
        }

        return listOfCaptures;
    }

    /**
     * Ellenőrzi, hogy a játék véget ért-e.
     * Ehhez végig megy az összes bábun, amelyet kiválasztottnak állít be, és ez alapján frissíti a normál lépések és ütések listáját.
     * A fehér és fekete bábukat külön kezeli. Ha valamelyik színhez nem tartozik egy lépés sem, akkor az ellenkező játékos nyert.
     *
     * @param pieces A tábla bábujainak 2D tömbje.
     * @param outputArea Az eredmény megjelenítéséhez használt objektum.
     * @return Igaz, ha a játék véget ért; hamis, ha nem.
     */
    public boolean checkGameOver(Piece[][] pieces, BoardOutput outputArea) {
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
