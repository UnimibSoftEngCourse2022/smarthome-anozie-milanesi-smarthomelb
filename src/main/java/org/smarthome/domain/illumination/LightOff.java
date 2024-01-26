package org.smarthome.domain.illumination;

public class LightOff extends LightState {

    public LightOff(Light light) {
        super(light);
    }

    @Override
    public void handle() {
        light.setLightState(new LightOn(light));
    }

}
