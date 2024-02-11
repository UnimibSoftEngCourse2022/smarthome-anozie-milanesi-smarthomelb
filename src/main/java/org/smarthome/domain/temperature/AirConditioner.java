package org.smarthome.domain.temperature;

import org.smarthome.listener.AirConditionerListener;
import org.smarthome.listener.ObservableElement;
import org.smarthome.simulation.RoomTemperatureSimulation;
import org.smarthome.util.Constants;

import java.util.Objects;

public class AirConditioner extends ObservableElement<AirConditionerListener> {

    private int temperature;
    private AirConditionerState airConditionerState;
    private RoomTemperatureSimulation roomTemperatureSimulation;

    public AirConditioner() {
        this.temperature = Constants.defaultIdealTemperature();
        this.airConditionerState = new AirConditionerOff(this);
    }

    protected RoomTemperatureSimulation getRoomTemperatureSimulation() {
        return roomTemperatureSimulation;
    }

    public void setRoomTemperatureSimulation(RoomTemperatureSimulation roomTemperatureSimulation) {
        this.roomTemperatureSimulation = roomTemperatureSimulation;
    }

    public synchronized AirConditionerState getAirConditionerState() {
        return airConditionerState;
    }

    public synchronized void setAirConditionerState(AirConditionerState airConditionerState) {
        if (!Objects.equals(getAirConditionerState().getClass(), airConditionerState.getClass())) {
            this.airConditionerState = airConditionerState;
            for (AirConditionerListener observer : observers) {
                observer.onChangeState(airConditionerState);
            }
        }
    }

    public synchronized boolean isOn() {
        return airConditionerState.getClass().equals(AirConditionerOn.class);
    }

    public synchronized int getTemperature() {
        return temperature;
    }

    public synchronized void setTemperature(int temperature) {
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

    public synchronized void handle() {
        airConditionerState.handle();
    }

}
