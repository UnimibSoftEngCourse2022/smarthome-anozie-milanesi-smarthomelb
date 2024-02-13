package org.smarthome.listener;

public interface EmergencyServiceListener extends ElementListener {
    void onEmergencyCall(String emergencyNumber);
}
