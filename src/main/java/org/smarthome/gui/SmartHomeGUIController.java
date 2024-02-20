package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.SmartHome;
import org.smarthome.domain.cleaning.VacuumState;
import org.smarthome.domain.protection.AlarmState;
import org.smarthome.exception.CleaningException;
import org.smarthome.exception.WrongSecurityPinException;
import org.smarthome.listener.*;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SmartHomeGUIController {

    private final SmartHome smartHome;
    private final SmartHomeGUI smartHomeGUI;

    public SmartHomeGUIController(SmartHome smartHome) {
        this.smartHome = smartHome;
        this.smartHomeGUI = new SmartHomeGUI(this);
    }

    public SmartHome getSmartHome() {
        return smartHome;
    }

    public SmartHomeGUI getSmartHomeGUI() {
        return smartHomeGUI;
    }

    public ActionListener handleRoomButton(Room room) {
        return e -> {
            smartHomeGUI.setVisible(false);
            RoomGUIController roomGUIController = new RoomGUIController(smartHomeGUI, room);
            roomGUIController.getRoomGUI().setVisible(true);
        };
    }

    public ActionListener handleStartCleaningButton() {
        return e -> smartHome.getCleaningControl().startCleaning();
    }

    public ActionListener handleStopCleaningButton() {
        return e -> smartHome.getCleaningControl().stopCleaning();
    }

    public ActionListener handlePowerAlarmButton() {
        return e -> {
            String pin = DialogGUIOpener.openInputDialog(
                    "Security pin",
                    "Type security pin:",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (pin != null) {
                smartHome.getProtectionControl().handleAlarm(pin);
            }
        };
    }

    public ActionListener handleAutomaticProtectionControlButton() {
        return e -> smartHome.getProtectionControl().setAutomationActive(
                !smartHome.getProtectionControl().isAutomationActive());
    }

    public VacuumListener getVacuumListener() {
        return new VacuumListener() {
            @Override
            public void onChangePosition(Room currentPosition) {
                smartHomeGUI.setCurrentPositionLabelText(currentPosition);
            }

            @Override
            public void onChangeState(VacuumState state) {
                smartHomeGUI.setVacuumStateLabelText(state);
            }

            @Override
            public void onCompletedCleaning() {
                DialogGUIOpener.openMessageDialog(
                        "Complete Cleaning",
                        "The cleaning of the house is now complete!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            @Override
            public void onStoppedCleaning() {
                DialogGUIOpener.openMessageDialog(
                        "Stopped Cleaning",
                        "The cleaning process has stopped!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            @Override
            public void onCleaningException(CleaningException e) {
                DialogGUIOpener.openMessageDialog(
                        "Cleaning Error!",
                        e.getMessage(),
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    public AlarmListener getAlarmListener() {
        return new AlarmListener() {
            @Override
            public void onChangeState(AlarmState state) {
                smartHomeGUI.setAlarmStateLabelText(state);
            }

            @Override
            public void onWrongSecurityPinException(WrongSecurityPinException e) {
                DialogGUIOpener.openMessageDialog(
                        "Security Error!",
                        e.getMessage(),
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    public SirenListener getSirenListener() {
        return active -> smartHomeGUI.setSirenStateLabelText(active);
    }

    public EmergencyServiceListener getEmergencyServiceListener() {
        return emergencyNumber ->
                DialogGUIOpener.openMessageDialog(
                        "Emergency Call",
                        "Emergency call to number: " + emergencyNumber,
                        JOptionPane.WARNING_MESSAGE
                );
    }

    public AutomationListener getAutomationProtectionControlListener() {
        return automationActive -> smartHomeGUI.setAutomaticProtectionControlState(automationActive);
    }

}
