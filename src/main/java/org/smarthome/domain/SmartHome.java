package org.smarthome.domain;

import org.smarthome.builder.SmartHomeBuilder;
import org.smarthome.controller.CleaningControl;
import org.smarthome.domain.cleaning.Vacuum;

import java.util.List;

public class SmartHome {

    private static SmartHome instance;

    private final List<Room> rooms;
    private final Vacuum vacuum;
    private final CleaningControl cleaningControl;

    private SmartHome(SmartHomeBuilder builder) {
        this.rooms = builder.getRooms();
        Vacuum vacuum = null;
        Room chargingStationPosition = builder.getVacuumChargingStationPosition();
        if (chargingStationPosition != null) {
            vacuum = new Vacuum(rooms, chargingStationPosition);
        }
        this.vacuum = vacuum;
        this.cleaningControl = new CleaningControl(vacuum);
    }

    public synchronized static SmartHome initialize(SmartHomeBuilder builder) {
        if (instance == null) {
            instance = new SmartHome(builder);
        }
        return getInstance();
    }

    public static synchronized SmartHome getInstance() {
        return instance;
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
