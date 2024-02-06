package org.smarthome.controller;

import org.smarthome.domain.illumination.Illumination;
import org.smarthome.domain.illumination.Light;

public class IlluminationControl extends AutomaticControl<Boolean> {

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

    @Override
    public void handleAutomation(Boolean presence) {
        if (illumination != null) {
            if (presence != null && presence) {
                illumination.on();
            } else {
                illumination.off();
            }
        }
    }

}
