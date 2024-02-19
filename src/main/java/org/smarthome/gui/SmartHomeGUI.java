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

    private final VacuumListener vacuumListener = new VacuumListener() {
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

    private final AlarmListener alarmListener = new AlarmListener() {
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

    private final SirenListener sirenListener = this::setSirenStateLabelText;

    private final EmergencyServiceListener emergencyNumber = emergencyNumber ->
            DialogOpener.openMessageDialog(
                    "Emergency Call",
                    "Emergency call to number: " + emergencyNumber,
                    JOptionPane.WARNING_MESSAGE
            );

    private final AutomationListener automationProtectionControlListener =
            this::setAutomaticProtectionControlState;

    private static SmartHomeGUI instance;

    private final SmartHome smartHome;
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
        } else {
            if (instance.getSmartHome() != null && instance.getSmartHome() != smartHome) {
                instance.dispose();
                instance = new SmartHomeGUI(smartHome);
            }
        }
        return instance;
    }

    private SmartHomeGUI(SmartHome smartHome) {
        this.smartHome = smartHome;
        setContentPane(mainPanel);
        setTitle("Smart Home");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initRooms();
        initCleaning();
        initSecurity();
        pack();
        setLocationRelativeTo(null);
    }

    public SmartHome getSmartHome() {
        return smartHome;
    }

    private void initRooms() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        roomPanel.setLayout(new GridBagLayout());

        for(Room room : smartHome.getRooms()) {
            roomPanel.add(createRoomButton(room), constraints);
        }
    }

    private void initCleaning() {
        CleaningControl cleaningControl = smartHome.getCleaningControl();
        Vacuum vacuum = smartHome.getVacuum();

        if (vacuum != null) {
            chargingStationPositionLabel.setText(vacuum.getChargingStationPosition().getName());
            setCurrentPositionLabelText(vacuum.getCurrentPosition());
            setVacuumStateLabelText(vacuum.getVacuumState());

            vacuum.addObserver(vacuumListener);

            startCleaningButton.addActionListener(e -> cleaningControl.startCleaning());
            stopCleaningButton.addActionListener(e -> cleaningControl.stopCleaning());
        } else {
            cleaningPanel.setVisible(false);
        }
    }

    private void initSecurity() {
        ProtectionControl protectionControl = smartHome.getProtectionControl();
        Alarm alarm = smartHome.getAlarm();

        if (alarm != null) {
            setAlarmStateLabelText(alarm.getAlarmState());
            setSirenStateLabelText(alarm.getSiren().isActive());

            alarm.addObserver(alarmListener);
            alarm.getSiren().addObserver(sirenListener);
            alarm.getEmergencyService().addObserver(emergencyNumber);

            setAutomaticProtectionControlState(protectionControl.isAutomationActive());
            protectionControl.addObserver(automationProtectionControlListener);

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
