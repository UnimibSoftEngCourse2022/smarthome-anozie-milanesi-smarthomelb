package org.smarthome.domain.protection;

import org.smarthome.domain.Actuator;

public abstract class AlarmState implements Actuator {

    protected Alarm alarm;

    protected AlarmState(Alarm alarm) {
        this.alarm = alarm;
    }

    public abstract boolean emergency();

}
