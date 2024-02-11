package org.smarthome.domain.protection;

import org.smarthome.listener.ObservableElement;
import org.smarthome.listener.AlarmListener;

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

    public synchronized AlarmState getAlarmState() {
        return alarmState;
    }

    public synchronized boolean isArmed() {
        return getAlarmState().getClass().equals(Armed.class);
    }


    public synchronized void setAlarmState(AlarmState alarmState) {
        if (!Objects.equals(getAlarmState().getClass(), alarmState.getClass())) {
            this.alarmState = alarmState;
            for (AlarmListener observer : observers) {
                observer.onChangeState(alarmState);
            }
        }
    }

    public synchronized void handle() {
        alarmState.handle();
    }

    public synchronized boolean emergency() {
        return alarmState.emergency();
    }

}
