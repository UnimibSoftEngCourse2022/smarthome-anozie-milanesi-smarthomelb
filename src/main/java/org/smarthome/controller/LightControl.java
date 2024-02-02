package org.smarthome.controller;

import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;

import java.util.List;

public class LightControl {

    private final Illumination illumination;

    public LightControl(Illumination illumination) {
        this.illumination = illumination;
    }

    public void handleIllumination() {
        if (illumination != null) {
            illumination.handle();
        }
    }

    public void handleSingleLight(Light light) {
        if (light != null) {
            light.handle();
        }
    }

}
