package org.smarthome.domain.protection;

public class Armed extends AlarmState {

    public Armed(Alarm alarm) {
        super(alarm);
    }

    @Override
    public void handle() {
        alarm.setAlarmState(new Disarmed(alarm));
        if (alarm.getSiren().isActive()) {
            alarm.getSiren().setActive(false);
        }
    }

    @Override
    public boolean emergency() {
        alarm.getSiren().setActive(true);
        return true;
    }

}
