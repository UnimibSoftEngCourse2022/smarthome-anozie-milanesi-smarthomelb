package org.smarthome.domain.temperature;

public class AirConditionerOff extends AirConditionerState {

    protected AirConditionerOff(AirConditioner airConditioner) {
        super(airConditioner);
    }

    @Override
    public void handle() {
        airConditioner.on();
    }

}
