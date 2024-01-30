package org.smarthome.domain.illumination;

import java.util.List;
import java.util.Objects;

public class Illumination {

    private IlluminationActionListener illuminationActionListener;
    private final List<Light> lights;
    private IlluminationState illuminationState;

    public Illumination(List<Light> lights) {
        this.lights = lights;
        illuminationState = new IlluminationOff(this);
    }

    public void setIlluminationActionListener(IlluminationActionListener illuminationActionListener) {
        this.illuminationActionListener = illuminationActionListener;
    }

    public List<Light> getLights() {
        return lights;
    }

    public IlluminationState getIlluminationState() {
        return illuminationState;
    }

    public void setIlluminationState(IlluminationState illuminationState) {
        if (!Objects.equals(getIlluminationState().getClass(), illuminationState.getClass())) {
            this.illuminationState = illuminationState;
            if (illuminationActionListener != null) {
                illuminationActionListener.onChangeState(illuminationState);
            }
        }
    }

    public void handle() {
        illuminationState.handle();
    }

}
