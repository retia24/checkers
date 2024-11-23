package main;

import pieces.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainFrame extends JFrame {

    private static Game game;
    private JMenuBar menuBar;
    private JMenu gameMenu;

    public MainFrame(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setMinimumSize(new Dimension(800, 800));
        setSize(800, 800);
        setResizable(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        game = new Game();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window is closing...");
                dispose();
            }
        });

        // Create a menu bar
        menuBar = new JMenuBar();

        // Create the "Game" menu
        gameMenu = new JMenu("Game");

        // Add the "New Game" menu item
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> {
            try {
                startNewGame(this);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add the "Save Game" menu item
        JMenuItem saveGameMenuItem = new JMenuItem("Save Game");
        saveGameMenuItem.addActionListener(e -> saveCurrentGame(this));

        // Add the "Load Game" menu item
        JMenuItem loadGameMenuItem = new JMenuItem("Load Game");
        loadGameMenuItem.addActionListener(e -> {
            try {
                loadGame(this);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add menu items to the menu
        gameMenu.add(newGameMenuItem);
        gameMenu.add(saveGameMenuItem);
        gameMenu.add(loadGameMenuItem);

        // Add the menu to the menu bar
        menuBar.add(gameMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);
    }

    private static void startNewGame(JFrame frame) throws IOException, ClassNotFoundException {
        String[] playerNames = requestPlayerNames(frame);
        if (playerNames != null) {
            clearFrame(frame);// Clear existing content
            game = new Game(frame, new Player(playerNames[0], PieceColor.WHITE), new Player(playerNames[1], PieceColor.BLACK)); // Create a new game
            game.loadBoardImages();
            frame.revalidate(); // Refresh the frame layout
            frame.repaint();    // Repaint the frame
        }
    }

    private static void saveCurrentGame(JFrame frame) {
        if (game != null && !game.isGameOver()) {
            game.save("game.txt", "leaderboard.txt", "playernames.txt");
            JOptionPane.showMessageDialog(frame, "Game saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (game != null && game.isGameOver()) {
            JOptionPane.showMessageDialog(frame, "Game can't be saved! Game is already won.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(frame, "No game to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void loadGame(JFrame frame) throws IOException, ClassNotFoundException {
        Player[] players = game.loadPlayers("playernames.txt");
        clearFrame(frame);
        game = new Game(frame, players[0], players[1]);
        game.loadGame("game.txt");
        game.loadLeaderBoard("leaderboard.txt");
        game.loadBoardImages();
        frame.revalidate();
        frame.repaint();
        JOptionPane.showMessageDialog(frame, "Game loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void clearFrame(JFrame frame) {
        frame.getContentPane().removeAll(); // Remove all components
        frame.revalidate();
        frame.repaint();
    }

    private static String[] requestPlayerNames(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Create labels and text fields for player names
        JLabel player1Label = new JLabel("Player 1 Name:");
        JTextField player1Field = new JTextField();
        JLabel player2Label = new JLabel("Player 2 Name:");
        JTextField player2Field = new JTextField();

        panel.add(player1Label);
        panel.add(player1Field);
        panel.add(player2Label);
        panel.add(player2Field);

        while (true) {
            // Show dialog and get the user's input
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    panel,
                    "Enter Player Names",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // If the user cancels, return null
            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            // Get the entered names
            String player1 = player1Field.getText().trim();
            String player2 = player2Field.getText().trim();

            // Validate input
            if (player1.isEmpty() || player2.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Both names must be non-empty!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            } else if (player1.equalsIgnoreCase(player2)) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Player names must be different!",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            } else {
                // Return valid names
                return new String[]{player1, player2};
            }
        }
    }
}
