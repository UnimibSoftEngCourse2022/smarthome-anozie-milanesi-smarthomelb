package org.smarthome.domain;

import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.controller.CleaningControl;
import org.smarthome.domain.cleaning.Vacuum;

import java.util.List;

public class SmartHome {

    private final List<Room> rooms;
    private final Vacuum vacuum;
    private final CleaningControl cleaningControl;

    public SmartHome(SmartHomeBuilder builder) {
        this.rooms = builder.getRooms();
        Vacuum vacuum = null;
        Room chargingStationPosition = builder.getChargingStationPosition();
        if (chargingStationPosition != null) {
            vacuum = new Vacuum(rooms, chargingStationPosition);
        }
        this.vacuum = vacuum;
        this.cleaningControl = new CleaningControl(vacuum);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Vacuum getVacuum() {
        return vacuum;
    }

    public CleaningControl getCleaningControl() {
        return cleaningControl;
    }

}
