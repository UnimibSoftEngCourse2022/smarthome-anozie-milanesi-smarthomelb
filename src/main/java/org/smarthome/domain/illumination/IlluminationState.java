package org.smarthome.domain.illumination;

import org.smarthome.domain.Actuator;

public abstract class IlluminationState implements Actuator {

    protected Illumination illumination;

    protected IlluminationState(Illumination illumination) {
        this.illumination = illumination;
    }

}
