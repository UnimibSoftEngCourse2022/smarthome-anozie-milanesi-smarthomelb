package org.smarthome.domain;

import org.smarthome.domain.illumination.Light;

import java.util.List;

public class Room {

    private final String name;
    private final List<Light> lights;

    public Room(String name, List<Light> lights) {
        this.name = name;
        this.lights = lights;
    }

    public String getName() {
        return name;
    }

    public List<Light> getLights() {
        return lights;
    }

}
