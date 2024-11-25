package board;

import logic.Capture;
import logic.Move;
import pieces.Piece;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * Osztály a játéktábla kirajzolásához.
 */
public class BoardRenderer {

    private final Board board; // A tábla amelyet kirajzol.

    public BoardRenderer(Board board) {
        this.board = board;
    }

    /**
     * Függvény amelyet a tábla hív meg. Összefogja a tábla, a kiemelések, és a bábuk kirajzolását.
     *
     * @param g2 Graphics2D paraméter, amely szükséges a kirajzoláshoz.
     */
    public void render(Graphics2D g2) {
        drawBoard(g2);
        drawHighlights(g2);
        drawPieces(g2);
    }

    /**
     * A tábla kirajzolása. A mezők méretét, illetve a sorok és oszlopok számát a board-tól kapja.
     * A colorOfField segítségével megállapítja az adott mező színét, ez alapján állítja be a színt és rajzolja ki a négyzeteket.
     *
     * @param g2 Graphics2D paraméter, amely szükséges a kirajzoláshoz.
     */
    private void drawBoard(Graphics2D g2) {
        int tileSize = board.getRowandcolsize();

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Color color = board.colorOfField(row, col) == FieldColor.LIGHT ? new Color(179, 179, 179) : new Color(78, 78, 78);
                g2.setColor(color);
                g2.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
            }
        }
    }

    /**
     * A bábukat rajzolja ki a 2D pieces tömbből, ha az adott elem nem null.
     * Amennyiben van kiválasztott bábu, akkor azt csak utoljára, a többi bábu kirajzolása után rajzolja, hogy a többi bábu fölött rajzolódjon ki.
     *
     * @param g2 Graphics2D paraméter, amely szükséges a kirajzoláshoz.
     */
    private void drawPieces(Graphics2D g2) {
        Piece[][] pieces = board.getPieces();
        Piece selectedPiece = board.getSelectedPiece();

        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null && pieces[row][col] != selectedPiece) pieces[row][col].paint(g2);
            }
        }

        if (selectedPiece != null) {
            selectedPiece.paint(g2);
        }
    }

    /**
     * Az elérhető lépéseket és ütéseket rajzolja ki, amennyiben van kiválasztott bábu.
     * A lépéseket és ütéseket az updateListOf... metódusokkal kéri le.
     * A lehetséges lépések kék, a lehetséges ütések narancs színnel lesznek kiemelve.
     *
     * @param g2 Graphics2D paraméter, amely szükséges a kirajzoláshoz.
     */
    private void drawHighlights(Graphics2D g2) {
        Piece selectedPiece = board.getSelectedPiece();
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

