package org.smarthome.domain.temperature;

import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.ObservableElement;
import org.smarthome.simulation.RoomTemperatureSimulation;

import java.util.Objects;

import static org.smarthome.util.Constants.DEFAULT_IDEAL_TEMPERATURE;

public class AirConditioner extends ObservableElement<AirConditionerListener> {

    private int temperature;
    private AirConditionerState airConditionerState;
    private RoomTemperatureSimulation roomTemperatureSimulation;

    public AirConditioner() {
        this.temperature = DEFAULT_IDEAL_TEMPERATURE;
        this.airConditionerState = new AirConditionerOff(this);
    }

    protected RoomTemperatureSimulation getRoomTemperatureSimulation() {
        return roomTemperatureSimulation;
    }

    public void setRoomTemperatureSimulation(RoomTemperatureSimulation roomTemperatureSimulation) {
        this.roomTemperatureSimulation = roomTemperatureSimulation;
    }

    public AirConditionerState getAirConditionerState() {
        return airConditionerState;
    }

    public void setAirConditionerState(AirConditionerState airConditionerState) {
        if (!Objects.equals(getAirConditionerState().getClass(), airConditionerState.getClass())) {
            this.airConditionerState = airConditionerState;
            for (AirConditionerListener observer : observers) {
                observer.onChangeState(airConditionerState);
            }
        }
    }

    public boolean isOn() {
        return airConditionerState.getClass().equals(AirConditionerOn.class);
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        if (this.temperature != temperature) {
            if (!isOn()) {
                setAirConditionerState(new AirConditionerOn(this));
            }

            this.temperature = temperature;
            if (roomTemperatureSimulation != null) {
                roomTemperatureSimulation.setTarget(temperature);
            }

            for (AirConditionerListener observer : observers) {
                observer.onTemperatureChange(temperature);
            }
        }
    }

    public void handle() {
        airConditionerState.handle();
    }

}
