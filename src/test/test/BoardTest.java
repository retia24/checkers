package test;

import board.Board;
import board.FieldColor;
import logic.Capture;
import main.BoardOutput;
import main.LeaderBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pieces.NormalPiece;
import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;
import players.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A Board osztály testeléséhez használt osztály.
 */
class BoardTest {

    private Board board; // A tábla amelyen a teszteket végzi.
    private BoardOutput mockOutput; // Output, amely nincs megjelenítve, viszont lekérdezhető az állapota.

    /**
     * Tábla létrehozása, output beállítása a playerekkel együtt.
     */
    @BeforeEach
    void setUp() {
        board = new Board();
        mockOutput = new BoardOutput(new LeaderBoard(board), board, new Player("Player1", PieceColor.WHITE), new Player("Player2", PieceColor.BLACK));
        board.setOutputArea(mockOutput);
    }

    /**
     * Teszteli a lépés végrehajtását. Illetve teszteli, hogy az utolsó sorba ért bábu promótálva lett-e.
     */
    @Test
    void testMove() {
        // Arrange
        Piece whitePiece = new NormalPiece(board, 1, 0, PieceColor.WHITE);
        board.setPiece(whitePiece, 1, 0);

        // Act
        board.makeMove(whitePiece, 0, 1); // Move to the last row

        // Assert
        assertInstanceOf(QueenPiece.class, board.getPieces()[0][1]);
        assertNull(board.getPieces()[1][0]);
        assertEquals(0, board.getPieces()[0][1].getRow());
        assertEquals(1, board.getPieces()[0][1].getCol());
    }

    /**
     * Teszteli, hogy ütés esetén a leütött bábu törlésre kerül e a tábláról, illetve az ütő bábu adatai frissülnek-e.
     */
    @Test
    void testCapture() {
        Piece blackPiece = new NormalPiece(board, 4, 3, PieceColor.BLACK);
        Piece whitePiece = new NormalPiece(board, 5, 4, PieceColor.WHITE);

        board.setPiece(blackPiece, 4, 3);
        board.setPiece(whitePiece, 5, 4);

        board.setSelectedPiece(whitePiece);
        List<Capture> captureList = board.getGameLogic().updateListOfCaptures(board.getPieces(), whitePiece, PieceColor.WHITE);

        board.makeCapture(whitePiece, 3, 2);

        assertNull(board.getPieces()[4][3]);
        assertEquals(3, whitePiece.getRow());
        assertEquals(2, whitePiece.getCol());
    }

    /**
     * Az aktuális játékos váltását teszteli oda-vissza.
     */
    @Test
    void testChangePlayer() {
        board.setCurrentPlayer(PieceColor.WHITE);

        board.changePlayer();
        assertEquals(PieceColor.BLACK, board.getCurrentPlayer());

        board.changePlayer();
        assertEquals(PieceColor.WHITE, board.getCurrentPlayer());
    }

    /**
     * A tábla kirajzolásához és lépés validálásához használatos, mező színének eldöntésére szolgáló függvény tesztje.
     */
    @Test
    void testColorOfField() {
        assertEquals(FieldColor.LIGHT, board.colorOfField(0, 0));
        assertEquals(FieldColor.DARK, board.colorOfField(0, 1));
        assertEquals(FieldColor.DARK, board.colorOfField(1, 0));
        assertEquals(FieldColor.LIGHT, board.colorOfField(1, 1));
    }
}

