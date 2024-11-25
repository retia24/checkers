package main;

import board.Board;
import logic.CursorInput;
import pieces.Piece;
import players.Player;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * A Game osztály a játék fő komponenseit és logikáját foglalja magába.
 * Tartalmazza a főablakot, a játéktáblát, a ranglistát és egy szövegkimeneti mezőt.
 * Az osztály lehetőséget biztosít a játék inicializálására, mentésére és betöltésére, illetve a játékhoz kapcsolódó felhasználói felület elemeinek kezelésére.
 */
public class Game {

    private JFrame frame;
    private Board board;
    private LeaderBoard leaderBoard;
    private BoardOutput textArea;

    /**
     * Alapértelmezett konstruktor, üres táblát hoz létre.
     */
    public Game() {
        board = new Board();
        leaderBoard = new LeaderBoard(board);
        leaderBoard = leaderBoard.loadLeaderBoard("leaderboard.txt", board);
    }

    /**
     * Konstruktor, amely inicializálja a játékot egy GUI-ablakkal és két játékossal.
     * Beállítja a tábla, a ranglista és a szövegkimeneti mező elrendezését az ablakban.
     *
     * @param f A játékhoz használt főablak JFrame.
     * @param p1 Az első játékos.
     * @param p2 A második játékos.
     * @throws IOException Ha fájlműveletek során hiba történik.
     * @throws ClassNotFoundException Ha a deszerializáláshoz szükséges osztály nem található.
     */
    public Game(JFrame f, Player p1, Player p2) throws IOException, ClassNotFoundException {
        frame = f;

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        board = new Board();
        board.initBoard();
        c.gridx =0;
        c.gridy =0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 0;
        c.weighty = 0;
        frame.add(board, c);

        c.gridx =1;
        c.gridy =0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;

        leaderBoard = new LeaderBoard(board);
        leaderBoard = leaderBoard.loadLeaderBoard("leaderboard.txt", board);
        frame.add(leaderBoard, c);

        c.gridx =1;
        c.gridy =1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;

        textArea = new BoardOutput(leaderBoard, board, p1, p2);
        board.setOutputArea(textArea);
        frame.add(textArea, c);

        CursorInput input = new CursorInput(board, board.getGameLogic());
        board.addMouseListener(input);
        board.addMouseMotionListener(input);

        board.showPlayer();
    }

    /**
     * Betölti a bábuhoz tartozó képeket a játéktáblához.
     */
    public void loadBoardImages() {
        board.loadPieceImages();
    }

    /**
     * Ellenőrzi, hogy véget ért-e a játék.
     * @return Igaz, ha a játék véget ért, különben hamis.
     */
    public boolean isGameOver() {
        return board.isGameWon();
    }

    /**
     * Elmenti a játéktábla és a játékosok állapotát fájlokba.
     *
     * @param gmfilename A játéktábla állapotát tároló fájl neve.
     * @param pnfilename A játékosok adatait tároló fájl neve.
     */
    public void save(String gmfilename, String pnfilename) {
        try (FileOutputStream f = new FileOutputStream(gmfilename);
             ObjectOutputStream out = new ObjectOutputStream(f)) {
            out.writeObject((Piece[][]) board.getPieces());
            System.out.println("Game saved with filename: " + gmfilename);
        } catch (IOException e) {
            System.err.println("Error while saving the game: " + e.getMessage());
            e.printStackTrace();
        }

        try (FileOutputStream f = new FileOutputStream(pnfilename);
             ObjectOutputStream out = new ObjectOutputStream(f)) {
            out.writeObject(textArea.getPlayers());
            System.out.println("Players saved with filename: " + pnfilename);
        } catch (IOException e) {
            System.err.println("Error while saving the players: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Betölti a játéktábla állapotát egy adott fájlból.
     *
     * @param filename A betöltendő fájl neve.
     */
    public void loadGame(String filename) {
        // Load the game state
        try (FileInputStream f2 = new FileInputStream(filename);
             ObjectInputStream o = new ObjectInputStream(f2)) {
             board.setPieces((Piece[][]) o.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Betölti a játékosok adatait egy adott fájlból.
     *
     * @param filename A betöltendő fájl neve.
     * @return A betöltött játékosok tömbje, vagy null hiba esetén.
     */
    public Player[] loadPlayers(String filename) {
        try (FileInputStream f2 = new FileInputStream(filename);
             ObjectInputStream o = new ObjectInputStream(f2)) {
             return ((Player[]) o.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading players: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
