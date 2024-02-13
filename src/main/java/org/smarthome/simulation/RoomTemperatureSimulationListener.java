package org.smarthome.simulation;

public interface RoomTemperatureSimulationListener {
    void onTemperatureChange(int temperature);
    void onStopTemperatureChange();
}
