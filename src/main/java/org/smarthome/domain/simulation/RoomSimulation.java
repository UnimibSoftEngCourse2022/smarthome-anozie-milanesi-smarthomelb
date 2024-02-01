package org.smarthome.domain.simulation;

import org.smarthome.domain.Room;

public class RoomSimulation {

    private final Room room;
    private final RoomPresenceSimulation presenceSimulation;
    private final RoomTemperatureSimulation roomTemperatureSimulation;

    public RoomSimulation(Room room) {
        this.room = room;
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
