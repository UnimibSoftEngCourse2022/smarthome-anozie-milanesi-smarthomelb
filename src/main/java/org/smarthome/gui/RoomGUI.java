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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RoomGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel lightPanel;
    private JButton lightControlButton;
    private JButton automaticIlluminationControlButton;
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

    public RoomGUI(RoomGUIController roomGUIController) {
        setContentPane(mainPanel);
        setTitle(roomGUIController.getRoom().getName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMenu(roomGUIController);
        initIllumination(roomGUIController);
        initAirConditioner(roomGUIController);
        initTemperatureSettings(roomGUIController);
        initAmbient(roomGUIController);
        backHomeButton.addActionListener(roomGUIController.handleBackHomeButton());
        pack();
        setLocationRelativeTo(null);
    }

    private void initIllumination(RoomGUIController roomGUIController) {
        Room room = roomGUIController.getRoom();
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.Y_AXIS));

        IlluminationControl illuminationControl = room.getIlluminationControl();
        Illumination illumination = room.getIllumination();
        PresenceSensor presenceSensor = room.getPresenceSensor();

        if (illumination != null) {
            for(Light light : room.getIllumination().getLights()) {
                JRadioButton button = createLightButton(roomGUIController, light);
                light.addObserver(roomGUIController.getLightActionListener(button, light));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                lightPanel.add(button);
            }
            lightControlButton.addActionListener(roomGUIController.handleLightControlButton());
        } else {
            illuminationPanel.setVisible(false);
        }

        if (presenceSensor != null) {
            setAutomaticIlluminationLabel(illuminationControl.isAutomationActive());
            illuminationControl.addObserver(roomGUIController.getAutomationIlluminationControlListener());

            automaticIlluminationControlButton.addActionListener(
                    roomGUIController.handleAutomaticIlluminationControlButton());
        } else {
            automaticIlluminationPanel.setVisible(false);
        }
    }

    private void initAirConditioner(RoomGUIController roomGUIController) {
        Room room = roomGUIController.getRoom();
        AirConditioner airConditioner = room.getAirConditioner();

        if(airConditioner != null) {
            setAirConditionerStateLabelText(airConditioner.getAirConditionerState());
            setAirConditionerTemperatureLabelText(airConditioner.getTemperature());

            airConditioner.addObserver(roomGUIController.getAirConditionerListener());

            activateAirConditionerButton.addActionListener(
                    roomGUIController.handleActivateAirConditionerButton());

            increaseTemperatureButton.addActionListener(
                    roomGUIController.handleIncreaseTemperatureButton());

            decreaseTemperatureButton.addActionListener(
                    roomGUIController.handleDecreaseTemperatureButton());
        } else {
            airConditionerPanel.setVisible(false);
        }
    }

    private void initTemperatureSettings(RoomGUIController roomGUIController) {
        Room room = roomGUIController.getRoom();
        TemperatureControl temperatureControl = room.getTemperatureControl();
        TemperaturePreference temperaturePreference = room.getTemperaturePreference();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        setIdealTemperatureLabelText(temperaturePreference.getIdealTemperature());
        setThresholdLabelText(temperaturePreference.getThreshold());

        temperatureControl.addObserver(roomGUIController.getAutomationTemperatureControlListener());
        temperaturePreference.addObserver(roomGUIController.getTemperaturePreferenceListener());

        if (temperatureSensor != null) {
            setAutomaticTemperatureControlLabelText(temperatureControl.isAutomationActive());
            automaticTemperatureControlButton.addActionListener(
                    roomGUIController.handleAutomaticTemperatureControlButton());
        } else {
            automaticTemperaturePanel.setVisible(false);
        }
    }

    private void initAmbient(RoomGUIController roomGUIController) {
        Room room = roomGUIController.getRoom();
        PresenceSensor presenceSensor = room.getPresenceSensor();
        TemperatureSensor temperatureSensor = room.getTemperatureSensor();

        if (presenceSensor == null && temperatureSensor == null) {
            ambientPanel.setVisible(false);
        } else {
            if (presenceSensor != null) {
                setAmbientPresenceLabelText(presenceSensor.getData());
                presenceSensor.addObserver(roomGUIController.getSensorPresenceListener());
            } else {
                presenceLabel.setVisible(false);
                ambientPresenceLabel.setVisible(false);
            }

            if (temperatureSensor != null) {
                setAmbientTemperatureLabelText(temperatureSensor.getData());
                temperatureSensor.addObserver(roomGUIController.getSensorTemperatureListener());
            } else {
                temperatureLabel.setVisible(false);
                ambientTemperatureLabel.setVisible(false);
            }
        }
    }

    private JRadioButton createLightButton(RoomGUIController roomGUIController, Light light) {
        JRadioButton lightRadioButton = new JRadioButton(light.getName());
        if (light.isOn()) {
            lightRadioButton.setSelected(true);
        }

        lightRadioButton.addActionListener(
                roomGUIController.handleLightRadioButton(light));
        return lightRadioButton;
    }

    public void setAutomaticIlluminationLabel(boolean automationActive) {
        automaticIlluminationLabel.setText(String.valueOf(automationActive));
    }

    public void setIdealTemperatureLabelText(int idealTemperature) {
        idealTemperatureLabel.setText(idealTemperature + "°");
    }

    public void setThresholdLabelText(int threshold) {
        thresholdLabel.setText(String.valueOf(threshold));
    }

    public void setAutomaticTemperatureControlLabelText(boolean automaticActive) {
        if(automaticActive) {
            automaticTemperatureControlLabel.setText("On");
        } else {
            automaticTemperatureControlLabel.setText("Off");
        }
    }

    public void setAirConditionerStateLabelText(AirConditionerState state) {
        if (state.getClass().equals(AirConditionerOn.class)) {
            airConditionerState.setText("On");
        } else {
            airConditionerState.setText("Off");
        }
    }

    public void setAirConditionerTemperatureLabelText(int temperature) {
        airConditionerTemperature.setText(temperature + "°");
    }

    public void setAmbientPresenceLabelText(boolean presence) {
        ambientPresenceLabel.setText(String.valueOf(presence));
    }

    public void setAmbientTemperatureLabelText(int temperature) {
        ambientTemperatureLabel.setText(temperature + "°");
    }

    private void setMenu(RoomGUIController roomGUIController) {
        Room room = roomGUIController.getRoom();
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
                String str = DialogGUIOpener.openInputDialog(
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
            String str = DialogGUIOpener.openInputDialog(
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
            String str = DialogGUIOpener.openInputDialog(
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
        DialogGUIOpener.openMessageDialog(
                "Error!",
                "The value typed is not an integer value.",
                JOptionPane.ERROR_MESSAGE
        );
    }

}
