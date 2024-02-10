package org.smarthome.domain.temperature;

import org.smarthome.simulation.RoomTemperatureSimulation;

public class AirConditionerOn extends AirConditionerState {

    protected AirConditionerOn(AirConditioner airConditioner) {
        super(airConditioner);
    }

    @Override
    public void handle() {
        airConditioner.setAirConditionerState(new AirConditionerOff(airConditioner));
        RoomTemperatureSimulation roomTemperatureSimulation = airConditioner.getRoomTemperatureSimulation();
        if (roomTemperatureSimulation != null) {
            roomTemperatureSimulation.stopTemperatureChange();
        }
    }

}
