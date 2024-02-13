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
        if (temperature >= Constants.airConditionerBottomRangeValue() &&
                temperature <= Constants.airConditionerUpperRangeValue()) {
            if (!isOn()) {
                on();
            }

            if (this.temperature != temperature) {
                this.temperature = temperature;
                for (AirConditionerListener observer : observers) {
                    observer.onTemperatureChange(temperature);
                }

                if (roomTemperatureSimulation != null) {
                    roomTemperatureSimulation.setTarget(temperature);
                }
            }
        }
    }

    public synchronized void on() {
        setAirConditionerState(new AirConditionerOn(this));
        if (roomTemperatureSimulation != null) {
            roomTemperatureSimulation.setTarget(getTemperature());
        }
    }

    public synchronized void off() {
        setAirConditionerState(new AirConditionerOff(this));
        if (roomTemperatureSimulation != null) {
            roomTemperatureSimulation.stopTemperatureChange();
        }
    }

    public synchronized void handle() {
        airConditionerState.handle();
    }

}
