package org.smarthome.builder;

import org.smarthome.domain.SmartHome;
import org.smarthome.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeBuilder {

    private List<Room> room;

    public SmartHomeBuilder() {
        this.room = new ArrayList<>();
    }

    public SmartHomeBuilder addRoom(Room room) {
        this.room.add(room);
        return this;
    }

    public SmartHome create() {
        return new SmartHome(room);
    }

}
