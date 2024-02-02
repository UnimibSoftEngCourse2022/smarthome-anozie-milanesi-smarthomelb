package org.smarthome.controller;

import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;

public class IlluminationControl {

    private final Illumination illumination;

    public IlluminationControl(Illumination illumination) {
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
