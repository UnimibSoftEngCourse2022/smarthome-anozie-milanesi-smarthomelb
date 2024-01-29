package org.smarthome.gui;

import org.smarthome.domain.SmartHome;

import javax.swing.*;


public class SmartHomeGUI extends JFrame {
    private JPanel panel1;
    private JButton room1;
    private JButton room2;
    private SmartHome home;

    public SmartHomeGUI() {

        setContentPane(panel1);
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        room1.addActionListener(e ->  {
            new RoomGUI().setVisible(true);
        });

        room2.addActionListener(e ->  {
            new RoomGUI().setVisible(true);
        });

    }

    public static void main(String[] args) {
        new SmartHomeGUI();

    }
}
