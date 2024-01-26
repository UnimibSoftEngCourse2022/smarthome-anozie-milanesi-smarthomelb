package org.smarthome.domain;

import java.util.List;

public class SmartHome {

    private List<Room> room;

    public SmartHome(List<Room> room) {
        this.room = room;
    }

    public List<Room> getRoom() {
        return room;
    }

}
