package main;

import pieces.Piece;
import pieces.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    private JFrame frame;
    private Board board;
    private LeaderBoard leaderBoard;
    private outputArea textArea;

    Player[] players;

    public Game() {
        board = new Board();
        leaderBoard = new LeaderBoard(board);
        players = new Player[2];
    }

    public Game(JFrame f, Player p1, Player p2) throws IOException, ClassNotFoundException {
        frame = f;

        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        board = new Board();
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

        frame.add(leaderBoard, c);

        c.gridx =1;
        c.gridy =1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;

        textArea = new outputArea(leaderBoard, board, p1, p2);
        board.setOutputArea(textArea);
        frame.add(textArea, c);

        board.showPlayer();
    }

    public void loadBoardImages() {
        board.loadPieceImages();
    }

    public boolean isGameOver() {
        return board.isGameWon();
    }

    public void save(String filename, String lbfilename, String pnfilename) {
        try (FileOutputStream f = new FileOutputStream(lbfilename);
             ObjectOutputStream out = new ObjectOutputStream(f)) {
            out.writeObject(leaderBoard);
            System.out.println("Leaderboard saved with filename: " + lbfilename);
        } catch (IOException e) {
            System.err.println("Error while saving the leaderboard: " + e.getMessage());
            e.printStackTrace();
        }

        try (FileOutputStream f = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(f)) {
            out.writeObject((Piece[][]) board.getPieces());
            System.out.println("Game saved with filename: " + filename);
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

    public void loadLeaderBoard(String lbfilename) {
        try (FileInputStream f2 = new FileInputStream(lbfilename);
             ObjectInputStream o = new ObjectInputStream(f2)) {
             leaderBoard = (LeaderBoard) o.readObject();
             board.setLeaderBoard(leaderBoard);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
