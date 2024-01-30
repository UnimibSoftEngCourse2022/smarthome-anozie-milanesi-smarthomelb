package org.smarthome.domain.illumination;

import java.util.Objects;

public class Light {

    private LightState lightState;

    public Light() {
        lightState = new LightOff(this);
    }

    public LightState getLightState() {
        return lightState;
    }

    public void setLightState(LightState lightState) {
        if (!Objects.equals(getLightState().getClass(), lightState.getClass())) {
            this.lightState = lightState;
        }
    }

    public void handle() {
        lightState.handle();
    }

}
