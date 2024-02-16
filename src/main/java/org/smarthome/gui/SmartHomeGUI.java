package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;

import javax.swing.*;
import java.awt.*;

import static org.smarthome.util.Constants.*;


public class SmartHomeGUI extends JFrame {

    private JPanel panel1;

    public SmartHomeGUI(SmartHome home) {
        setContentPane(panel1);
        setLayout(new FlowLayout());
        setTitle("Smart Home");
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initSmartHome(home);
        setVisible(true);
    }

    public void initSmartHome(SmartHome home) {
        for(Room room : home.getRooms()) {
            panel1.add(createRoomButton(room));
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
