package logic;

import board.Board;
import pieces.Piece;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * A egérrel való bemenetet kezeli. A bemenet alapján hívja meg a board és gameLogic objektumjainak megfelelő metódusait.
 */
public class CursorInput implements MouseListener, MouseMotionListener {

    private Board board; // A tábla.
    private GameLogic gameLogic; // A játékhoz használt logika.

    public CursorInput(Board b, GameLogic g) {
        board = b;
        gameLogic = g;
    }

    /**
     * A kapott event alapján, a kurzor pozíciója alapján megállapítja azt a bábut amelyre rányomott a felhasználó.
     * Ha van már kiválasztott bábu, akkor nem csinál semmit. Ez azért szükséges, hogy ütéskényszer esetén csak az ütő bábuval lehessen lépni.
     * Ha nincs még kiválasztott bábu, akkor beállítja az éppen választott bábut annak.
     *
     * @param e Az event.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.getRowandcolsize();
        int row = e.getY() / board.getRowandcolsize();
        Piece pressedPiece = board.getPieces()[row][col];

        if (board.getSelectedPiece() != null) {
            return;
        }

        if (pressedPiece != null) {
            board.setSelectedPiece(pressedPiece);
        }
    }

    /**
     * A kurzor lenyomott mozgatása esetén ha van kiválasztott bábu, akkor mindig a kurzorhoz mozgatja.
     * Az x és y pozíciókból azért vonja ki a mező méretének a felét, hogy a kurzor pont a bábu közepére mutasson.
     * Minden mozgatás esetén újrarajzolja a táblát.
     *
     * @param e Az event.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.getSelectedPiece() != null) {
            board.getSelectedPiece().setxPos(e.getX() - board.getRowandcolsize() / 2);
            board.getSelectedPiece().setyPos(e.getY() - board.getRowandcolsize() / 2);
            board.repaint();
        }
    }

    /**
     * A kurzor felengedése esetén több lehetséges kimenetel van.
     * Először a kurzor pozíciójából megszerzi az aktuális sort és oszlopot.
     * Ha a kiválasztott elem nem null, ellenőrzi, hogy a kiválasztott elemhez képest milyen lépés az aktuális sor és oszlopba.
     * Ezzel tér vissza az isValidMove.
     * Ha normál lépés, akkor azt elvégzi, és a kiválasztott elemet null-ra állítja.
     * Ha ütés, akkor elvégzi az ütést, viszont csak akkor állítja vissza a kiválasztott elemet, ha nincs több ütés.
     * Ha nem szabályos lépés, akkor visszaállítja a kiválasztott elem pozícióját. Null-ra viszont csak akkor állítja, ha már nincs több ütés.
     * Újrarajzolja a táblát.
     *
     * @param e Az event.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.getRowandcolsize();
        int row = e.getY() / board.getRowandcolsize();

        if (board.getSelectedPiece() != null) {
            switch (gameLogic.isValidMove(board, board.getSelectedPiece(), row, col)) {
                case NormalMove -> {
                    board.makeMove(board.getSelectedPiece(), row, col);
                    board.setSelectedPiece(null);
                }
                case CaptureMove -> {
                    board.makeCapture(board.getSelectedPiece(), row, col);

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



    // A többi kurzor event esetén nem történik semmi.

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
