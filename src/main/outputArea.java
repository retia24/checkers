package main;

import pieces.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class outputArea extends JTextArea implements Serializable {
    private static final long serialVersionUID = 1L;
    private Player[] players = new Player[2];

    public outputArea(LeaderBoard lb, Board b, Player p1, Player p2) {
        super();
        players[0] = p1;
        players[1] = p2;
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setPreferredSize(new Dimension(lb.getLbWidth(), b.getHeightofBoard()/2));
    }

    public Player[] getPlayers() {
        return players;
    }

    public void whiteWon() {
        setText("GAME OVER\n" + players[0].toString() + " WON with " + players[0].getMovecount() + " moves");
    }

    public void blackWon() {
        setText("GAME OVER\n" + players[1].toString() + " WON with " + players[1].getMovecount() + " moves");
    }

    public void whitesTurn() {
        players[0].setCurrentPlayer(true);
        players[1].setCurrentPlayer(false);
        setText(players[0].toString() + "'s Turn (" + players[0].getMovecount() + ")");
    }

    public void blacksTurn() {
        players[0].setCurrentPlayer(false);
        players[1].setCurrentPlayer(true);
        setText(players[1].toString() + "'s Turn (" + players[1].getMovecount() + ")");
    }
}
