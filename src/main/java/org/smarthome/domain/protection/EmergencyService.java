package org.smarthome.domain.protection;

import org.smarthome.listener.EmergencyServiceListener;
import org.smarthome.listener.ObservableElement;

public class EmergencyService extends ObservableElement<EmergencyServiceListener> {

    private static EmergencyService instance;

    private EmergencyService() {}

    public static synchronized EmergencyService getInstance() {
        if (instance == null) {
            instance = new EmergencyService();
        }
        return instance;
    }

    public synchronized void emergencyCall() {
        // Emulate an emergency call
        for (EmergencyServiceListener observer : observers) {
            observer.onEmergencyCall();
        }
    }

}