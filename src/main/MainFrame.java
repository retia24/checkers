package main;

import pieces.PieceColor;
import players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * JFrame-et megvalósító MainFrame osztály. Tárolja az aktuális játékot, illetve a menüt a játék vezérléséhez.
 */
public class MainFrame extends JFrame {

    private Game game;
    private JMenuBar menuBar;
    private JMenu gameMenu;

    private static final int size = 800;

    /**
     * Alapértelmezett konstruktor, amely meghívja az ősét a kapott String címmel.
     * Beállítja a frame méretét, illetve az ablak közepére helyezi a frame-et.
     * A leállításhoz egy inner class-t állít be, amely WindowAdapter-től származik.
     * Létrehoz egy üres Game-et, illetve egy menüt, amelyben választhatunk, hogy új Game-et hozunk létre, mentünk, vagy töltünk be.
     * Ehhez az actionListener-eken belül saját függvényeket használ.
     *
     * @param title
     */
    public MainFrame(String title) {
        super(title);

        getContentPane().setBackground(Color.BLACK);
        setMinimumSize(new Dimension(size, size));
        setSize(size, size);
        setResizable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new CheckersWindowAdapter());

        game = new Game();

        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");

        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> {
            try {
                startNewGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem saveGameMenuItem = new JMenuItem("Save Game");
        saveGameMenuItem.addActionListener(e -> saveCurrentGame());

        JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(e -> {
            try {
                loadGame();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        gameMenu.add(newGameMenuItem);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);

        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Inner class, amely a WindowAdaptertől öröklődik.
     * Bezárás esetén megkérdezi a felhasználót, hogy valóban be akarja-e zárni az ablakot.
     * Ha igen, akkor bezárja az ablakot, ha nem, akkor nem csinál semmit.
     */
    private static class CheckersWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int result = JOptionPane.showConfirmDialog(
                    e.getWindow(),
                    "Do you really want to close the application?\nAll unsaved data will be lost!",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                System.out.println("Window is closing...");
                e.getWindow().dispose();
            } else {
                System.out.println("Window close canceled.");
            }
        }
    }

    /**
     * Új játékot indít, ehhez bekéri a player neveket, majd törli a meglévő elemeket.
     * A game-be létrehoz egy új Game-et, a megfelelő adatokkal.
     * A bábukhoz betölti a képeket, majd frissíti az egész frame-et, hogy minden megjelenjen.
     *
     * @throws IOException A Game létrehozása dobhatja.
     * @throws ClassNotFoundException A Game létrehozása dobhatja.
     */
    private void startNewGame() throws IOException, ClassNotFoundException {
        String[] playerNames = requestPlayerNames(this);

        if (playerNames != null) {
            clearFrame();
            game = new Game(this, new Player(playerNames[0], PieceColor.WHITE), new Player(playerNames[1], PieceColor.BLACK)); // Create a new game
            game.loadBoardImages();
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Ha a game létezik és nem ért még véget, akkor elmenti a játék állapotát:
     *  - a board 2D pieces tömbjét a game.txt fájlba.
     *  - az outputArea-ból a játékosok 2 elemű tömbjét a players.txt fájlba.
     * Ha a game véget ért vagy nem létezik game, akkor a megfelelő hibaüzenetet írja ki a felhasználónak.
     */
    private void saveCurrentGame() {
        if (game != null && !game.isGameOver()) {
            game.save("game.txt", "players.txt");
            JOptionPane.showMessageDialog(this, "Game saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (game != null && game.isGameOver()) {
            JOptionPane.showMessageDialog(this, "Game can't be saved! Game is already won.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(this, "No game to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Betölti a legutóbb elmentett játékot.
     * Ha nem létezik valamelyik fájl, akkor hibaüzenetet dob a felhasználónak.
     * Egyébként először törli a meglévő elemeket a frame-ben, majd betölti a playereket, és ez alapján létrehozza a game-et.
     * A bábukhoz betölti a képeket, majd frissíti az egész frame-et, hogy minden megjelenjen.
     *
     * @throws IOException A Game betöltése dobhatja.
     * @throws ClassNotFoundException A Game betöltése dobhatja.
     */
    private void loadGame() throws IOException, ClassNotFoundException {
        File f1 = new File("game.txt");
        File f2 = new File("players.txt");

        if (!f1.exists() || !f2.exists()) {
            JOptionPane.showMessageDialog(this, "Game file(s) missing.\nCan't load game.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        clearFrame();

        Player[] players = game.loadPlayers("players.txt");
        game = new Game(this, players[0], players[1]);
        game.loadGame("game.txt");
        game.loadBoardImages();

        this.revalidate();
        this.repaint();
        JOptionPane.showMessageDialog(this, "Game loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * A frame összes elemét törli, majd megjeleníti az üres frame-et.
     */
    public void clearFrame() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }

    /**
     * Új játék indítása esetén, a player nevek felhasználótól való bekérésére szolgál.
     * Lérehoz egy új JPanel-t a képernyő közepén, benne JTextFieldekkel.
     * A bekért neveknek nem üresnek, és egymástól különbözőnek kell lenniük,
     * ha az ezek nem teljesülnek, újbóli beírást kér a felhasználótól.
     * Ha cancel-t nyom a felhasználó, akkor null-t ad vissza.
     * Ha sikeres a beírás, visszadja a playerek neveit tartalmazó tömböt.
     *
     * @param frame A frame, amelyen megjelenik.
     * @return Stringek tömbje, mely tartalmazza a playerek nevét.
     */
    private String[] requestPlayerNames(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel player1Label = new JLabel("Player 1 (White Piece):");
        JTextField player1Field = new JTextField();
        JLabel player2Label = new JLabel("Player 2 (Black Piece):");
        JTextField player2Field = new JTextField();

        panel.add(player1Label);
        panel.add(player1Field);
        panel.add(player2Label);
        panel.add(player2Field);

        while (true) {
            int result = JOptionPane.showConfirmDialog(frame, panel, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            String player1 = player1Field.getText().trim();
            String player2 = player2Field.getText().trim();

            if (player1.isEmpty() || player2.isEmpty()) {
                JOptionPane.showMessageDialog(frame,"Both names must be non-empty!","Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else if (player1.equalsIgnoreCase(player2)) {
                JOptionPane.showMessageDialog(frame, "Player names must be different!","Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                return new String[]{player1, player2};
            }
        }
    }
}
