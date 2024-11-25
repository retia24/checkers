package main;

import board.Board;
import players.Player;

import javax.swing.*;
import java.awt.*;

/**
 * A tábla szöveges kimenetelének megjelenítésére szolgáló osztály.
 * Tárolja a 2 játékost, illetve a leaderboardot, hogy győzelem esetén a győztest hozzá tudja adni ahhoz.
 */
public class BoardOutput extends JTextArea {

    private final Player[] players = new Player[2]; // A játékosok tömbje.
    private final LeaderBoard leaderBoard;

    public BoardOutput(LeaderBoard lb, Board b, Player p1, Player p2) {
        super();
        leaderBoard = lb;
        players[0] = p1;
        players[1] = p2;
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setPreferredSize(new Dimension(lb.getLbWidth(), b.getHeightofBoard()/2));

        setFont(new Font("Arial", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 158, 255));
        setMargin(new Insets(10, 10, 10, 10));
        setAlignmentX(CENTER_ALIGNMENT);
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * Fehér győzelem jelzése a kimeneten.
     * Hozzáadja a jelenlegi vagyis győztes játékost a leaderboardhoz, majd frissíti a leaderboardot, hogy megjelenjen a játékos a képernyőn.
     * Elmenti azonnal a leaderboardot fájlba.
     */
    public void whiteWon() {
        setText("GAME OVER\n" + players[0].toString() + " WON with " + players[0].getMovecount() + " moves");
        leaderBoard.addPlayer(getCurrentPlayer(), getCurrentPlayer().getMovecount());
        leaderBoard.updateList();
        leaderBoard.save("leaderboard.txt");
    }

    /**
     * Fekete győzelem jelzése a kimeneten.
     * Hozzáadja a jelenlegi vagyis győztes játékost a leaderboardhoz, majd frissíti a leaderboardot, hogy megjelenjen a játékos a képernyőn.
     * Elmenti azonnal a leaderboardot fájlba.
     */
    public void blackWon() {
        setText("GAME OVER\n" + players[1].toString() + " WON with " + players[1].getMovecount() + " moves");
        leaderBoard.addPlayer(getCurrentPlayer(), getCurrentPlayer().getMovecount());
        leaderBoard.updateList();
        leaderBoard.save("leaderboard.txt");
    }

    /**
     * Fehér következő játékos jelzése a kimeneten, illetve a playerek jelenlegi játékos jelzőinek átállítása.
     */
    public void whitesTurn() {
        players[0].setCurrentPlayer(true);
        players[1].setCurrentPlayer(false);
        setText(players[0].toString() + "'s Turn (" + players[0].getMovecount() + ")");
    }

    /**
     * Fehér következő játékos jelzése a kimeneten, illetve a playerek jelenlegi játékos jelzőinek átállítása.
     */
    public void blacksTurn() {
        players[0].setCurrentPlayer(false);
        players[1].setCurrentPlayer(true);
        setText(players[1].toString() + "'s Turn (" + players[1].getMovecount() + ")");
    }

    /**
     * Jelenlegi játékos getter, a playerek currentPlayer attribútuma alapján.
     * @return Jelenlegi játékos.
     */
    public Player getCurrentPlayer() {
        return players[0].isCurrentPlayer() ? players[0] : players[1];
    }
}

