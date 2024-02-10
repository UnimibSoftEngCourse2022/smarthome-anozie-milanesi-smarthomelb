package org.smarthome.simulation;

public interface RoomTemperatureListener {
    void onTemperatureChange(int temperature);
    void onStopTemperatureChange();
}
