package main;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JFrame window = new MainFrame("Checkers");
        window.setVisible(true);
    }
}