package org.smarthome.domain.illumination;

public class LightOn extends LightState {

    public LightOn(Light light) {
        super(light);
    }

    @Override
    public void handle() {
        light.setLightState(new LightOff(light));
    }

}
