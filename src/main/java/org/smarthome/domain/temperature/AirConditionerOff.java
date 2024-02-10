package org.smarthome.domain.temperature;

import org.smarthome.simulation.RoomTemperatureSimulation;

public class AirConditionerOff extends AirConditionerState {

    protected AirConditionerOff(AirConditioner airConditioner) {
        super(airConditioner);
    }

    @Override
    public void handle() {
        airConditioner.setAirConditionerState(new AirConditionerOn(airConditioner));
        RoomTemperatureSimulation roomTemperatureSimulation = airConditioner.getRoomTemperatureSimulation();
        if (roomTemperatureSimulation != null) {
            roomTemperatureSimulation.setTarget(airConditioner.getTemperature());
        }
    }

}
