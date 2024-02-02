package org.smarthome.domain;

import org.smarthome.builder.SmartHomeRoomBuilder;
import org.smarthome.controller.IlluminationControl;
import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;
import org.smarthome.domain.simulation.RoomSimulation;

import java.util.List;

public class Room extends RoomSimulation {

    private final String name;
    private final Illumination illumination;
    private final IlluminationControl illuminationControl;

    public Room(SmartHomeRoomBuilder builder) {
        super();
        this.name = builder.getName();
        List<Light> lights = builder.getLights();
        if (lights != null && lights.size() > 0) {
            this.illumination = new Illumination(lights);
        } else {
            this.illumination = null;
        }
        this.illuminationControl = new IlluminationControl(illumination);
    }

    public String getName() {
        return name;
    }

    public Illumination getIllumination() {
        return illumination;
    }

    public IlluminationControl getLightControl() {
        return illuminationControl;
    }

}
