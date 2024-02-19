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
import org.smarthome.gui.util.ConstantsGUI;
import org.smarthome.gui.util.DialogOpener;
import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.AutomationListener;
import org.smarthome.listener.SensorListener;
import org.smarthome.listener.TemperaturePreferenceListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RoomGUI extends JFrame {

    private final AutomationListener automationIlluminationControlListener =
            this::setAutomaticIlluminationLabel;

    private final AutomationListener automationTemperatureControlListener =
            this::setAutomaticTemperatureControlLabelText;

    private final TemperaturePreferenceListener temperaturePreferenceListener = new TemperaturePreferenceListener() {
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

    private final AirConditionerListener airConditionerListener = new AirConditionerListener() {
        @Override
        public void onChangeState(AirConditionerState state) {
            setAirConditionerStateLabelText(state);
        }

        @Override
        public void onTemperatureChange(int temperature) {
            setAirConditionerTemperatureLabelText(temperature);
        }
    };

    private final SensorListener<Boolean> sensorPresenceListener = this::setAmbientPresenceLabelText;

    private final SensorListener<Integer> sensorTemperatureListener = this::setAmbientTemperatureLabelText;

    private static RoomGUI instance;

    private final Room room;
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

    public static RoomGUI getInstance() {
        return instance;
    }

    public static RoomGUI init(Room room) {
        if (instance == null) {
            instance = new RoomGUI(room);
        } else {
            if (instance.getRoom() != null && instance.getRoom() != room) {
                instance.dispose();
                instance = new RoomGUI(room);
            }
        }
        return instance;
    }

    private RoomGUI(Room room) {
        this.room = room;
        setContentPane(mainPanel);
        setTitle(room.getName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMenu();
        initIllumination();
        initAirConditioner();
        initTemperatureSettings();
        initAmbient();
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

    public Room getRoom() {
        return room;
    }

    private void initIllumination() {
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
            illuminationControl.addObserver(automationIlluminationControlListener);

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

    private void initAirConditioner() {
        TemperatureControl temperatureControl = room.getTemperatureControl();
        AirConditioner airConditioner = room.getAirConditioner();

        if(airConditioner != null) {
            setAirConditionerStateLabelText(airConditioner.getAirConditionerState());
            setAirConditionerTemperatureLabelText(airConditioner.getTemperature());

            airConditioner.addObserver(airConditionerListener);

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

    private void initTemperatureSettings() {
        TemperatureControl temperatureControl = room.getTemperatureControl();
        TemperaturePreference temperaturePreference = room.getTemperaturePreference();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        setIdealTemperatureLabelText(temperaturePreference.getIdealTemperature());
        setThresholdLabelText(temperaturePreference.getThreshold());

        temperatureControl.addObserver(automationTemperatureControlListener);
        temperaturePreference.addObserver(temperaturePreferenceListener);

        if (temperatureSensor != null) {
            setAutomaticTemperatureControlLabelText(temperatureControl.isAutomationActive());
            automaticTemperatureControlButton.addActionListener(e ->
                    temperatureControl.setAutomationActive(!temperatureControl.isAutomationActive()));
        } else {
            automaticTemperaturePanel.setVisible(false);
        }
    }

    private void initAmbient() {
        PresenceSensor presenceSensor = room.getPresenceSensor();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        if (presenceSensor == null && temperatureSensor == null) {
            ambientPanel.setVisible(false);
        } else {
            if (presenceSensor != null) {
                setAmbientPresenceLabelText(presenceSensor.getData());
                presenceSensor.addObserver(sensorPresenceListener);
            } else {
                presenceLabel.setVisible(false);
                ambientPresenceLabel.setVisible(false);
            }

            if (temperatureSensor != null) {
                setAmbientTemperatureLabelText(temperatureSensor.getData());
                temperatureSensor.addObserver(sensorTemperatureListener);
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
        airConditionerTemperature.setText(temperature + "°");
    }

    private void setAmbientPresenceLabelText(boolean presence) {
        ambientPresenceLabel.setText(String.valueOf(presence));
    }

    private void setAmbientTemperatureLabelText(int temperature) {
        ambientTemperatureLabel.setText(temperature + "°");
    }

    private void setMenu() {
        PresenceSensor presenceSensor = room.getPresenceSensor();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        JMenuBar menuBar = new JMenuBar();

        if (presenceSensor != null || temperatureSensor != null) {
            JMenu simulation = new JMenu("Simulation");
            if (presenceSensor != null) {
                simulation.add(createPresenceSimulationMenu());
            }
            if (temperatureSensor != null) {
                simulation.add(createTemperatureSimulationMenu());
            }
            menuBar.add(simulation);
        }

        JMenu changeTemperatureSettings = new JMenu("Preference");
        changeTemperatureSettings.add(createChangeIdealTemperatureMenu());
        changeTemperatureSettings.add(createChangeThresholdMenu());
        menuBar.add(changeTemperatureSettings);

        setJMenuBar(menuBar);
    }

    private JMenuItem createPresenceSimulationMenu() {
        JMenuItem presenceSimulation = new JMenuItem("Presence");
        presenceSimulation.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                room.getPresenceSimulation().setPresence(!room.getPresenceSimulation().isPresence());
            }
        });
        return presenceSimulation;
    }

    private JMenuItem createTemperatureSimulationMenu() {
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

    private JMenuItem createChangeIdealTemperatureMenu() {
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

    private JMenuItem createChangeThresholdMenu() {
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

}
