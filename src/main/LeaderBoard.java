package main;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LeaderBoard extends JPanel implements Serializable {
    static final long serialVersionUID = 1L;
    HashMap<Player, Integer> LB = new HashMap<>();
    JList<String> jList = new JList<>();
    DefaultListModel<String> listModel = new DefaultListModel<>();
    JFrame frame;

    private static final int lbWidth = 120;

    public LeaderBoard (Board board) {
        setBackground(Color.blue);
        setPreferredSize(new Dimension(lbWidth, board.getHeightofBoard()/2));
        setMinimumSize(new Dimension(lbWidth, board.getHeightofBoard()/2));
        setMaximumSize(new Dimension(lbWidth, board.getHeightofBoard()/2));
        jList.setModel(listModel);
        this.add(new JScrollPane(jList), BorderLayout.NORTH);
    }

    public void updateList() {
        for (Map.Entry<Player, Integer> entry : LB.entrySet()) {
            if (!listModel.contains("Name: " + entry.getKey().toString() + ", Age: " + entry.getValue())) listModel.addElement("Name: " + entry.getKey().toString() + ", Age: " + entry.getValue());
        }
    }

    public boolean containsPlayer(Player player) {
        return LB.containsKey(player);
    }

    public void addPlayer(Player player, int score) {
        LB.put(player, score);
    }

    public int getLbWidth() {
        return lbWidth;
    }
}
