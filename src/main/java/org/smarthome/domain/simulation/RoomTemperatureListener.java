package org.smarthome.domain.simulation;

public interface RoomTemperatureListener {
    void onTemperatureChange(int temperature);
    void onTargetTemperatureReached();
    void onStopTemperatureTargetGoal();
}
