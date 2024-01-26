package org.smarthome.domain.illumination;

public abstract class LightState {

    protected Light light;

    public LightState(Light light) {
        this.light = light;
    }

    public abstract void handle();

}
