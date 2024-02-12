package org.smarthome.domain.sensor;

import org.smarthome.simulation.RoomTemperatureSimulation;

public class TemperatureSensor extends Sensor<Integer> {

    private RoomTemperatureSimulation roomTemperatureSimulation;

    public TemperatureSensor() {
        super();
    }

    public void setRoomTemperatureSimulation(RoomTemperatureSimulation roomTemperatureSimulation) {
        this.roomTemperatureSimulation = roomTemperatureSimulation;
    }

    @Override
    public Integer monitor() {
        if (roomTemperatureSimulation != null) {
            return roomTemperatureSimulation.getTemperature();
        }
        return null;
    }

    @Override
    public boolean analyze(Integer detected) {
        if (detected != null && !detected.equals(data)) {
            this.data = detected;
            return true;
        }
        return false;
    }

    @Override
    public void plan() {
        // no planning
    }

    @Override
    public void execute() {
        notifyDataChange();
    }

}
