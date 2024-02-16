package org.smarthome.controller;

import org.smarthome.domain.sensor.PresenceSensor;
import org.smarthome.util.Constants;

import java.util.Timer;
import java.util.TimerTask;

public class SensorPresenceControl extends SensorControl<Boolean> {

    private final ProtectionControl protectionControl;
    private final IlluminationControl illuminationControl;
    private Timer presenceTimer;

    public SensorPresenceControl(
            PresenceSensor presenceSensor,
            ProtectionControl protectionControl,
            IlluminationControl illuminationControl) {
        super(presenceSensor);
        this.protectionControl = protectionControl;
        this.illuminationControl = illuminationControl;
    }

    @Override
    public void onDataChange(Boolean presence) {
        if (presence != null) {
            if (protectionControl.isAutomationActive() &&
                    protectionControl.isAlarmArmed()) {
                protectionControl.handleAutomation(presence);
            } else if (illuminationControl.isAutomationActive()) {
                if (presence) {
                    resetPresenceTimer();
                    illuminationControl.handleAutomation(true);
                } else {
                    startPresenceTimer();
                }
            }
        }
    }

    private void startPresenceTimer() {
        resetPresenceTimer();

        presenceTimer = new Timer();
        presenceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                illuminationControl.handleAutomation(false);
            }
        }, Constants.presenceTimerMsDuration());
    }

    private void resetPresenceTimer() {
        if (presenceTimer != null) {
            presenceTimer.cancel();
            presenceTimer = null;
        }
    }

}
