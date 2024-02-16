package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.illumination.LightOn;

import javax.swing.*;
import java.awt.*;

import static org.smarthome.util.Constants.*;

public class RoomGUI extends JFrame {
    private JButton lightControlButton;
    private JPanel roomPanel;
    private JLabel automaticLightState;
    private JLabel sensorPresenceState;

    public RoomGUI(Room room) throws HeadlessException {
        setContentPane(roomPanel);
        setLayout(new FlowLayout());
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMenu();
        setSensorPresenceState(room);
        setAutomaticLightState(room);
        initIllumination(room);
        setVisible(true);
    }

    public void initIllumination(Room room) {
        for(Light light : room.getIllumination().getLights()) {
            JRadioButton button = createLightButton(light);
            add(button);

            light.addObserver(state ->
                    button.setSelected(state.getClass().equals(LightOn.class)));
        }

        lightControlButton.addActionListener(e -> room.getIlluminationControl().handleIllumination());
    }

    public void setAutomaticLightState(Room room) {
        if(room.getIlluminationControl().isAutomationActive()) {
            automaticLightState.setText("ON");
        }else {
            automaticLightState.setText("OFF");
        }
    }

    public void setSensorPresenceState(Room room) {
        if(room.getPresenceSensor() != null && room.getPresenceSensor().monitor()){
            sensorPresenceState.setText("ON");
        }else {
            sensorPresenceState.setText("OFF");
        }
    }

    public void setMenu() {
        JMenu simulation = new JMenu("Simulation");
        JMenu temperature = new JMenu("Temperature");

        JMenuBar menuBar = new JMenuBar();

        JMenuItem presenceSimulation = new JMenuItem("Presence Simulation");

        JMenuItem temperatureSimulation = new JMenuItem("Temperature Simulation");

        simulation.add(presenceSimulation);
        simulation.add(temperatureSimulation);

        menuBar.add(simulation);
        menuBar.add(temperature);

        setJMenuBar(menuBar);
    }

    private JRadioButton createLightButton(Light light) {
        JRadioButton lightButton = new JRadioButton("Light");
        if (light.isOn()) {
            lightButton.setSelected(true);
        }

        lightButton.addActionListener(e -> light.handle());

        lightButton.setVisible(true);
        return lightButton;
    }

}
