package org.smarthome.domain.illumination;

import org.smarthome.domain.ObservableElement;
import org.smarthome.listener.LightActionListener;

import java.util.Objects;

public class Light extends ObservableElement<LightActionListener> {

    private LightState lightState;

    public Light() {
        super();
        lightState = new LightOff(this);
    }

    public synchronized LightState getLightState() {
        return lightState;
    }

    public synchronized void setLightState(LightState lightState) {
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
