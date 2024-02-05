package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.illumination.LightOff;
import org.smarthome.domain.illumination.LightOn;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomGUI extends JFrame {

    private JPanel panel1;
    private JButton lightControlButton;
    private List<JRadioButton> lightButtons;

    public RoomGUI(Room room) throws HeadlessException {

        setContentPane(panel1);
        setLayout(new FlowLayout());
        setSize(400,400);
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        lightButtons = new ArrayList<>();

        for(Light light : room.getIllumination().getLights()) {
            panel1.add(createLightButton(light));
        }

        lightControlButton.addActionListener(e -> {
            room.getLightControl().handleIllumination();

            for(JRadioButton lightbutton : lightButtons){
                lightbutton.doClick();
            }

        });
    }

    public JRadioButton createLightButton(Light light) {

        JRadioButton lightButton = new JRadioButton("Light");
        lightButton.setVisible(true);

        lightButton.addActionListener(e -> {

            if(lightButton.isSelected()) {
                light.setLightState(new LightOn(light));
            }
            else {
                light.setLightState(new LightOff(light));
            }

        });

        lightButtons.add(lightButton);

        return lightButton;
    }

}
