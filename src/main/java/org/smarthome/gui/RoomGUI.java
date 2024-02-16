package org.smarthome.gui;

import org.smarthome.controller.IlluminationControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.smarthome.util.Constants.*;

public class RoomGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel lightPanel;
    private JButton lightControlButton;
    private JButton actuateIlluminationButton;
    private JLabel automaticIlluminationLabel;

    public RoomGUI(Room room) throws HeadlessException {
        setContentPane(mainPanel);
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMenu();
        initIllumination(room);
        initAutomaticIllumination(room.getIlluminationControl());
        setVisible(true);
    }

    private void initIllumination(Room room) {
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.Y_AXIS));

        for(Light light : room.getIllumination().getLights()) {
            JRadioButton button = createLightButton(light);
            light.addObserver(state -> button.setSelected(light.isOn()));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            lightPanel.add(button);
        }

        lightControlButton.addActionListener(e -> room.getIlluminationControl().handleIllumination());
    }

    private void initAutomaticIllumination(IlluminationControl illuminationControl) {
        setAutomaticIlluminationLabel(illuminationControl.isAutomationActive());

        illuminationControl.addObserver(this::setAutomaticIlluminationLabel);

        actuateIlluminationButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                illuminationControl.setAutomationActive(
                        !illuminationControl.isAutomationActive());
            }
        });
    }

    private void setMenu() {
        JMenu simulation = new JMenu("Simulation");

        JMenuBar menuBar = new JMenuBar();

        JMenuItem presenceSimulation = new JMenuItem("Presence Simulation");

        JMenuItem temperatureSimulation = new JMenuItem("Temperature Simulation");

        simulation.add(presenceSimulation);
        simulation.add(temperatureSimulation);

        menuBar.add(simulation);

        setJMenuBar(menuBar);
    }

    private JRadioButton createLightButton(Light light) {
        JRadioButton lightButton = new JRadioButton("Light");
        if (light.isOn()) {
            lightButton.setSelected(true);
        }

        lightButton.addActionListener(e -> light.handle());
        return lightButton;
    }

    private void setAutomaticIlluminationLabel(boolean automationActive) {
        automaticIlluminationLabel.setText(String.valueOf(automationActive));
    }

}
