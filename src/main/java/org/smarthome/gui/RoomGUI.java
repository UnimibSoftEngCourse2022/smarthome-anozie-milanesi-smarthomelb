package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.illumination.LightOn;

import javax.swing.*;
import java.awt.*;

public class RoomGUI extends JFrame {

    private JPanel panel1;
    private JButton lightControlButton;

    public RoomGUI(Room room) throws HeadlessException {
        setContentPane(panel1);
        setLayout(new FlowLayout());
        setSize(600,600);
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        initIllumination(room);
    }

    public void initIllumination(Room room) {
        for(Light light : room.getIllumination().getLights()) {
            JRadioButton button = createLightButton(light);
            panel1.add(button);

            light.addObserver(state ->
                    button.setSelected(state.getClass().equals(LightOn.class)));
        }

        lightControlButton.addActionListener(e -> {
            room.getIlluminationControl().handleIllumination();
        });
    }

    private JRadioButton createLightButton(Light light) {
        JRadioButton lightButton = new JRadioButton("Light");
        if (light.isOn()) {
            lightButton.setSelected(true);
        }

        lightButton.addActionListener(e -> {
            light.handle();
        });

        lightButton.setVisible(true);
        return lightButton;
    }

}
