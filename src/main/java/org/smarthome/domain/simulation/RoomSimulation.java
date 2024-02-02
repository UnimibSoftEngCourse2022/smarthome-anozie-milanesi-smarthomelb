package org.smarthome.domain.simulation;

public class RoomSimulation {

    private final RoomPresenceSimulation presenceSimulation;
    private final RoomTemperatureSimulation roomTemperatureSimulation;

    public RoomSimulation() {
        this.presenceSimulation = new RoomPresenceSimulation();
        this.roomTemperatureSimulation = new RoomTemperatureSimulation();
    }

    public RoomPresenceSimulation getPresenceSimulation() {
        return presenceSimulation;
    }

    public RoomTemperatureSimulation getRoomTemperatureSimulation() {
        return roomTemperatureSimulation;
    }

}
