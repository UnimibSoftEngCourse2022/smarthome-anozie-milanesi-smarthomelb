package org.smarthome.domain.protection;

public class EmergencyService {

    private static EmergencyService instance;

    private EmergencyService() {}

    public synchronized static EmergencyService getInstance() {
        if (instance == null) {
            instance = new EmergencyService();
        }
        return instance;
    }

    public void makeEmergencyCall() {
        // Emulate an emergency call
    }

}