package org.smarthome.builder;

import org.smarthome.domain.Room;
import org.smarthome.domain.illumination.Light;

import java.util.ArrayList;
import java.util.List;

public class SmartHomeRoomBuilder implements RoomBuilder {

    private final String name;
    private final List<Light> lights;

    public SmartHomeRoomBuilder(String name) {
        this.name = name;
        this.lights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Light> getLights() {
        return lights;
    }

    @Override
    public SmartHomeRoomBuilder addLight(Light light) {
        lights.add(light);
        return this;
    }

    public Room create() {
        return new Room(this);
    }

}
