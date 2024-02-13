package org.smarthome.gui;

import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.*;
import org.smarthome.domain.illumination.Light;
import org.smarthome.exception.CleaningException;
import org.smarthome.listener.VacuumListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class VacuumGUI extends JFrame{
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private VacuumListener observer;

    public VacuumGUI(Vacuum vacuum) {
        setContentPane(panel1);
        setSize(600,600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setVacuumObserver(vacuum);
        initVacuum(vacuum);
    }

    public void initVacuum(Vacuum vacuum) {

        textField1.setText("Charging station position: " + vacuum.getChargingStationPosition().getName());
        setTextFieldCurrentPosition(vacuum.getCurrentPosition());
        setTextFieldState(vacuum.getVacuumState());

        textField1.setVisible(true);
        textField2.setVisible(true);
        textField3.setVisible(true);


        button1.setText("Start Cleaning");
        button2.setText("Stop Cleanig");

        button1.addActionListener(e -> {

            try {

                vacuum.clean();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            observer.onChangeState(vacuum.getVacuumState());
            observer.onChangePosition(vacuum.getCurrentPosition());
            observer.onCompletedCleaning();
        });

        button2.addActionListener(e -> {

            try {

                vacuum.stop();

            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            observer.onStoppedCleaning();
            observer.onChangeState(vacuum.getVacuumState());
            observer.onChangePosition(vacuum.getCurrentPosition());

        });

    }


    public void setVacuumObserver(Vacuum vacuum) {
        observer = new VacuumListener() {
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
                dialog.setSize(200, 200);
                dialog.setVisible(true);
            }

            @Override
            public void onStoppedCleaning() {
                JDialog dialog = new JDialog();
                JLabel label = new JLabel("The cleaning process has stopped!");
                dialog.setTitle("Stopped Cleaning");
                dialog.add(label);
                dialog.setVisible(true);

                setTextFieldState(vacuum.getVacuumState());
                setTextFieldCurrentPosition(vacuum.getCurrentPosition());
            }

            @Override
            public void onCleaningException(CleaningException e) {
            }
        };
    }


    public void setTextFieldState(VacuumState vacuumState) {
        if (vacuumState.getClass().equals(Cleaning.class)) {
            textField3.setText("Stato: Cleaning");
        }
        else if (vacuumState.getClass().equals(Transit.class)) {
            textField3.setText("Stato: Transit");
        }
        else if (vacuumState.getClass().equals(Charging.class)) {
            textField3.setText("Stato: Charging");
        }
    }

    public void setTextFieldCurrentPosition(Room currentPosition) {
        textField2.setText("Current Position: " + currentPosition.getName());
    }

}
