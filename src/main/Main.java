package main;

import javax.swing.*;
import java.io.IOException;

/**
 * Main osztály. Létrehoz egy MainFrame-et, amely tartalmazza a játék komponenseit.
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JFrame window = new MainFrame("Checkers");
        window.setVisible(true);
    }
}