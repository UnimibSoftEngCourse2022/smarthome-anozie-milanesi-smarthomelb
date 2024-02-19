package org.smarthome.gui;

import org.smarthome.controller.CleaningControl;
import org.smarthome.controller.ProtectionControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.*;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.AlarmState;
import org.smarthome.domain.protection.Armed;
import org.smarthome.exception.CleaningException;
import org.smarthome.exception.WrongSecurityPinException;
import org.smarthome.gui.util.DialogOpener;
import org.smarthome.listener.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SmartHomeGUI extends JFrame {

    private static SmartHomeGUI instance;

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

    public static SmartHomeGUI getInstance() {
        return instance;
    }

    public static SmartHomeGUI init(SmartHome smartHome) {
        if (instance == null) {
            instance = new SmartHomeGUI(smartHome);
        }
        return instance;
    }

    private SmartHomeGUI(SmartHome smartHome) {
        setContentPane(mainPanel);
        setTitle("Smart Home");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initRooms(smartHome);
        initCleaning(smartHome);
        initSecurity(smartHome);
        pack();
        setLocationRelativeTo(null);
    }

    private void initRooms(SmartHome smartHome) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        roomPanel.setLayout(new GridBagLayout());

        for(Room room : smartHome.getRooms()) {
            roomPanel.add(createRoomButton(room), constraints);
        }
    }

    private void initCleaning(SmartHome smartHome) {
        CleaningControl cleaningControl = smartHome.getCleaningControl();
        Vacuum vacuum = smartHome.getVacuum();

        if (vacuum != null) {
            chargingStationPositionLabel.setText(vacuum.getChargingStationPosition().getName());
            setCurrentPositionLabelText(vacuum.getCurrentPosition());
            setVacuumStateLabelText(vacuum.getVacuumState());

            vacuum.addObserver(createVacuumListener());

            startCleaningButton.addActionListener(e -> cleaningControl.startCleaning());
            stopCleaningButton.addActionListener(e -> cleaningControl.stopCleaning());
        } else {
            cleaningPanel.setVisible(false);
        }
    }

    private void initSecurity(SmartHome smartHome) {
        ProtectionControl protectionControl = smartHome.getProtectionControl();
        Alarm alarm = smartHome.getAlarm();

        if (alarm != null) {
            setAlarmStateLabelText(alarm.getAlarmState());
            setSirenStateLabelText(alarm.getSiren().isActive());

            alarm.addObserver(createAlarmListener());
            alarm.getSiren().addObserver(createSirenListener());
            alarm.getEmergencyService().addObserver(createEmergencyServiceListener());

            setAutomaticProtectionControlState(protectionControl.isAutomationActive());
            protectionControl.addObserver(createAutomationProtectionControlListener());

            powerAlarmButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String pin = DialogOpener.openInputDialog(
                            "Security pin",
                            "Type security pin:",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (pin != null) {
                        protectionControl.handleAlarm(pin);
                    }
                }
            });
            automaticProtectionControlButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    protectionControl.setAutomationActive(!protectionControl.isAutomationActive());
                }
            });
        } else {
            securityPanel.setVisible(false);
        }
    }

    private JButton createRoomButton(Room room) {
        JButton roomButton = new JButton(room.getName());
        roomButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                RoomGUI.init(room).setVisible(true);
            }
        });
        return roomButton;
    }

    private void setVacuumStateLabelText(VacuumState vacuumState) {
        if (vacuumState.getClass().equals(Cleaning.class)) {
            vacuumStateLabel.setText("Cleaning");
        } else if (vacuumState.getClass().equals(Transit.class)) {
            vacuumStateLabel.setText("Transit");
        } else if (vacuumState.getClass().equals(Charging.class)) {
            vacuumStateLabel.setText("Charging");
        }
    }

    private void setCurrentPositionLabelText(Room currentPosition) {
        currentPositionLabel.setText(currentPosition.getName());
    }

    private void setAlarmStateLabelText(AlarmState state) {
        if (state.getClass().equals(Armed.class)) {
            alarmStateLabel.setText("Armed");
        } else  {
            alarmStateLabel.setText("Disarmed");
        }
    }

    private void setSirenStateLabelText(boolean active) {
        if (active) {
            sirenStateLabel.setText("On");
        } else  {
            sirenStateLabel.setText("Off");
        }
    }

    private void setAutomaticProtectionControlState(boolean automationActive) {
        if (automationActive) {
            automaticProtectionControlStateLabel.setText("On");
        } else  {
            automaticProtectionControlStateLabel.setText("Off");
        }
    }

    /* - */

    private VacuumListener createVacuumListener() {
        return new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
                setCurrentPositionLabelText(currentPosition);
            }

            @Override
            public void onChangeState(VacuumState state) {
                setVacuumStateLabelText(state);
            }

            @Override
            public void onCompletedCleaning() {
                DialogOpener.openMessageDialog(
                        "Complete Cleaning",
                        "The cleaning of the house is now complete!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            @Override
            public void onStoppedCleaning() {
                DialogOpener.openMessageDialog(
                        "Stopped Cleaning",
                        "The cleaning process has stopped!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            @Override
            public void onCleaningException(CleaningException e) {
                DialogOpener.openMessageDialog(
                        "Cleaning Error!",
                        e.getMessage(),
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    private AlarmListener createAlarmListener() {
        return new AlarmListener() {
            @Override
            public void onChangeState(AlarmState state) {
                setAlarmStateLabelText(state);
            }

            @Override
            public void onWrongSecurityPinException(WrongSecurityPinException e) {
                DialogOpener.openMessageDialog(
                        "Security Error!",
                        e.getMessage(),
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    private SirenListener createSirenListener() {
        return this::setSirenStateLabelText;
    }

    private EmergencyServiceListener createEmergencyServiceListener() {
        return emergencyNumber ->
                DialogOpener.openMessageDialog(
                        "Emergency Call",
                        "Emergency call to number: " + emergencyNumber,
                        JOptionPane.WARNING_MESSAGE
                );
    }

    private AutomationListener createAutomationProtectionControlListener() {
        return this::setAutomaticProtectionControlState;
    }

}
