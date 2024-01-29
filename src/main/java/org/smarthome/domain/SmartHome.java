package org.smarthome.domain;

import org.smarthome.controller.CleaningControl;
import org.smarthome.domain.cleaning.Vacuum;

import java.util.List;

public class SmartHome {

    private final List<Room> rooms;
    private final Vacuum vacuum;
    private final CleaningControl cleaningControl;

    public SmartHome(List<Room> rooms, Vacuum vacuum) {
        this.rooms = rooms;
        this.vacuum = vacuum;
        if (vacuum == null) {
            this.cleaningControl = null;
        } else {
            this.cleaningControl = new CleaningControl(vacuum);
        }
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
