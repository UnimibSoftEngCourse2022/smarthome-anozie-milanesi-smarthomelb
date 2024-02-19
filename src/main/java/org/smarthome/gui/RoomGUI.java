package org.smarthome.gui;

import org.smarthome.controller.IlluminationControl;
import org.smarthome.controller.TemperatureControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.domain.sensor.TemperatureSensor;
import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.AirConditionerOn;
import org.smarthome.domain.temperature.AirConditionerState;
import org.smarthome.domain.temperature.TemperaturePreference;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.gui.util.DialogOpener;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.AutomationListener;
import org.smarthome.listener.SensorListener;
import org.smarthome.listener.TemperaturePreferenceListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
    private JButton backHomeButton;
    private JPanel illuminationPanel;
    private JPanel airConditionerPanel;
    private JPanel automaticIlluminationPanel;
    private JPanel automaticTemperaturePanel;
    private JLabel ambientPresenceLabel;
    private JLabel ambientTemperatureLabel;
    private JLabel presenceLabel;
    private JLabel temperatureLabel;
    private JPanel ambientPanel;

    public static RoomGUI init(Room room) {
        return new RoomGUI(room);
    }

    private RoomGUI(Room room) {
        setContentPane(mainPanel);
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMenu(room);
        initIllumination(room);
        initAirConditioner(room);
        initTemperatureSettings(room);
        initAmbient(room);
        backHomeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SmartHomeGUI.getInstance().setVisible(true);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    private void initIllumination(Room room) {
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.Y_AXIS));

        IlluminationControl illuminationControl = room.getIlluminationControl();
        Illumination illumination = room.getIllumination();
        PresenceSensor presenceSensor = room.getPresenceSensor();

        if (illumination != null) {
            for(Light light : room.getIllumination().getLights()) {
                JRadioButton button = createLightButton(light);
                light.addObserver(state -> button.setSelected(light.isOn()));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                lightPanel.add(button);
            }
            lightControlButton.addActionListener(e -> illuminationControl.handleIllumination());
        } else {
            illuminationPanel.setVisible(false);
        }

        if (presenceSensor != null) {
            setAutomaticIlluminationLabel(illuminationControl.isAutomationActive());
            illuminationControl.addObserver(createAutomationIlluminationControlListener());

            actuateIlluminationButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    illuminationControl.setAutomationActive(
                            !illuminationControl.isAutomationActive());
                }
            });
        } else {
            automaticIlluminationPanel.setVisible(false);
        }
    }

    private void initAirConditioner(Room room) {
        TemperatureControl temperatureControl = room.getTemperatureControl();
        AirConditioner airConditioner = room.getAirConditioner();

        if(airConditioner != null) {
            setAirConditionerStateLabelText(airConditioner.getAirConditionerState());
            setAirConditionerTemperatureLabelText(airConditioner.getTemperature());

            airConditioner.addObserver(createAirConditionerListener());

            activateAirConditionerButton.addActionListener(e ->
                    temperatureControl.handleAirConditioner());

            increaseTemperatureButton.addActionListener(e ->
                    temperatureControl.increaseTemperature());

            decreaseTemperatureButton.addActionListener(e ->
                    temperatureControl.decreaseTemperature());
        } else {
            airConditionerPanel.setVisible(false);
        }
    }

    private void initTemperatureSettings(Room room) {
        TemperatureControl temperatureControl = room.getTemperatureControl();
        TemperaturePreference temperaturePreference = room.getTemperaturePreference();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        setIdealTemperatureLabelText(temperaturePreference.getIdealTemperature());
        setThresholdLabelText(temperaturePreference.getThreshold());

        temperatureControl.addObserver(createAutomationTemperatureControlListener());
        temperaturePreference.addObserver(createTemperaturePreferenceListener());

        if (temperatureSensor != null) {
            setAutomaticTemperatureControlLabelText(temperatureControl.isAutomationActive());
            automaticTemperatureControlButton.addActionListener(e ->
                    temperatureControl.setAutomationActive(!temperatureControl.isAutomationActive()));
        } else {
            automaticTemperaturePanel.setVisible(false);
        }
    }

    private void initAmbient(Room room) {
        PresenceSensor presenceSensor = room.getPresenceSensor();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        if (presenceSensor == null && temperatureSensor == null) {
            ambientPanel.setVisible(false);
        } else {
            if (presenceSensor != null) {
                setAmbientPresenceLabelText(presenceSensor.getData());
                presenceSensor.addObserver(createSensorPresenceListener());
            } else {
                presenceLabel.setVisible(false);
                ambientPresenceLabel.setVisible(false);
            }

            if (temperatureSensor != null) {
                setAmbientTemperatureLabelText(temperatureSensor.getData());
                temperatureSensor.addObserver(createSensorTemperatureListener());
            } else {
                temperatureLabel.setVisible(false);
                ambientTemperatureLabel.setVisible(false);
            }
        }
    }

    private JRadioButton createLightButton(Light light) {
        JRadioButton lightButton = new JRadioButton(light.getName());
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
        idealTemperatureLabel.setText(idealTemperature + "°");
    }

    private void setThresholdLabelText(int threshold) {
        thresholdLabel.setText(String.valueOf(threshold));
    }

    private void setAutomaticTemperatureControlLabelText(boolean automaticActive) {
        if(automaticActive) {
            automaticTemperatureControlLabel.setText("On");
        } else {
            automaticTemperatureControlLabel.setText("Off");
        }
    }

    private void setAirConditionerStateLabelText(AirConditionerState state) {
        if (state.getClass().equals(AirConditionerOn.class)) {
            airConditionerState.setText("On");
        } else {
            airConditionerState.setText("Off");
        }
    }

    private void setAirConditionerTemperatureLabelText(int temperature) {
        airConditionerTemperature.setText(temperature + "°");
    }

    private void setAmbientPresenceLabelText(boolean presence) {
        ambientPresenceLabel.setText(String.valueOf(presence));
    }

    private void setAmbientTemperatureLabelText(int temperature) {
        ambientTemperatureLabel.setText(temperature + "°");
    }

    private void setMenu(Room room) {
        PresenceSensor presenceSensor = room.getPresenceSensor();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        JMenuBar menuBar = new JMenuBar();

        if (presenceSensor != null || temperatureSensor != null) {
            JMenu simulation = new JMenu("Simulation");
            if (presenceSensor != null) {
                simulation.add(createPresenceSimulationMenu(room));
            }
            if (temperatureSensor != null) {
                simulation.add(createTemperatureSimulationMenu(room));
            }
            menuBar.add(simulation);
        }

        JMenu changeTemperatureSettings = new JMenu("Preference");
        changeTemperatureSettings.add(createChangeIdealTemperatureMenu(room));
        changeTemperatureSettings.add(createChangeThresholdMenu(room));
        menuBar.add(changeTemperatureSettings);

        setJMenuBar(menuBar);
    }

    private JMenuItem createPresenceSimulationMenu(Room room) {
        JMenuItem presenceSimulation = new JMenuItem("Presence");
        presenceSimulation.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                room.getPresenceSimulation().setPresence(!room.getPresenceSimulation().isPresence());
            }
        });
        return presenceSimulation;
    }

    private JMenuItem createTemperatureSimulationMenu(Room room) {
        JMenuItem temperatureSimulation = new JMenuItem("Temperature");
        temperatureSimulation.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = DialogOpener.openInputDialog(
                        "Change temperature",
                        "Simulate change current room temperature:",
                        JOptionPane.QUESTION_MESSAGE,
                        String.valueOf(room.getTemperatureSimulation().getTemperature())
                );

                if (str != null) {
                    try{
                        int value = Integer.parseInt(str);
                        room.getTemperatureSimulation().setTarget(value);
                    } catch (NumberFormatException e1) {
                        notIntegerDialog();
                    }
                }
            }
        });
        return temperatureSimulation;
    }

    private JMenuItem createChangeIdealTemperatureMenu(Room room) {
        JMenuItem changeIdealTemperature = new JMenuItem("Ideal temperature");
        changeIdealTemperature.addActionListener(e -> {
            String str = DialogOpener.openInputDialog(
                    "Ideal temperature",
                    "Change ideal temperature:",
                    JOptionPane.QUESTION_MESSAGE,
                    String.valueOf(room.getTemperaturePreference().getIdealTemperature())
            );

            if (str != null) {
                try{
                    int value = Integer.parseInt(str);
                    room.getTemperaturePreference().setIdealTemperature(value);
                } catch (NumberFormatException e1) {
                    notIntegerDialog();
                }
            }
        });
        return changeIdealTemperature;
    }

    private JMenuItem createChangeThresholdMenu(Room room) {
        JMenuItem changeThreshold = new JMenuItem("Threshold");
        changeThreshold.addActionListener(e -> {
            String str = DialogOpener.openInputDialog(
                    "Temperature Threshold",
                    "Change temperature Threshold:",
                    JOptionPane.QUESTION_MESSAGE,
                    String.valueOf(room.getTemperaturePreference().getThreshold())
            );

            if (str != null) {
                try{
                    int value = Integer.parseInt(str);
                    room.getTemperaturePreference().setThreshold(value);
                } catch (NumberFormatException e1) {
                    notIntegerDialog();
                }
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

    /* - */

    private AutomationListener createAutomationIlluminationControlListener() {
        return this::setAutomaticIlluminationLabel;
    }

    private AutomationListener createAutomationTemperatureControlListener() {
        return this::setAutomaticTemperatureControlLabelText;
    }

    private TemperaturePreferenceListener createTemperaturePreferenceListener() {
        return new TemperaturePreferenceListener() {
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
        };
    }

    private AirConditionerListener createAirConditionerListener() {
        return new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                setAirConditionerStateLabelText(state);
            }

            @Override
            public void onTemperatureChange(int temperature) {
                setAirConditionerTemperatureLabelText(temperature);
            }
        };
    }

    private SensorListener<Boolean> createSensorPresenceListener() {
        return this::setAmbientPresenceLabelText;
    }

    private SensorListener<Integer> createSensorTemperatureListener() {
        return this::setAmbientTemperatureLabelText;
    }

}
