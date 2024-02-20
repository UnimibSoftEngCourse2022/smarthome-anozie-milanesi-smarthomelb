package org.smarthome.gui;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.temperature.AirConditionerState;
import org.smarthome.exception.FieldOutOfRangeException;
import org.smarthome.listener.*;

import javax.swing.*;
import java.awt.event.ActionListener;

public class RoomGUIController {

    private final SmartHomeGUI smartHomeGUI;
    private final Room room;
    private final RoomGUI roomGUI;

    public RoomGUIController(SmartHomeGUI smartHomeGUI, Room room) {
        this.smartHomeGUI = smartHomeGUI;
        this.room = room;
        this.roomGUI = new RoomGUI(this);
    }

    public Room getRoom() {
        return room;
    }

    public RoomGUI getRoomGUI() {
        return roomGUI;
    }

    public ActionListener handleBackHomeButton() {
        return e -> {
            roomGUI.dispose();
            smartHomeGUI.setVisible(true);
        };
    }

    public ActionListener handleLightRadioButton(Light light) {
        return e -> light.handle();
    }

    public ActionListener handleLightControlButton() {
        return  e -> room.getIlluminationControl().handleIllumination();
    }

    public ActionListener handleAutomaticIlluminationControlButton() {
        return e -> room.getIlluminationControl().setAutomationActive(
                !room.getIlluminationControl().isAutomationActive());
    }

    public ActionListener handleActivateAirConditionerButton() {
        return e -> room.getTemperatureControl().handleAirConditioner();
    }

    public ActionListener handleIncreaseTemperatureButton() {
        return e -> room.getTemperatureControl().increaseTemperature();
    }

    public ActionListener handleDecreaseTemperatureButton() {
        return e -> room.getTemperatureControl().decreaseTemperature();
    }

    public LightActionListener getLightActionListener(JRadioButton button, Light light) {
        return state -> button.setSelected(light.isOn());
    }

    public AutomationListener getAutomationIlluminationControlListener() {
        return automationActive -> roomGUI.setAutomaticIlluminationLabel(automationActive);
    }

    public AutomationListener getAutomationTemperatureControlListener() {
        return automationActive -> roomGUI.setAutomaticTemperatureControlLabelText(automationActive);
    }

    public ActionListener handleAutomaticTemperatureControlButton() {
        return e -> room.getTemperatureControl().setAutomationActive(
                !room.getTemperatureControl().isAutomationActive());
    }

    public TemperaturePreferenceListener getTemperaturePreferenceListener() {
        return new TemperaturePreferenceListener() {
            @Override
            public void onIdealTemperatureChange(int idealTemperature) {
                roomGUI.setIdealTemperatureLabelText(idealTemperature);
            }

            @Override
            public void onThresholdChange(int threshold) {
                roomGUI.setThresholdLabelText(threshold);
            }

            @Override
            public void onFieldOutOfRangeException(FieldOutOfRangeException e) {
                DialogGUIOpener.openMessageDialog(
                        "Error!",
                        "Temperature out of range",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        };
    }

    public AirConditionerListener getAirConditionerListener() {
        return new AirConditionerListener() {
            @Override
            public void onChangeState(AirConditionerState state) {
                roomGUI.setAirConditionerStateLabelText(state);
            }

            @Override
            public void onTemperatureChange(int temperature) {
                roomGUI.setAirConditionerTemperatureLabelText(temperature);
            }
        };
    }

    public SensorListener<Boolean> getSensorPresenceListener() {
        return presence -> roomGUI.setAmbientPresenceLabelText(presence);
    }

    public SensorListener<Integer> getSensorTemperatureListener() {
        return temperature -> roomGUI.setAmbientTemperatureLabelText(temperature);
    }

}
