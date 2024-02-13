package org.smarthome.builder;

import org.smarthome.domain.Room;
import org.smarthome.domain.protection.EmergencyService;
import org.smarthome.domain.protection.Siren;

public interface HomeBuilder {
    HomeBuilder addRoom(Room room);
    HomeBuilder setVacuumChargingStationPosition(Room position);
    HomeBuilder setProtection(Siren siren, EmergencyService emergencyService);
}
