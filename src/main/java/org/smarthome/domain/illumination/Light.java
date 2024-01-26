package org.smarthome.domain.illumination;

public class Light {

    private LightState lightState;

    public Light() {
        lightState = new LightOff(this);
    }

    public void setLightState(LightState lightState) {
        this.lightState = lightState;
    }

    public void handle() {
        lightState.handle();
    }

}



