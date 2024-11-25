package test;

import board.Board;
import logic.GameLogic;
import logic.MoveType;
import logic.Move;
import logic.Capture;
import main.BoardOutput;
import main.LeaderBoard;
import pieces.NormalPiece;
import pieces.Piece;
import pieces.PieceColor;
import pieces.QueenPiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import players.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    private GameLogic gameLogic;
    private Board board;
    private BoardOutput mockOutput;

    /**
     * Létrehoz egy táblát és inicializálja is bábukkal.
     */
    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
        board = new Board();
        mockOutput = new BoardOutput(new LeaderBoard(board), board, new Player("Player1", PieceColor.WHITE), new Player("Player2", PieceColor.BLACK));
        board.setOutputArea(mockOutput);
        board.initBoard();
    }

    /**
     * Azt teszteli, hogy egy kiválasztott fehér bábuval egy adott lépés normál lépésnek minősül-e.
     * Ehhez először megnézi, hogy valóban egy nem null, fehér bábu a kiválasztott.
     * Majd megnézi, hogy valóban az elvártat adja-e az isValidMove metódus.
     */
    @Test
    void testIsValidMoveNormalMove() {
        Piece whitePiece = board.getPieces()[5][0];

        assertNotNull(whitePiece);
        assertEquals(PieceColor.WHITE, whitePiece.getColor());

        MoveType moveType = gameLogic.isValidMove(board, whitePiece, 4, 1);
        assertEquals(MoveType.NormalMove, moveType);
    }

    /**
     * Azt teszteli, hogy egy kiválasztott fehér bábuval egy adott lépés ütés lépésnek minősül-e.
     * Ehhez először lerak egy leüthető fekete bábut a táblára, majd megnézi, hogy valóban egy nem null, fehér bábu a kiválasztott.
     * Majd megnézi, hogy valóban az elvártat adja-e az isValidMove metódus.
     */
    @Test
    void testIsValidMoveCaptureMove() {
        board.getPieces()[4][1] = new NormalPiece(board, 4, 1, PieceColor.BLACK);

        Piece whitePiece = board.getPieces()[5][0];
        assertNotNull(whitePiece);
        assertEquals(PieceColor.WHITE, whitePiece.getColor());

        MoveType moveType = gameLogic.isValidMove(board, whitePiece, 3, 2);
        assertEquals(MoveType.CaptureMove, moveType);
    }

    /**
     * Azt teszteli, hogy egy kiválasztott fehér bábuval egy adott lépés illegális lépésnek minősül-e.
     * Ehhez először megnézi, hogy valóban egy nem null, fehér bábu a kiválasztott.
     * Majd megnézi, hogy valóban az elvártat adja-e az isValidMove metódus egy illegális lépésre.
     */
    @Test
    void testIsValidMoveIllegalMove() {
        Piece whitePiece = board.getPieces()[5][0];
        assertNotNull(whitePiece);
        assertEquals(PieceColor.WHITE, whitePiece.getColor());

        MoveType moveType = gameLogic.isValidMove(board, whitePiece, 5, 2);
        assertEquals(MoveType.IllegalMove, moveType);
    }

    /**
     * Az updateListOfNormalMoves metódus tesztje.
     * Azt teszteli, hogy egy fehér, egy fekete, és egy királynő bábuhoz a megfelelő lehetséges lépéseket gyűjti-e össze listába.
     */
    @Test
    void testUpdateListOfNormalMoves() {
        Piece whitePiece = board.getPieces()[5][4]; // Fehér
        assertNotNull(whitePiece);

        List<Move> normalMoves = gameLogic.updateListOfNormalMoves(board.getPieces(), whitePiece, PieceColor.WHITE);

        assertEquals(2, normalMoves.size());
        assertEquals(4, normalMoves.getFirst().getRow());
        assertEquals("(4,3)", normalMoves.get(0).toString());
        assertEquals("(4,5)", normalMoves.get(1).toString());

        Piece blackPiece = board.getPieces()[2][3]; // Fekete
        assertNotNull(blackPiece);

        normalMoves = gameLogic.updateListOfNormalMoves(board.getPieces(), blackPiece, PieceColor.BLACK);

        assertEquals(2, normalMoves.size());
        assertEquals(3, normalMoves.getFirst().getRow());
        assertEquals("(3,2)", normalMoves.get(0).toString());
        assertEquals("(3,4)", normalMoves.get(1).toString());

        board.clearBoard(); // Csak királynő
        Piece queenPiece = new QueenPiece(board, 2, 3, PieceColor.BLACK);
        board.setPiece(queenPiece, 2, 3);

        normalMoves = gameLogic.updateListOfNormalMoves(board.getPieces(), queenPiece, PieceColor.BLACK);

        assertEquals(4, normalMoves.size());
    }

    /**
     * Az updateListOfCaptures metódus tesztje.
     * Azt teszteli, hogy egy fehér, egy fekete, és egy királynő bábuhoz a megfelelő lehetséges ütéseket gyűjti-e össze listába.
     */
    @Test
    void testUpdateListOfCaptures() {
        board.clearBoard();
        Piece whitePiece = new NormalPiece(board, 4, 3, PieceColor.WHITE);
        Piece blackPiece = new NormalPiece(board, 3, 4, PieceColor.BLACK);
        board.setPiece(whitePiece, 4, 3);
        board.setPiece(blackPiece, 3, 4);

        // Fehérhez tartozó ütések
        List<Capture> captures = gameLogic.updateListOfCaptures(board.getPieces(), whitePiece, PieceColor.WHITE);

        assertEquals(1, captures.size());
        assertEquals(2, captures.getFirst().getRow());
        assertEquals(5, captures.getFirst().getCol());
        assertEquals(PieceColor.BLACK, captures.getFirst().getCapturedPiece().getColor());

        // Feketéhez tartozó ütések
        captures = gameLogic.updateListOfCaptures(board.getPieces(), blackPiece, PieceColor.BLACK);

        assertEquals(1, captures.size());
        assertEquals(5, captures.getFirst().getRow());
        assertEquals(2, captures.getFirst().getCol());
        assertEquals(PieceColor.WHITE, captures.getFirst().getCapturedPiece().getColor());

        // Királynőhöz tartozó ütések
        board.clearBoard();
        Piece whiteQueenPiece = new QueenPiece(board, 4, 3, PieceColor.WHITE);
        Piece blackPiece1 = new NormalPiece(board, 3, 2, PieceColor.BLACK);
        Piece blackPiece2 = new NormalPiece(board, 3, 4, PieceColor.BLACK);
        Piece blackPiece3 = new NormalPiece(board, 5, 2, PieceColor.BLACK);
        Piece blackPiece4 = new NormalPiece(board, 5, 4, PieceColor.BLACK);
        board.setPiece(whiteQueenPiece, 4, 3);
        board.setPiece(blackPiece1, 3, 2);
        board.setPiece(blackPiece2, 3, 4);
        board.setPiece(blackPiece3, 5, 2);
        board.setPiece(blackPiece4, 5, 4);

        captures = gameLogic.updateListOfCaptures(board.getPieces(), whiteQueenPiece, PieceColor.WHITE);
        assertEquals(4, captures.size());
    }

    /**
     * Azt ellenőrzi, hogy ha egy félnek nem marad már bábuja vagyis lépése sem, akkor lezárul a játék
     */
    @Test
    void testCheckGameOverWithNoMoves() {
        // Fehérrel
        board.clearBoard();
        board.getPieces()[0][0] = new NormalPiece(board, 0, 0, PieceColor.WHITE);
        board.getPieces()[1][1] = new NormalPiece(board, 1, 1, PieceColor.WHITE);

        boolean gameOver = gameLogic.checkGameOver(board.getPieces(), mockOutput);

        assertTrue(gameOver);

        // Feketével
        board.clearBoard();
        board.getPieces()[0][0] = new NormalPiece(board, 0, 0, PieceColor.BLACK);
        board.getPieces()[1][1] = new NormalPiece(board, 1, 1, PieceColor.BLACK);

        gameOver = gameLogic.checkGameOver(board.getPieces(), mockOutput);

        assertTrue(gameOver);
    }

    /**
     * Ha mindkét félnek van még lehetséges lépése, akkor nem zárja le a játékot.
     */
    @Test
    void testGameOverWithMovesForBoth() {
        boolean gameOver = gameLogic.checkGameOver(board.getPieces(), mockOutput);
        assertFalse(gameOver);
    }
}
