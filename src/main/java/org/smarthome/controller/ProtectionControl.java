package org.smarthome.controller;

import org.smarthome.domain.Room;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProtectionControl extends AutomaticControl<Boolean> {

    private final Alarm alarm;
    private final List<IlluminationControl> homeIlluminations;
    private Timer emergencyTimer;
    private boolean emergencyTimerRunning;

    public ProtectionControl(Alarm alarm, List<Room> rooms) {
        this.alarm = alarm;
        this.homeIlluminations = new ArrayList<>();
        for (Room room : rooms) {
            homeIlluminations.add(room.getIlluminationControl());
        }
        this.emergencyTimerRunning = false;
        setAutomationActive(true);
    }

    public synchronized boolean isAlarmArmed() {
        if (alarm != null) {
            boolean armed = alarm.isArmed();
            if (!armed) {
                resetEmergencyTimer();
            }
            return armed;
        }
        return false;
    }

    public void handleAlarm(String pin) {
        if (alarm != null) {
            alarm.handle(pin);
        }
    }

    @Override
    public void handleAutomation(Boolean presence) {
        if (alarm != null && presence != null && presence) {
            alarm.emergency();
            if (isAlarmArmed()) {
                startEmergencyTimer();
            }
        }
    }

    private void handleEmergency() {
        if (alarm != null && alarm.isArmed()) {
            for (IlluminationControl illuminationControl : homeIlluminations) {
                illuminationControl.handleAutomation(false);
            }
            alarm.getEmergencyService().emergencyCall();
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
