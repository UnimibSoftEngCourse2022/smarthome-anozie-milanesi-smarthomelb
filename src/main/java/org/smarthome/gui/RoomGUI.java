package org.smarthome.gui;

import org.smarthome.controller.IlluminationControl;
import org.smarthome.controller.TemperatureControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.temperature.*;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.gui.util.ConstantsGUI;
import org.smarthome.gui.util.DialogOpener;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.TemperaturePreferenceListener;

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
    private JLabel idealTemperatureLabel;
    private JLabel thresholdLabel;
    private JLabel automaticTemperatureControlLabel;
    private JButton automaticTemperatureControlButton;
    private JButton increaseTemperatureButton;
    private JButton decreaseTemperatureButton;
    private JButton activateAirConditionerButton;
    private JLabel airConditionerState;
    private JLabel airConditionerTemperature;

    public RoomGUI(Room room) throws HeadlessException {
        setContentPane(mainPanel);
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMenu(room);
        initIllumination(room);
        initAutomaticIllumination(room.getIlluminationControl());
        initTemperatureSettings(room.getTemperatureControl(), room.getTemperaturePreference());
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

    private void initTemperatureSettings(TemperatureControl temperatureControl, TemperaturePreference temperaturePreference) {
        setIdealTemperatureLabelText(temperaturePreference.getIdealTemperature());
        setThresholdLabelText(temperaturePreference.getThreshold());
        setAutomaticTemperatureControlLabelText(temperatureControl.isAutomationActive());

        temperatureControl.addObserver(this::setAutomaticTemperatureControlLabelText);
        temperaturePreference.addObserver(new TemperaturePreferenceListener() {
            @Override
            public void onIdealTemperatureChange(int idealTemperature) {
                setIdealTemperatureLabelText(idealTemperature);
            }

            @Override
            public void onThresholdChange(int threshold) {
                setThresholdLabelText(threshold);
            }

            @Override
            public void onFieldOutOfRangeException(FieldOutOfRangeException e) {
                DialogOpener.openMessageDialog(
                        "Error!",
                        "Temperature out of range",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        automaticTemperatureControlButton.addActionListener(e -> {
            temperatureControl.setAutomationActive(!temperatureControl.isAutomationActive());
        });
    }

    private void initAirConditioner(AirConditioner airConditioner, TemperatureControl temperatureControl) {
        if(airConditioner != null) {
            setAirConditionerStateLabelText(airConditioner.getAirConditionerState());
            setAirConditionerTemperatureLabelText(airConditioner.getTemperature());

            airConditioner.addObserver(new AirConditionerListener() {
                @Override
                public void onChangeState(AirConditionerState state) {
                    setAirConditionerStateLabelText(state);
                }

                @Override
                public void onTemperatureChange(int temperature) {
                    setAirConditionerTemperatureLabelText(temperature);
                }
            });

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
            setAirConditionerTemperatureLabelText(0);
        }
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

    private void setIdealTemperatureLabelText(int idealTemperature) {
        idealTemperatureLabel.setText(String.valueOf(idealTemperature));
    }

    private void setThresholdLabelText(int threshold) {
        thresholdLabel.setText(String.valueOf(threshold));
    }

    private void setAutomaticTemperatureControlLabelText(boolean automaticActive) {
        if(automaticActive) {
            automaticTemperatureControlLabel.setText(ConstantsGUI.ON);
        } else {
            automaticTemperatureControlLabel.setText(ConstantsGUI.OFF);
        }
    }

    private void setAirConditionerStateLabelText(AirConditionerState state) {
        if (state.getClass().equals(AirConditionerOn.class)) {
            airConditionerState.setText(ConstantsGUI.ON);
        } else {
            airConditionerState.setText(ConstantsGUI.OFF);
        }
    }

    private void setAirConditionerTemperatureLabelText(int temperature) {
        airConditionerTemperature.setText(String.valueOf(temperature));
    }

    private void setMenu(Room room) {
        JMenuBar menuBar = new JMenuBar();
        JMenu simulation = new JMenu("Simulation");
        JMenu changeTemperatureSettings = new JMenu("Change Temperature Settings");

        JMenuItem presenceSimulation = new JMenuItem("Presence Simulation");
        JMenuItem temperatureSimulation = new JMenuItem("Temperature Simulation");

        simulation.add(presenceSimulation);
        simulation.add(temperatureSimulation);

        changeTemperatureSettings.add(createChangeIdealTemperatureMenu(room));
        changeTemperatureSettings.add(createChangeThresholdMenu(room));

        menuBar.add(simulation);
        menuBar.add(changeTemperatureSettings);

        setJMenuBar(menuBar);
    }

    private JMenuItem createChangeIdealTemperatureMenu(Room room) {
        JMenuItem changeIdealTemperature = new JMenuItem("Change Ideal Temperature");
        changeIdealTemperature.addActionListener(e -> {
            String str = DialogOpener.openInputDialog(
                    "Ideal temperature",
                    "Change ideal temperature:",
                    JOptionPane.QUESTION_MESSAGE,
                    String.valueOf(room.getTemperaturePreference().getIdealTemperature())
            );

            try{
                int value = Integer.parseInt(str);
                room.getTemperaturePreference().setIdealTemperature(value);
            } catch (NumberFormatException e1) {
                notIntegerDialog();
            }
        });

        return changeIdealTemperature;
    }

    private JMenuItem createChangeThresholdMenu(Room room) {
        JMenuItem changeThreshold = new JMenuItem("Change Threshold Temperature");
        changeThreshold.addActionListener(e -> {
            String str = DialogOpener.openInputDialog(
                    "Temperature Threshold",
                    "Change temperature Threshold:",
                    JOptionPane.QUESTION_MESSAGE,
                    String.valueOf(room.getTemperaturePreference().getThreshold())
            );

            try{
                int value = Integer.parseInt(str);
                room.getTemperaturePreference().setThreshold(value);
            } catch (NumberFormatException e1) {
                notIntegerDialog();
            }
        });

        return changeThreshold;
    }

    private void notIntegerDialog() {
        DialogOpener.openMessageDialog(
                "Error!",
                "The value typed is not an integer value.",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
