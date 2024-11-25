package board;

import logic.Capture;
import logic.GameLogic;
import logic.Move;
import main.*;
import pieces.NormalPiece;
import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * A játék tábla osztálya, amely a játék teljes logikáját és megjelenítését kezeli.
 * Tárolja a bábukat, a játék állapotát, és a lépések végrehajtását biztosítja.
 */
public class Board extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int rows = 8; // A tábla sorainak száma
    private final int cols = 8; // A tábla oszlopainak száma
    private final int rowandcolsize = 75; // Egy cella mérete pixelben

    private Piece[][] pieces; // A bábukat tartalmazó 2D tömb
    private Piece selectedPiece = null; // Az éppen kiválasztott bábu

    private final GameLogic gameLogic; // A játék logikáját kezeli
    private List<Move> listOfMoves; // Az összes lehetséges lépés listája a kiválasztott bábuval
    private List<Capture> listOfCaptures; // Az összes lehetséges ütés listája a kiválasztott bábuval
    private boolean captureLock = false; // Zárolás jelzésére, ha kötelezően elvégzendő ütés van

    private PieceColor currentPlayer; // A jelenlegi játékos színe
    private transient BoardOutput outputArea; // A játék állapotát megjelenítő szövegterület

    private boolean gameWon; // Jelzi, hogy véget ért-e a játék

    private transient final BoardRenderer boardRenderer; // A tábla megjelenítéséért felelős

    /**
     * Alapértelmezett konstruktor, amely inicializálja a tábla paramétereit.
     * Beállítja a méretet, a háttérszínt, és elhelyezi a bábukat.
     */
    public Board() {
        pieces = new Piece[rows][cols];
        listOfMoves = new ArrayList<>();
        listOfCaptures = new ArrayList<>();
        currentPlayer = PieceColor.WHITE;
        gameLogic = new GameLogic();
        boardRenderer = new BoardRenderer(this);

        gameWon = false;

        setPreferredSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMinimumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setMaximumSize(new Dimension(cols * rowandcolsize, rows * rowandcolsize));
        setBackground(Color.BLACK);
    }

    /**
     * Inicializálja a táblát. az első 3 és utolsó 3 sorba rakja a bábukat, a setupPieces() segítségével.
     */
    public void initBoard() {
        setupPieces(PieceColor.BLACK, 0, 3);
        setupPieces(PieceColor.WHITE, rows - 3, rows);
    }

    /**
     * Kitölti null-al a bábuk tárolásához használt 2D tömböt.
     */
    public void clearBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pieces[i][j] = null;
            }
        }
    }

    /**
     * A bábukat megadott szín szerint rakja bele a pieces 2D tömbbe.
     * @param color A bábu színe.
     * @param startRow A kezdő sor.
     * @param endRow Az utolsó sor.
     */
    private void setupPieces(PieceColor color, int startRow, int endRow) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < cols; j++) {
                if (colorOfField(i, j) == FieldColor.DARK) {
                    pieces[i][j] = new NormalPiece(this, i, j, color);
                }
            }
        }
    }

    /**
     * Mivel a bábuk képeit nem lehet szerializálni, így szükséges az összes bábu képének újratöltése.
     * Ezt teszi ez a függvény, a 2D pieces tömb összes elemére meghívja a loadImage() függvényt.
     */
    public void loadPieceImages() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pieces[r][c] != null) {
                    pieces[r][c].loadImage();
                }
            }
        }
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setSelectedPiece(Piece piece) {
        selectedPiece = piece;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getHeightofBoard() {
        return cols * rowandcolsize;
    }

    public int getRowandcolsize() {
        return rowandcolsize;
    }

    public List<Capture> getCaptures() {
        return listOfCaptures;
    }

    public boolean getCaptureLock() {
        return captureLock;
    }

    public void setCaptureLock(boolean lock) {
        captureLock = lock;
    }

    public BoardOutput getOutputArea() {
        return outputArea;
    }

    /**
     * Az outputArea beállítása után, a jelenlegi játékos színét állítja be, az outputArea-ban tárolt Player tömb alapján.
     * Megnézi, hogy az 1. játékos a jelenlegi-e, ha igen, akkor a fehér, ha nem, akkor a fekete bábus játékos következik.
     * @param oa Inicializáláshoz szükséges paraméter.
     */
    public void setOutputArea(BoardOutput oa) {
        outputArea = oa;
        if (outputArea.getPlayers()[0].isCurrentPlayer()) {
            currentPlayer = PieceColor.WHITE;
        }
        else {
            currentPlayer = PieceColor.BLACK;
        }
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] p) {
        pieces = p;
    }

    public void setPiece(Piece p, int row, int col) {
        pieces[row][col] = p;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PieceColor c) {
        currentPlayer = c;
    }

    /**
     * A kiválasztott bábu helyzetét visszállítja.
     */
    public void resetSelectedPiecePos() {
        selectedPiece.setxPos(selectedPiece.getCol() * rowandcolsize);
        selectedPiece.setyPos(selectedPiece.getRow() * rowandcolsize);
    }

    /**
     * A kapott ütéseket tartalmazó listában megkeresi azt, amelyikben a célpozíció megegyezik a kapott pozícióval.
     * Az adott ütésből pedig a leütött bábú pozíciója alapján a pieces 2D tömbben törlésre kerül a bábu.
     * @param row A célsor.
     * @param col A céloszlop.
     * @param captureList Az ütések listája.
     */
    private void removeCapturedPiece(int row, int col, List<Capture> captureList) {
        for (Capture m : captureList) {
            if (m.getRow() == row && m.getCol() == col) {
                pieces[m.getCapturedPiece().getRow()][m.getCapturedPiece().getCol()] = null;
            }
        }
    }

    /**
     * Végrehajt egy lépést a táblán, a kapott bábuval, a célpozícióba. A régi helyen kitörli a táblából. Az új helyen visszarakja.
     * A bábu paramétereit az új pozíció szerint frissíti.
     * Ha a bábu normál és eléri a tábla végét, akkor promótálja királynővé.
     * A lépés végén ellenőrzi, hogy véget ért-e a játék, ha nem, átváltja a jelenlegi játékost.
     *
     * @param p A lépést végrehajtó bábu.
     * @param row Az új célpozíció sora.
     * @param col Az új célpozíció oszlopa.
     */
    public void makeMove(Piece p, int row, int col) {
        pieces[p.getRow()][p.getCol()] = null;
        if (p instanceof NormalPiece && (p.getColor() == PieceColor.WHITE && row == 0) || (p.getColor() == PieceColor.BLACK && row == 7)) {
            p = new QueenPiece(this, row, col, p.getColor()); // Promótálás
            p.loadImage();
        }
        p.setRow(row);
        p.setCol(col);
        p.setxPos(col * rowandcolsize);
        p.setyPos(row * rowandcolsize);
        pieces[row][col] = p;
        if (!gameLogic.checkGameOver(pieces, outputArea))
            changePlayer();
        else {
            gameWon = true;
        }
    }

    /**
     * Egy bábu ütését kezeli a táblán. Eltávolítja a leütött bábut a tábláról, és az új helyére helyezi az ütő bábut.
     * Ha a bábu normál és eléri a tábla végét, akkor promótálja királynővé.
     * Az ütés után frissíti az ütésel listáját. Ha ez nem üres, akkor a captureLock aktív lesz, és újra ütésre lesz kényszerítve a játékos.
     * A lépések végén ellenőrzi, hogy véget ért-e a játék, ha nem, átváltja a jelenlegi játékost.
     *
     * @param p Az ütést végrehajtó bábu.
     * @param row Az új célpozíció sora.
     * @param col Az új célpozíció oszlopa.
     */
    public void makeCapture(Piece p, int row, int col) {
        pieces[p.getRow()][p.getCol()] = null;
        listOfCaptures = gameLogic.updateListOfCaptures(pieces, selectedPiece, currentPlayer);
        removeCapturedPiece(row, col, listOfCaptures);
        if (p instanceof NormalPiece && (p.getColor() == PieceColor.WHITE && row == 0) || (p.getColor() == PieceColor.BLACK && row == 7)) {
            p = new QueenPiece(this, row, col, p.getColor());
            p.loadImage();
        }
        p.setRow(row);
        p.setCol(col);
        p.setxPos(col * rowandcolsize);
        p.setyPos(row * rowandcolsize);
        pieces[row][col] = p;
        listOfCaptures = gameLogic.updateListOfCaptures(pieces, selectedPiece, currentPlayer);
        if (listOfCaptures.isEmpty()) {
            if (!gameLogic.checkGameOver(pieces, outputArea))
                changePlayer();
            else {
                gameWon = true;
            }
        }
        else {
            listOfMoves.clear();
            captureLock = true;
        }
    }

    /**
     * A kimeneten megjeleníti, hogy az éppen aktuális játékos következik
     */
    public void showPlayer() {
        if (currentPlayer == PieceColor.WHITE) {
            outputArea.whitesTurn();
        }
        else {
            outputArea.blacksTurn();
        }
    }

    /**
     * Megváltoztatja a jelenlegi játékost. Növeli a volt játékos lépésszámát eggyel. Majd ezt kiírja a kimenetre.
     */
    public void changePlayer() {
        if (currentPlayer == PieceColor.BLACK) {
            currentPlayer = PieceColor.WHITE;
            outputArea.getPlayers()[1].incrementMovecount();
            outputArea.whitesTurn();
        }
        else {
            currentPlayer = PieceColor.BLACK;
            outputArea.getPlayers()[0].incrementMovecount();
            outputArea.blacksTurn();
        }
    }

    /**
     * Kirajzolja a táblát és a rajta levő bábukat.
     *
     * @param g Kirajzoláshoz szükséges paraméter.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        boardRenderer.render(g2);
    }

    /**
     * Segédfüggvény mellyel gyorsan meg lehet határozni egy mező színét, ha a tábla sakktábla szerűen van kirajzolva.
     *
     * @param row A tábla sora.
     * @param col A tábla oszlopa.
     * @return A mező színe.
     */
    public FieldColor colorOfField(int row, int col) {
        return (row + col) % 2 == 0 ? FieldColor.LIGHT : FieldColor.DARK;
    }
}
