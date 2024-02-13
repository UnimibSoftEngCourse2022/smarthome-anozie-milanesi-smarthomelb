package org.smarthome.gui;

import org.smarthome.controller.CleaningControl;
import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.*;
import org.smarthome.exception.CleaningException;
import org.smarthome.listener.VacuumListener;

import javax.swing.*;

public class VacuumGUI extends JFrame implements VacuumListener {

    private JPanel panel1;
    private JButton startCleaningButton;
    private JButton stopCleaningButton;
    private JLabel stateLabel;
    private JLabel currentPositionLabel;
    private JLabel chargingStationPositionLabel;

    public VacuumGUI(SmartHome smartHome) {
        setContentPane(panel1);
        setSize(768,432);
        setTitle("Cleaning");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initCleaningGUI(smartHome.getVacuum(), smartHome.getCleaningControl());
        setVisible(true);
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
        JDialog dialog = new JDialog();
        JLabel label = new JLabel("The cleaning of the house is now complete!");
        dialog.setTitle("Complete Cleaning");
        dialog.add(label);
        dialog.setSize(384, 216);
        dialog.setVisible(true);
    }

    @Override
    public void onStoppedCleaning() {
        JDialog dialog = new JDialog();
        JLabel label = new JLabel("The cleaning process has stopped!");
        dialog.setTitle("Stopped Cleaning");
        dialog.add(label);
        dialog.setSize(384, 216);
        dialog.setVisible(true);
    }

    @Override
    public void onCleaningException(CleaningException e) {
        JDialog dialog = new JDialog();
        JLabel label = new JLabel(e.getMessage());
        dialog.setTitle("Cleaning Error!");
        dialog.add(label);
        dialog.setSize(384, 216);
        dialog.setVisible(true);
    }

}
