package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.illumination.LightOn;

import javax.swing.*;
import java.awt.*;

public class RoomGUI extends JFrame {
    private JPanel panel1;
    private JButton lightControlButton;
    private JScrollBar scrollBar1;

    public RoomGUI(Room room) throws HeadlessException {

        setTitle(room.getName());
        setLayout(new FlowLayout());
        setContentPane(panel1);
        setSize(400,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        for(Light l : room.getIllumination().getLights()) {
            add(createLightButton(l));
        }

    }

    public JRadioButton createLightButton(Light l) {

        JRadioButton lightButton = new JRadioButton("Light");
        lightButton.setVisible(true);

        lightButton.addActionListener(e -> {
            l.setLightState(new LightOn(l));
            lightButton.setEnabled(true);
        });

        return lightButton;
    }

}
