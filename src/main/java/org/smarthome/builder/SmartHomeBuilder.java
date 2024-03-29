package org.smarthome.builder;

import org.smarthome.domain.SmartHome;
import org.smarthome.domain.Room;
import org.smarthome.domain.protection.Alarm;
import org.smarthome.domain.protection.EmergencyService;
import org.smarthome.domain.protection.Siren;
import org.smarthome.exception.UnidentifiedRoomException;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeBuilder implements HomeBuilder {

    private final List<Room> rooms;
    private Room vacuumChargingStationPosition;
    private Alarm alarm;

    public SmartHomeBuilder() {
        this.rooms = new ArrayList<>();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room getVacuumChargingStationPosition() {
        return vacuumChargingStationPosition;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    @Override
    public SmartHomeBuilder addRoom(Room room) {
        rooms.add(room);
        return this;
    }

    @Override
    public SmartHomeBuilder setVacuumChargingStationPosition(Room position) {
        if (!rooms.contains(position)) {
            throw new UnidentifiedRoomException(position);
        }
        vacuumChargingStationPosition = position;
        return this;
    }

    @Override
    public SmartHomeBuilder setProtection(Siren siren, EmergencyService emergencyService) {
        if (siren != null && emergencyService != null) {
            this.alarm = new Alarm(siren, emergencyService);
        }
        return this;
    }

    public SmartHome create() {
        return new SmartHome(this);
    }

}
