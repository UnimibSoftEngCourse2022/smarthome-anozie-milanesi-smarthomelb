package org.smarthome.controller;

import org.smarthome.domain.sensor.PresenceSensor;

import java.util.Timer;
import java.util.TimerTask;

import static org.smarthome.util.Constants.PRESENCE_TIMER_MS_DURATION;

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
        if (presence != null && !protectionControl.emergencySituation()) {
            if (illuminationControl.isAutomationActive()) {
                if (presence) {
                    illuminationControl.handleAutomation(true);
                    resetPresenceTimer();
                } else {
                    startPresenceTimer();
                }
            }
        }
    }

    private void startPresenceTimer() {
        if (presenceTimer != null) {
            presenceTimer.cancel();
        }

        presenceTimer = new Timer();
        presenceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                illuminationControl.handleAutomation(false);
            }
        }, PRESENCE_TIMER_MS_DURATION);
    }

    private void resetPresenceTimer() {
        if (presenceTimer != null) {
            presenceTimer.cancel();
            presenceTimer = null;
        }
    }

}
