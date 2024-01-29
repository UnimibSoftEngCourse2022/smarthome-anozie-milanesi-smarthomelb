package org.smarthome.gui;

import org.smarthome.domain.Room;

import javax.swing.*;
import java.awt.*;

public class RoomGUI extends JFrame {
    private JPanel panel1;
    private JButton lightControlButton;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private Room room;

    public RoomGUI() throws HeadlessException {

        setContentPane(panel1);
        setSize(400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        lightControlButton.addActionListener(e -> {

            radioButton1.doClick();
            radioButton2.doClick();
            radioButton3.doClick();

        });
    }

    public static void main(String[] args) {
        new RoomGUI();
    }
}
