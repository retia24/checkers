package main;

import board.Board;
import players.Player;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A LeaderBoard osztály a játékosok pontszámainak megjelenítéséért és kezeléséért felelős.
 * Ez az osztály JPanelként működik, így könnyen integrálható a GUI-ba.
 * Lehetőséget nyújt a játékosok listájának frissítésére, új játékosok hozzáadására, illetve a ranglista mentésére és betöltésére fájlokból.
 */
public class LeaderBoard extends JPanel implements Serializable {
    static final long serialVersionUID = 1L;

    HashMap<String, Integer> LB = new HashMap<>(); // A játékosok pontszámainak tárolására szolgáló adatstruktúra.
    JList<String> jList = new JList<>(); // A ranglista megjelenítésére használt GUI komponens.
    DefaultListModel<String> listModel = new DefaultListModel<>(); // A JList modellje, amely a megjelenített adatokat tartalmazza.

    private static final int lbWidth = 120;

    /**
     * Létrehozza a LeaderBoardot.
     * Beállítja a ranglista megjelenítéséhez szükséges elrendezést, panelméretet és alapvető stílusokat.
     * Hozzáadja a listModel-t.
     *
     * @param board A játéktábla referenciaja, amelynek magassága alapján méretezi a ranglistát.
     */
    public LeaderBoard (Board board) {
        super(new BorderLayout());
        setBackground(Color.blue);
        setPreferredSize(new Dimension(lbWidth, board.getHeightofBoard()/2));
        setMinimumSize(new Dimension(lbWidth, board.getHeightofBoard()/2));
        setMaximumSize(new Dimension(lbWidth, board.getHeightofBoard()/2));

        JLabel headerLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(0, 158, 255));
        headerLabel.setForeground(Color.WHITE);

        jList.setModel(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);

        this.add(headerLabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Frissíti a ranglistát a pontszámok alapján.
     * Először törli a meglévő listát, majd a pontszámok szerint növekvő sorrendben rendezi a playereket, és megjeleníti azokat a listában.
     */
    public void updateList() {
        listModel.clear();
        LB.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue())
                .forEach(entry -> {
                    listModel.addElement(entry.getKey() + ", " + entry.getValue());
                });
    }

    /**
     * Ellenőrzi, hogy egy adott játékos szerepel-e a ranglistában.
     *
     * @param player A vizsgált játékos.
     * @return Igaz, ha a játékos már szerepel a ranglistán.
     */
    public boolean containsPlayer(Player player) {
        return LB.containsKey(player.toString());
    }

    /**
     * Új játékost ad hozzá a ranglistához, vagy frissíti a pontszámát, ha már létezik.
     * Ha a játékos már szerepel, csak akkor frissíti a pontszámot, ha az új pontszám kisebb.
     *
     * @param player A hozzáadandó játékos.
     * @param score Az új pontszám.
     */
    public void addPlayer(Player player, int score) {
        if (!containsPlayer(player)) LB.put(player.toString(), score);
        else if (LB.get(player.toString()) > score) LB.put(player.toString(), score);
    }

    public int getLbWidth() {
        return lbWidth;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public HashMap<String, Integer> getLB() {
        return LB;
    }

    /**
     * Elmenti a ranglistát egy fájlba.
     *
     * @param lbfilename A fájl neve, amelybe a ranglistát menti.
     */
    public void save(String lbfilename) {
        try (FileOutputStream f = new FileOutputStream(lbfilename);
             ObjectOutputStream out = new ObjectOutputStream(f)) {
            out.writeObject(this);
            System.out.println("Leaderboard saved with filename: " + lbfilename);
        } catch (IOException e) {
            System.err.println("Error while saving the leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Betölti a ranglistát egy fájlból, vagy új fájlt hoz létre, ha nem létezik.
     *
     * @param lbfilename A fájl neve, amelyből a ranglistát betölti.
     * @param board A játéktábla referenciaja.
     * @return A betöltött vagy újonnan létrehozott LeaderBoard objektum.
     */
    public LeaderBoard loadLeaderBoard(String lbfilename, Board board) {
        File lbFile = new File(lbfilename);

        if (lbFile.exists()) {
            // If the file exists, try loading the leaderboard
            try (FileInputStream f2 = new FileInputStream(lbFile);
                 ObjectInputStream o = new ObjectInputStream(f2)) {
                return (LeaderBoard) o.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading leaderboard: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // If the file doesn't exist, create a new one and initialize the leaderboard
            System.out.println("Leaderboard file not found. Creating a new leaderboard file.");

            // Initialize a new LeaderBoard
            LeaderBoard newLeaderBoard = new LeaderBoard(board);

            // Create and save the new leaderboard file
            try (FileOutputStream f2 = new FileOutputStream(lbfilename);
                 ObjectOutputStream o = new ObjectOutputStream(f2)) {
                o.writeObject(newLeaderBoard);
                System.out.println("New leaderboard file created.");
            } catch (IOException e) {
                System.err.println("Error saving leaderboard: " + e.getMessage());
                e.printStackTrace();
            }

            return newLeaderBoard;  // Return the new leaderboard
        }
        return null;
    }
}
