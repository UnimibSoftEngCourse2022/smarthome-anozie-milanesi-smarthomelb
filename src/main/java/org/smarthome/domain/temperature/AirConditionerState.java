package org.smarthome.domain.temperature;

import org.smarthome.domain.Actuator;

public abstract class AirConditionerState implements Actuator {

    protected AirConditioner airConditioner;

    protected AirConditionerState(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

}
