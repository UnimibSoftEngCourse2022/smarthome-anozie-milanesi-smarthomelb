package org.smarthome.gui;

import org.smarthome.controller.ProtectionControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.*;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.AlarmState;
import org.smarthome.domain.protection.Armed;

import javax.swing.*;
import java.awt.*;

public class SmartHomeGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel roomPanel;
    private JButton startCleaningButton;
    private JButton stopCleaningButton;
    private JLabel chargingStationPositionLabel;
    private JLabel currentPositionLabel;
    private JLabel vacuumStateLabel;
    private JLabel alarmStateLabel;
    private JLabel sirenStateLabel;
    private JLabel automaticProtectionControlStateLabel;
    private JButton powerAlarmButton;
    private JButton automaticProtectionControlButton;
    private JPanel cleaningPanel;
    private JPanel securityPanel;

    public SmartHomeGUI(SmartHomeGUIController smartHomeGUIController) {
        setContentPane(mainPanel);
        setTitle("Smart Home");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initRooms(smartHomeGUIController);
        initCleaning(smartHomeGUIController);
        initSecurity(smartHomeGUIController);
        pack();
        setLocationRelativeTo(null);
    }

    private void initRooms(SmartHomeGUIController smartHomeGUIController) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        roomPanel.setLayout(new GridBagLayout());

        for(Room room : smartHomeGUIController.getSmartHome().getRooms()) {
            roomPanel.add(createRoomButton(smartHomeGUIController, room), constraints);
        }
    }

    private void initCleaning(SmartHomeGUIController smartHomeGUIController) {
        Vacuum vacuum = smartHomeGUIController.getSmartHome().getVacuum();
        if (vacuum != null) {
            chargingStationPositionLabel.setText(vacuum.getChargingStationPosition().getName());
            setCurrentPositionLabelText(vacuum.getCurrentPosition());
            setVacuumStateLabelText(vacuum.getVacuumState());

            vacuum.addObserver(smartHomeGUIController.getVacuumListener());

            startCleaningButton.addActionListener(smartHomeGUIController.handleStartCleaningButton());
            stopCleaningButton.addActionListener(smartHomeGUIController.handleStopCleaningButton());
        } else {
            cleaningPanel.setVisible(false);
        }
    }

    private void initSecurity(SmartHomeGUIController smartHomeGUIController) {
        ProtectionControl protectionControl = smartHomeGUIController.getSmartHome().getProtectionControl();
        Alarm alarm = smartHomeGUIController.getSmartHome().getAlarm();

        if (alarm != null) {
            setAlarmStateLabelText(alarm.getAlarmState());
            setSirenStateLabelText(alarm.getSiren().isActive());

            alarm.addObserver(smartHomeGUIController.getAlarmListener());
            alarm.getSiren().addObserver(smartHomeGUIController.getSirenListener());

            alarm.getEmergencyService().addObserver(smartHomeGUIController.getEmergencyServiceListener());

            setAutomaticProtectionControlState(protectionControl.isAutomationActive());
            protectionControl.addObserver(smartHomeGUIController.getAutomationProtectionControlListener());

            powerAlarmButton.addActionListener(
                    smartHomeGUIController.handlePowerAlarmButton());
            automaticProtectionControlButton.addActionListener(
                    smartHomeGUIController.handleAutomaticProtectionControlButton());
        } else {
            securityPanel.setVisible(false);
        }
    }

    private JButton createRoomButton(SmartHomeGUIController smartHomeGUIController, Room room) {
        JButton roomButton = new JButton(room.getName());
        roomButton.addActionListener(smartHomeGUIController.handleRoomButton(room));
        return roomButton;
    }

    public void setVacuumStateLabelText(VacuumState vacuumState) {
        if (vacuumState.getClass().equals(Cleaning.class)) {
            vacuumStateLabel.setText("Cleaning");
        } else if (vacuumState.getClass().equals(Transit.class)) {
            vacuumStateLabel.setText("Transit");
        } else if (vacuumState.getClass().equals(Charging.class)) {
            vacuumStateLabel.setText("Charging");
        }
    }

    public void setCurrentPositionLabelText(Room currentPosition) {
        currentPositionLabel.setText(currentPosition.getName());
    }

    public void setAlarmStateLabelText(AlarmState state) {
        if (state.getClass().equals(Armed.class)) {
            alarmStateLabel.setText("Armed");
        } else  {
            alarmStateLabel.setText("Disarmed");
        }
    }

    public void setSirenStateLabelText(boolean active) {
        if (active) {
            sirenStateLabel.setText("On");
        } else  {
            sirenStateLabel.setText("Off");
        }
    }

    public void setAutomaticProtectionControlState(boolean automationActive) {
        if (automationActive) {
            automaticProtectionControlStateLabel.setText("On");
        } else  {
            automaticProtectionControlStateLabel.setText("Off");
        }
    }

}
