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
import org.smarthome.gui.util.ConstantsGUI;
import org.smarthome.gui.util.DialogOpener;
import org.smarthome.listener.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.smarthome.util.Constants.*;


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

    public SmartHomeGUI(SmartHome smartHome) {
        setContentPane(mainPanel);
        setTitle("Smart Home");
        setSize(defaultJFrameWidthSetting(), defaultJFrameHeightSetting());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initRooms(smartHome.getRooms());
        initCleaning(smartHome.getVacuum(), smartHome.getCleaningControl());
        initSecurity(smartHome.getProtectionControl(), smartHome.getAlarm());
        setVisible(true);
    }

    private void initRooms(List<Room> rooms) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        roomPanel.setLayout(new GridBagLayout());

        for(Room room : rooms) {
            roomPanel.add(createRoomButton(room), constraints);
        }
    }

    private void initCleaning(Vacuum vacuum, CleaningControl cleaningControl) {
        chargingStationPositionLabel.setText(vacuum.getChargingStationPosition().getName());
        setCurrentPositionLabelText(vacuum.getCurrentPosition());
        setVacuumStateLabelText(vacuum.getVacuumState());

        vacuum.addObserver(new VacuumListener() {
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
        });

        startCleaningButton.addActionListener(e -> cleaningControl.startCleaning());
        stopCleaningButton.addActionListener(e -> cleaningControl.stopCleaning());
    }

    private void initSecurity(ProtectionControl protectionControl, Alarm alarm) {
        if (alarm != null) {
            setAlarmStateLabelText(alarm.getAlarmState());
            setSirenStateLabelText(alarm.getSiren().isActive());

            alarm.addObserver(new AlarmListener() {
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
            });

            alarm.getSiren().addObserver(this::setSirenStateLabelText);

            alarm.getEmergencyService().addObserver(emergencyNumber -> {
                DialogOpener.openMessageDialog(
                        "Emergency Call",
                        "Emergency call to number: " + emergencyNumber,
                        JOptionPane.WARNING_MESSAGE
                );
            });
        } else {
            alarmStateLabel.setText(ConstantsGUI.NONE);
            sirenStateLabel.setText(ConstantsGUI.NONE);
        }

        setAutomaticProtectionControlState(protectionControl.isAutomationActive());
        protectionControl.addObserver(this::setAutomaticProtectionControlState);

        powerAlarmButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pin = DialogOpener.openInputDialog(
                        "Security pin",
                        "Type security pin:",
                        JOptionPane.QUESTION_MESSAGE
                );
                protectionControl.handleAlarm(pin);
            }
        });

        automaticProtectionControlButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                protectionControl.setAutomationActive(!protectionControl.isAutomationActive());
            }
        });
    }

    private JButton createRoomButton(Room room) {
        JButton roomButton = new JButton(room.getName());
        roomButton.addActionListener(e -> new RoomGUI(room));
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
            alarmStateLabel.setText(ConstantsGUI.ARMED);
        } else  {
            alarmStateLabel.setText(ConstantsGUI.DISARMED);
        }
    }

    private void setSirenStateLabelText(boolean active) {
        if (active) {
            sirenStateLabel.setText(ConstantsGUI.ON);
        } else  {
            sirenStateLabel.setText(ConstantsGUI.OFF);
        }
    }

    private void setAutomaticProtectionControlState(boolean automationActive) {
        if (automationActive) {
            automaticProtectionControlStateLabel.setText(ConstantsGUI.ON);
        } else  {
            automaticProtectionControlStateLabel.setText(ConstantsGUI.OFF);
        }
    }

}
