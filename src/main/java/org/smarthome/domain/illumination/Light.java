package org.smarthome.domain.illumination;

import java.util.Objects;

public class Light {

    private LightActionListener lightActionListener;
    private LightState lightState;

    public Light() {
        lightState = new LightOff(this);
    }

    public void setLightActionListener(LightActionListener lightActionListener) {
        this.lightActionListener = lightActionListener;
    }

    public LightState getLightState() {
        return lightState;
    }

    public void setLightState(LightState lightState) {
        if (!Objects.equals(getLightState().getClass(), lightState.getClass())) {
            this.lightState = lightState;
            if (lightActionListener != null) {
                lightActionListener.onChangeState(lightState);
            }
        }
    }

    public void handle() {
        lightState.handle();
    }

}
