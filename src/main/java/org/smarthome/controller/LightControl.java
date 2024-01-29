package org.smarthome.controller;

import org.smarthome.domain.illumination.Light;

import java.util.List;

public class LightControl {

    private final List<Light> controlledLights;

    public LightControl(List<Light> controlledLights) {
        this.controlledLights = controlledLights;
    }

    public void handleLights() {
        for (Light l : controlledLights) {
            l.handle();
        }
    }

}
