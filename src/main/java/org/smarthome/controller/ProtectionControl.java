package org.smarthome.controller;

import org.smarthome.domain.protection.Alarm;

public class ProtectionControl {

    private final Alarm alarm;

    public ProtectionControl(Alarm alarm) {
        this.alarm = alarm;
    }

    public void handleAlarm() {
        if (alarm != null) {
            alarm.handle();
        }
    }

    public boolean emergencySituation() {
        if (alarm != null) {
            return alarm.emergency();
        }
        return false;
    }

    public void deactivateSiren() {
        if (alarm != null) {
            alarm.getSiren().setActive(false);
        }
    }

}
