package org.smarthome.domain;

import org.smarthome.controller.LightControl;
import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;

import java.util.List;

public class Room {

    private final String name;
    private final Illumination illumination;
    private final LightControl lightControl;

    public Room(String name, List<Light> lights) {
        this.name = name;
        if (lights == null) {
            this.illumination = null;
        } else {
            this.illumination = new Illumination(lights);
        }
        this.lightControl = new LightControl(illumination);
    }

    public String getName() {
        return name;
    }

    public Illumination getIllumination() {
        return illumination;
    }

    public LightControl getLightControl() {
        return lightControl;
    }

}
