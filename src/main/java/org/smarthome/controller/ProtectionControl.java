package org.smarthome.controller;

import org.smarthome.domain.Room;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.EmergencyService;
import org.smarthome.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProtectionControl {

    private final Alarm alarm;
    private final List<IlluminationControl> illuminationControls;
    private Timer emergencyTimer;
    private boolean emergencyTimerRunning;

    public ProtectionControl(Alarm alarm, List<Room> rooms) {
        this.alarm = alarm;
        this.illuminationControls = new ArrayList<>();
        for (Room room : rooms) {
            illuminationControls.add(room.getIlluminationControl());
        }
        this.emergencyTimerRunning = false;
    }

    public void handleAlarm() {
        if (alarm != null) {
            alarm.handle();
        }
    }

    public boolean emergencySituation() {
        if (alarm != null) {
            boolean emergency = alarm.emergency();
            if (emergency) {
                startEmergencyTimer();
            } else {
                resetEmergencyTimer();
            }

            return emergency;
        }
        return false;
    }

    private void handleEmergency() {
        if (alarm.isArmed()) {
            for (IlluminationControl illuminationControl : illuminationControls) {
                illuminationControl.handleAutomation(false);
            }
            EmergencyService.getInstance().emergencyCall();
        }
    }

    private synchronized void startEmergencyTimer() {
        if (!emergencyTimerRunning) {
            resetEmergencyTimer();

            emergencyTimer = new Timer();
            emergencyTimerRunning = true;
            emergencyTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handleEmergency();
                    emergencyTimerRunning = false;
                }
            }, Constants.emergencyTimerMsDuration());
        }
    }

    private void resetEmergencyTimer() {
        if (emergencyTimer != null) {
            emergencyTimer.cancel();
            emergencyTimer = null;
            emergencyTimerRunning = false;
        }
    }

}
