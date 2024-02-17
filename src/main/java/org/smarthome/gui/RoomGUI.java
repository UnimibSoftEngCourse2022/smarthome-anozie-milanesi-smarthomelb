package org.smarthome.gui;

import org.smarthome.controller.IlluminationControl;
import org.smarthome.controller.TemperatureControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.temperature.*;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.AutomationListener;

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
    private JButton increaseTemperatureButton;
    private JButton decreaseTemperatureButton;
    private JLabel idealTemperatureSetting;
    private JLabel thresholdTemperatureSetting;
    private JLabel automaticTemperatureSetting;
    private JButton activateAirConditionerButton;
    private JLabel airConditionerState;
    private JLabel airConditionerTemperature;
    private JButton activateAutomaticTemperatureButton;

    public RoomGUI(Room room) throws HeadlessException {
        setContentPane(mainPanel);
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMenu(room);
        initIllumination(room);
        initAutomaticIllumination(room.getIlluminationControl());
        initTemperatureSettings(room.getTemperaturePreference(), room.getTemperatureControl());
        initAirConditioner(room.getAirConditioner(), room.getTemperatureControl());
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

    private void setMenu(Room room) {

        JMenu simulation = new JMenu("Simulation");
        JMenu changeTemperatureSettings = new JMenu("Change Temperature Settings");

        JMenuBar menuBar = new JMenuBar();

        JMenuItem presenceSimulation = new JMenuItem("Presence Simulation");

        JMenuItem temperatureSimulation = new JMenuItem("Temperature Simulation");

        simulation.add(presenceSimulation);
        simulation.add(temperatureSimulation);

        menuBar.add(simulation);
        menuBar.add(changeTemperatureSettings);

        setJMenuBar(menuBar);
    }

    private void initTemperatureSettings(TemperaturePreference temperaturePreference,TemperatureControl temperatureControl) {
        setIdealTemperatureSetting(temperaturePreference);
        setThresholdTemperatureSetting(temperaturePreference);
        setAutomaticTemperatureSetting(temperatureControl);

        temperatureControl.addObserver(setAutomatonListener(temperatureControl));

        activateAutomaticTemperatureButton.addActionListener(e -> {
            temperatureControl.setAutomationActive(!temperatureControl.isAutomationActive());
        });
    }

    private AutomationListener setAutomatonListener(TemperatureControl temperatureControl) {
        return automationActive -> setAutomaticTemperatureSetting(temperatureControl);
    }

    private void setIdealTemperatureSetting(TemperaturePreference temperaturePreference) {
        idealTemperatureSetting.setText(String.valueOf(temperaturePreference.getIdealTemperature()));
    }

    private void setThresholdTemperatureSetting(TemperaturePreference temperaturePreference) {
        thresholdTemperatureSetting.setText(String.valueOf(temperaturePreference.getThreshold()));
    }

    private void setAutomaticTemperatureSetting(TemperatureControl temperatureControl) {
        if(temperatureControl.isAutomationActive()) {
            automaticTemperatureSetting.setText("ON");
        } else {
            automaticTemperatureSetting.setText("OFF");
        }
    }

    private void initAirConditioner(AirConditioner airConditioner, TemperatureControl temperatureControl) {
        if(airConditioner != null) {
            setAirConditionerState(airConditioner.getAirConditionerState());
            setAirConditionerTemperature(airConditioner.getTemperature());

            airConditioner.addObserver(setAirConditionerListener());

            activateAirConditionerButton.addActionListener(e -> {
                temperatureControl.handleAirConditioner();
            });

            increaseTemperatureButton.addActionListener(e -> {
                temperatureControl.increaseTemperature();
            });

            decreaseTemperatureButton.addActionListener(e -> {
                temperatureControl.decreaseTemperature();
            });
        } else {
            airConditionerState.setText("NULL");
            setAirConditionerTemperature(0);
        }
    }
    private void setAirConditionerState(AirConditionerState state) {
        if (state.getClass().equals(AirConditionerOn.class)) {
            airConditionerState.setText("ON");
        } else {
            airConditionerState.setText("OFF");
        }
    }

    private void setAirConditionerTemperature(int temperature) {
        airConditionerTemperature.setText(String.valueOf(temperature));
    }

    private AirConditionerListener setAirConditionerListener() {
        return new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                setAirConditionerState(state);
            }

            @Override
            public void onTemperatureChange(int temperature) {
                setAirConditionerTemperature(temperature);
            }
        };
    }

}
