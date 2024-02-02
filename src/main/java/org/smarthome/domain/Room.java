package org.smarthome.domain;

import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.simulation.RoomSimulation;

import java.util.List;

public class Room extends RoomSimulation {

    private final String name;
    private final List<Light> lights;

    public Room(String name, List<Light> lights) {
        super();
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
