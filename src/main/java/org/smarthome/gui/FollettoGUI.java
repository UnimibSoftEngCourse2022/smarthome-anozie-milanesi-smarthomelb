package org.smarthome.gui;

import javax.swing.*;

public class FollettoGUI extends JFrame{
    private JPanel panel1;
    private JButton onButton;
    private JButton offButton;
    private JSpinner spinner1;

    public FollettoGUI() {

        setContentPane(panel1);
        setSize(400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        new FollettoGUI();
    }
}
