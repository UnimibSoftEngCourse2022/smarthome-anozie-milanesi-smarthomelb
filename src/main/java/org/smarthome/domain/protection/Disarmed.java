package org.smarthome.domain.protection;

public class Disarmed extends AlarmState {

    public Disarmed(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void handle() {
        alarm.setAlarmState(new Armed(alarm));
    }

    @Override
    public boolean emergency() {
        return false;
    }

}
