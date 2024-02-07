package org.smarthome.domain.illumination;

import org.smarthome.domain.ObservableElement;
import org.smarthome.listener.IlluminationListener;

import java.util.List;
import java.util.Objects;

public class Illumination extends ObservableElement<IlluminationListener> {

    private final List<Light> lights;
    private IlluminationState illuminationState;

    public Illumination(List<Light> lights) {
        this.lights = lights;
        illuminationState = new IlluminationOff(this);
    }

    public List<Light> getLights() {
        return lights;
    }

    public synchronized IlluminationState getIlluminationState() {
        return illuminationState;
    }

    public synchronized void setIlluminationState(IlluminationState illuminationState) {
        if (!Objects.equals(getIlluminationState().getClass(), illuminationState.getClass())) {
            this.illuminationState = illuminationState;
            for (IlluminationListener observer : observers) {
                observer.onChangeState(illuminationState);
            }
        }
    }

    public synchronized void on() {
        setIlluminationState(new IlluminationOn(this));
        for (Light light : getLights()) {
            light.setLightState(new LightOn(light));
        }
    }

    public synchronized void off() {
        setIlluminationState(new IlluminationOff(this));
        for (Light light : getLights()) {
            light.setLightState(new LightOff(light));
        }
    }

    public void handle() {
        illuminationState.handle();
    }

}
