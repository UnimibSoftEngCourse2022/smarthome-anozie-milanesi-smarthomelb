package org.smarthome.builder;

import org.smarthome.domain.SmartHome;
import org.smarthome.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeBuilder implements HomeBuilder {

    private final List<Room> rooms;
    private Room chargingStationPosition;

    public SmartHomeBuilder() {
        this.rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room getChargingStationPosition() {
        return chargingStationPosition;
    }

    @Override
    public SmartHomeBuilder addRoom(Room room) {
        rooms.add(room);
        return this;
    }

    @Override
    public SmartHomeBuilder setVacuumChargingStationPosition(Room chargingStationPosition) {
        this.chargingStationPosition = chargingStationPosition;
        return this;
    }

    public SmartHome create() {
        return new SmartHome(this);
    }

}
