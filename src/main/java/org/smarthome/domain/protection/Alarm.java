package org.smarthome.domain.protection;

import org.smarthome.domain.ObservableElement;
import org.smarthome.domain.listener.AlarmListener;

import java.util.Objects;

public class Alarm extends ObservableElement<AlarmListener> {

    private final Siren siren;
    private AlarmState alarmState;

    public Alarm(Siren siren) {
        super();
        this.siren = siren;
        this.alarmState = new Disarmed(this);
    }

    public Siren getSiren() {
        return siren;
    }

    public AlarmState getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(AlarmState alarmState) {
        if (!Objects.equals(getAlarmState().getClass(), alarmState.getClass())) {
            this.alarmState = alarmState;
            for (AlarmListener observer : observers) {
                observer.onChangeState(alarmState);
            }
        }
    }

    public void handle() {
        alarmState.handle();
    }

    public boolean emergency() {
        return alarmState.emergency();
    }

}
