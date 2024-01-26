package org.smarthome.domain;

import java.util.List;

public class SmartHome {

    private final List<Room> rooms;

    public SmartHome(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

}
