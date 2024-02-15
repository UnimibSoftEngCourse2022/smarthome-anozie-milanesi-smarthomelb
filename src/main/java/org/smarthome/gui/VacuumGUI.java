package org.smarthome.gui;

import org.smarthome.controller.CleaningControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.*;
import org.smarthome.exception.CleaningException;
import org.smarthome.gui.dialog.VacuumDialog;
import org.smarthome.listener.VacuumListener;

import javax.swing.*;

import static org.smarthome.util.Constants.*;

public class VacuumGUI extends JFrame implements VacuumListener {
    private JPanel panel1;
    private JButton startCleaningButton;
    private JButton stopCleaningButton;
    private JLabel stateLabel;
    private JLabel currentPositionLabel;
    private JLabel chargingStationPositionLabel;
    private final VacuumDialog vacuumDialog;

    public VacuumGUI(SmartHome smartHome) {
        setContentPane(panel1);
        setSize(defaultJFrameWidthSetting(),defaultJFrameHeightSetting());
        setTitle("Cleaning");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initCleaningGUI(smartHome.getVacuum(), smartHome.getCleaningControl());
        setVisible(true);
        vacuumDialog = new VacuumDialog();
    }

    public void initCleaningGUI(Vacuum vacuum, CleaningControl cleaningControl) {
        stateLabel.setText(vacuum.getChargingStationPosition().getName());
        setTextFieldCurrentPosition(vacuum.getCurrentPosition());
        setTextFieldState(vacuum.getVacuumState());

        vacuum.addObserver(this);

        startCleaningButton.addActionListener(e -> cleaningControl.startCleaning());
        stopCleaningButton.addActionListener(e -> cleaningControl.stopCleaning());
    }

    public void setTextFieldState(VacuumState vacuumState) {
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

    public void setTextFieldCurrentPosition(Room currentPosition) {
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
        vacuumDialog.setTitleDialog("Complete Cleaning");
        vacuumDialog.setMessageDialog("The cleaning of the house is now complete!");
        vacuumDialog.setVisible(true);

    }

    @Override
    public void onStoppedCleaning() {
        vacuumDialog.setTitleDialog("Stopped Cleaning");
        vacuumDialog.setMessageDialog("The cleaning process has stopped!");
        vacuumDialog.setVisible(true);

    }

    @Override
    public void onCleaningException(CleaningException e) {
        vacuumDialog.setTitleDialog("Cleaning Error!");
        vacuumDialog.setMessageDialog(e.getMessage());
        vacuumDialog.setVisible(true);
    }

}
