package org.smarthome.domain.temperature;

public class AirConditioner {
    private AirConditionerState airConditionerState;
    private int temperature;

    public AirConditioner() {
        this.airConditionerState = new AirConditionerOff(this);
    }

    public AirConditionerState getAirConditionerState() {
        return airConditionerState;
    }

    public void setAirConditionerState(AirConditionerState airConditionerState) {
        this.airConditionerState = airConditionerState;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) throws Exception {
        if(airConditionerState.getClass().equals(AirConditionerOn.class)) {
            this.temperature = temperature;
        }else {
            throw new Exception("The air conditioner is off");
        }

    }

    public void handle() {
        airConditionerState.handle();
    }
}
