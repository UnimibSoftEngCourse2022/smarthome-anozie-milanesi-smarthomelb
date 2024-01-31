package org.smarthome.builder;

import org.smarthome.domain.Room;

public interface HomeBuilder {
    HomeBuilder addRoom(Room room);
    HomeBuilder setVacuumChargingStationPosition(Room chargingStationPosition);
}
