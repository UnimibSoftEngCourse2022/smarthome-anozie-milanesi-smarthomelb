package org.smarthome.controller;

import org.smarthome.domain.Room;
import org.smarthome.domain.protection.Alarm;

import java.util.ArrayList;
import java.util.List;

public class ProtectionControl {

    private final Alarm alarm;
    private final List<IlluminationControl> illuminationControls;

    public ProtectionControl(Alarm alarm, List<Room> rooms) {
        this.alarm = alarm;
        this.illuminationControls = new ArrayList<>();
        for (Room room : rooms) {
            illuminationControls.add(room.getIlluminationControl());
        }
    }

    public void handleAlarm() {
        if (alarm != null) {
            alarm.handle();
        }
    }

    public boolean emergencySituation() {
        if (alarm != null) {
            boolean isEmergency = alarm.emergency();
            if (isEmergency) {
                turnOffAllIllumination();
            }

            return isEmergency;
        }
        return false;
    }

    public void deactivateSiren() {
        if (alarm != null) {
            alarm.getSiren().setActive(false);
        }
    }

    private void turnOffAllIllumination() {
        for (IlluminationControl illuminationControl : illuminationControls) {
            illuminationControl.handleAutomation(false);
        }
    }

}
