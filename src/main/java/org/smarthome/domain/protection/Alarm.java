package org.smarthome.domain.protection;

import org.smarthome.exception.WrongSecurityPinException;
import org.smarthome.listener.ObservableElement;
import org.smarthome.listener.AlarmListener;
import org.smarthome.util.Constants;

import java.util.Objects;

public class Alarm extends ObservableElement<AlarmListener> {

    private final Siren siren;
    private final EmergencyService emergencyService;
    private AlarmState alarmState;

    public Alarm(Siren siren, EmergencyService emergencyService) {
        super();
        this.siren = siren;
        this.emergencyService = emergencyService;
        this.alarmState = new Disarmed(this);
    }

    public Siren getSiren() {
        return siren;
    }

    public EmergencyService getEmergencyService() {
        return emergencyService;
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

    public synchronized void handle(String pin) {
        if (pin != null && pin.equals(Constants.securityPin())) {
            alarmState.handle();
        } else {
            for (AlarmListener observer : observers) {
                observer.onWrongSecurityPinException(new WrongSecurityPinException());
            }
        }
    }

    public synchronized void emergency() {
        alarmState.emergency();
    }

}
