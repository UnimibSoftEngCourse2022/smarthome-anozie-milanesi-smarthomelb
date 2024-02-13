package org.smarthome.domain.protection;

import org.smarthome.listener.EmergencyServiceListener;
import org.smarthome.listener.ObservableElement;

public class EmergencyService extends ObservableElement<EmergencyServiceListener> {

    private final String emergencyNumber;

    public EmergencyService(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void emergencyCall() {
        // Emulate an emergency call
        for (EmergencyServiceListener observer : observers) {
            observer.onEmergencyCall(emergencyNumber);
        }
    }

}