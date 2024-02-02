package org.smarthome.domain.simulation;

public class RoomSimulation {

    private final RoomPresenceSimulation presenceSimulation;
    private final RoomTemperatureSimulation temperatureSimulation;

    public RoomSimulation() {
        this.presenceSimulation = new RoomPresenceSimulation();
        this.temperatureSimulation = new RoomTemperatureSimulation();
    }

    public RoomPresenceSimulation getPresenceSimulation() {
        return presenceSimulation;
    }

    public RoomTemperatureSimulation getTemperatureSimulation() {
        return temperatureSimulation;
    }

}
