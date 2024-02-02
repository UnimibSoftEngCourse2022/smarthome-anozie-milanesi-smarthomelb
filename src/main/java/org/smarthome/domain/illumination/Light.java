package org.smarthome.domain.illumination;

import org.smarthome.domain.cleaning.CleaningActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Light {

    private final List<LightActionListener> observers;
    private LightState lightState;

    public Light() {
        this.observers = new ArrayList<>();
        lightState = new LightOff(this);
    }

    public void addObserver(LightActionListener observer) {
        observers.add(observer);
    }

    public void removeObserver(LightActionListener observer) {
        observers.remove(observer);
    }

    public LightState getLightState() {
        return lightState;
    }

    public void setLightState(LightState lightState) {
        if (!Objects.equals(getLightState().getClass(), lightState.getClass())) {
            this.lightState = lightState;
            for (LightActionListener observer : observers) {
                observer.onChangeState(lightState);
            }
        }
    }

    public void handle() {
        lightState.handle();
    }

}
