package org.smarthome.gui;

import org.smarthome.controller.CleaningControl;
import org.smarthome.controller.ProtectionControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.*;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.exception.CleaningException;
import org.smarthome.gui.dialog.MessageDialog;
import org.smarthome.listener.VacuumListener;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import static org.smarthome.util.Constants.*;


public class SmartHomeGUI extends JFrame implements VacuumListener {
    private JPanel mainPanel;
    private JPanel roomPanel;
    private JButton startCleaningButton;
    private JButton stopCleaningButton;
    private JLabel stateLabel;
    private JLabel currentPositionLabel;
    private JLabel chargingStationPositionLabel;
    private JLabel alarmState;
    private JLabel sirenState;
    private JButton activateAutomaticControlButton;
    private JLabel emergencyCallState;
    private JLabel automaticControlState;
    private MessageDialog messageDialog;

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
        messageDialog = new MessageDialog();
        stateLabel.setText(vacuum.getChargingStationPosition().getName());
        setTextFieldCurrentPosition(vacuum.getCurrentPosition());
        setTextFieldState(vacuum.getVacuumState());

        vacuum.addObserver(this);

        startCleaningButton.addActionListener(e -> cleaningControl.startCleaning());
        stopCleaningButton.addActionListener(e -> cleaningControl.stopCleaning());
    }

    private void initSecurity(ProtectionControl protectionControl, Alarm alarm) {

        protectionControl.addObserver(protectionControl::setAutomationActive);

        activateAutomaticControlButton.addActionListener(e -> {
            protectionControl.setAutomationActive(!protectionControl.isAutomationActive());
        });

        if(alarm != null && protectionControl.isAutomationActive()) {
            setAlarmState(protectionControl);
            setSirenState(alarm);
            setEmergencyCallState(protectionControl);
        }else {
            automaticControlState.setText("OFF");
            alarmState.setText("OFF");
            sirenState.setText("OFF");
            emergencyCallState.setText("NO CALL");
        }

    }

    private void setAutomaticControlState(ProtectionControl protectionControl) {
        if(protectionControl.isAutomationActive()) {
            automaticControlState.setText("ON");
        }else  {
            automaticControlState.setText("OFF");
        }
    }

    private void setAlarmState(ProtectionControl protectionControl){
        if(protectionControl.isAlarmArmed()) {
            alarmState.setText("ON");
        }else  {
            alarmState.setText("OFF");
        }
    }

    private void setSirenState(Alarm alarm) {
        if(alarm.getSiren().isActive()) {
            sirenState.setText("ON");
        }else {
            sirenState.setText("OFF");
        }
    }

    private void setEmergencyCallState(ProtectionControl protectionControl) {
        if(protectionControl.isAlarmArmed()){
            emergencyCallState.setText("CALL ON GOING");
        } else {
            emergencyCallState.setText("NO CALL");
        }
    }

    private JButton createRoomButton(Room room) {
        JButton roomButton = new JButton(room.getName());
        roomButton.addActionListener(e -> new RoomGUI(room));
        return roomButton;
    }

    private void setTextFieldState(VacuumState vacuumState) {
        if (vacuumState.getClass().equals(Cleaning.class)) {
            chargingStationPositionLabel.setText("Cleaning");
        }
        else if (vacuumState.getClass().equals(Transit.class)) {
            chargingStationPositionLabel.setText("Transit");
        }
        else if (vacuumState.getClass().equals(Charging.class)) {
            chargingStationPositionLabel.setText("Charging");
        }
    }

    private void setTextFieldCurrentPosition(Room currentPosition) {
        currentPositionLabel.setText(currentPosition.getName());
    }

    @Override
    public void onChangePosition(Room currentPosition) {
        setTextFieldCurrentPosition(currentPosition);
    }

    @Override
    public void onChangeState(VacuumState state) {
        setTextFieldState(state);
    }

    @Override
    public void onCompletedCleaning() {
        messageDialog.setTitleDialog("Complete Cleaning");
        messageDialog.setMessageDialog("The cleaning of the house is now complete!");
        messageDialog.setVisible(true);

    }

    @Override
    public void onStoppedCleaning() {
        messageDialog.setTitleDialog("Stopped Cleaning");
        messageDialog.setMessageDialog("The cleaning process has stopped!");
        messageDialog.setVisible(true);

    }

    @Override
    public void onCleaningException(CleaningException e) {
        messageDialog.setTitleDialog("Cleaning Error!");
        messageDialog.setMessageDialog(e.getMessage());
        messageDialog.setVisible(true);
    }

}
