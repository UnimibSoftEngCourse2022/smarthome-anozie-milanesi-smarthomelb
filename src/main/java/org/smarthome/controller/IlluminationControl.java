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
            setAutomationActive(false);
            illumination.handle();
        }
    }

    public void handleSingleLight(Light light) {
        if (light != null) {
            setAutomationActive(false);
            light.handle();
        }
    }

    @Override
    public void handleAutomation(Boolean turnOn) {
        if (illumination != null && turnOn != null) {
            if (turnOn) {
                illumination.on();
            } else {
                illumination.off();
            }
        }
    }

}
