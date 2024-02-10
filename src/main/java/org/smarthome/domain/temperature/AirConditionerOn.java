package org.smarthome.domain.temperature;

public class AirConditionerOn extends AirConditionerState{

    protected AirConditionerOn(AirConditioner airConditioner) {
        super(airConditioner);
    }

    @Override
    public void handle() {
        airConditioner.setAirConditionerState(new AirConditionerOff(airConditioner));
    }
}
