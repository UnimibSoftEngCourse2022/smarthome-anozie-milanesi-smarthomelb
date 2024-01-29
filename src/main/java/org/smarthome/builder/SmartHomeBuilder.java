package org.smarthome.builder;

import org.smarthome.domain.SmartHome;
import org.smarthome.domain.Room;
import org.smarthome.domain.cleaning.Vacuum;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeBuilder implements HomeBuilder {

    private final List<Room> rooms;
    private Vacuum vacuum;

    public SmartHomeBuilder() {
        this.rooms = new ArrayList<>();
    }

    @Override
    public SmartHomeBuilder addRoom(Room room) {
        rooms.add(room);
        return this;
    }

    @Override
    public SmartHomeBuilder setVacuum(Vacuum vacuum) {
        this.vacuum = vacuum;
        return this;
    }


    public SmartHome create() {
        return new SmartHome(rooms, vacuum);
    }

}
