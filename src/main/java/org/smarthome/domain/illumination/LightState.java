package org.smarthome.domain.illumination;

import org.smarthome.domain.Actuator;

public abstract class LightState implements Actuator {

    protected Light light;

    protected LightState(Light light) {
        this.light = light;
    }

}
