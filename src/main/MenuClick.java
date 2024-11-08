package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("date".equals(command)) {
            // Display current date when menu item is clicked
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            JOptionPane.showMessageDialog(null, "Current Date: " + currentDate);
        } else {
            // Handle other actions, for example button click (if no specific command)
            JOptionPane.showMessageDialog(null, "Button Clicked!");
        }
    }
}
