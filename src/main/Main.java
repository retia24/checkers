package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Checkers");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.BLACK);
        window.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        window.setMinimumSize(new Dimension(800, 800));
        window.setSize(800, 800);
        window.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);

        Board board = new Board();
        c.gridx =0;
        c.gridy =0;
        c.gridwidth = 1;
        c.gridheight = 1;
        window.add(board, c);

        LeaderBoard leaderBoard = new LeaderBoard();
        leaderBoard.setPreferredSize(new Dimension(120, board.getHeightofBoard()));
        leaderBoard.setMinimumSize(new Dimension(120, board.getHeightofBoard()));
        leaderBoard.setMaximumSize(new Dimension(120, board.getHeightofBoard()));
        c.gridx =1;
        c.gridy =0;
        c.gridwidth = 1;
        c.gridheight = 1;
        window.add(leaderBoard, c);

        ActionListener al = new MenuClick();
        JButton b = new JButton("Show Date");
        b.addActionListener(al);
        JMenuItem mi1 = new JMenuItem("Current Date");
        mi1.setActionCommand("date"); // action command beállítása
        mi1.addActionListener(al); // listener hozzáadása
        JMenu m1 = new JMenu("Test");
        m1.add(mi1);
        JMenuBar bar = new JMenuBar();
        bar.add(m1);
        window.setJMenuBar(bar);

        window.setVisible(true);
    }
}