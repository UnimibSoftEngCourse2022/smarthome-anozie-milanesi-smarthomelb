package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;

import javax.swing.*;
import java.awt.*;


public class SmartHomeGUI extends JFrame {

    private JPanel panel1;
    private JScrollBar scrollBar1;

    public SmartHomeGUI(SmartHome home) {
        setContentPane(panel1);
        setLayout(new FlowLayout());
        setSize(400, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        for(Room r : home.getRooms()) {
            add(createRoomButton(r));
        }
    }

    public JButton createRoomButton(Room room) {
        JButton roomButton = new JButton("Room");
        roomButton.setVisible(true);

        roomButton.addActionListener(e -> {
            RoomGUI roomGUI = new RoomGUI(room);
        });

        return roomButton;
    }

}
