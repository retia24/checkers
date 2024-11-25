package test;

import board.Board;
import main.LeaderBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pieces.PieceColor;
import players.Player;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tesztosztály a LeaderBoard osztályhoz.
 * A tesztek biztosítják, hogy a LeaderBoard megfelelően működik az új játékosok hozzáadásakor és a lista frissítésekor.
 */
class LeaderBoardTest {

    private LeaderBoard leaderBoard;

    /**
     * Tesztkörnyezet inicializálása.
     * Létrehoz egy Board példányt és egy teszt LeaderBoard-ot.
     */
    @BeforeEach
    void setUp() {
        Board board = new Board() {};

        leaderBoard = new LeaderBoard(board);
    }

    /**
     * Új játékos hozzáadása a ranglistához.
     * Ellenőrzi, hogy a játékos megfelelően kerül hozzáadásra és a pontszáma is helyesen rögzül.
     */
    @Test
    void testAddNewPlayer() {
        Player player1 = new Player("Player1", PieceColor.WHITE);

        leaderBoard.addPlayer(player1, 50);

        assertTrue(leaderBoard.getLB().containsKey(player1.toString()));
        assertEquals(50, leaderBoard.getLB().get(player1.toString()));
    }

    /**
     * Már meglévő játékos újra hozzzáadása kisebb score-al.
     * Ellenőrzi, hogy valóban frissíti-e a playerhez tartozó eredményt.
     */
    @Test
    void testAddPlayerLowerScore() {
        Player player1 = new Player("Player1", PieceColor.WHITE);
        leaderBoard.addPlayer(player1, 50);

        leaderBoard.addPlayer(player1, 40);

        assertEquals(40, leaderBoard.getLB().get(player1.toString()));
    }

    /**
     * Már meglévő játékos hozzáadása nagyobb score-al mint volt.
     * Ellenőrzi, hogy valóban nem változik a playerhez tartozó érték.
     */
    @Test
    void testAddPlayer_UpdatePlayerHigherScore() {
        Player player1 = new Player("Player1", PieceColor.WHITE);
        leaderBoard.addPlayer(player1, 50);

        leaderBoard.addPlayer(player1, 60);

        assertEquals(50, leaderBoard.getLB().get(player1.toString()));
    }

    /**
     * Teszteli, hogy üres hashmap esetén a leaderboard is üres lesz.
     */
    @Test
    void testUpdateListWithEmptyLeaderboard() {
        leaderBoard.updateList();

        assertEquals(0, leaderBoard.getListModel().size());
    }

    /**
     * Teszteli, hogy a leaderboard hashmap-hez felvétel után, valóban bekerülnek-e a listmodelbe a playerek a helyes sorrendben.
     */
    @Test
    void testUpdateList_WithPlayers() {
        Player player1 = new Player("Player1", PieceColor.WHITE);
        Player player2 = new Player("Player2", PieceColor.BLACK);
        Player player3 = new Player("Player3", PieceColor.WHITE);

        leaderBoard.addPlayer(player1, 50);
        leaderBoard.addPlayer(player2, 60);
        leaderBoard.addPlayer(player3, 40);

        leaderBoard.updateList();

        assertEquals(3, leaderBoard.getListModel().size());
        assertEquals("Player3, 40", leaderBoard.getListModel().get(0));
        assertEquals("Player1, 50", leaderBoard.getListModel().get(1));
        assertEquals("Player2, 60", leaderBoard.getListModel().get(2));
    }
}

