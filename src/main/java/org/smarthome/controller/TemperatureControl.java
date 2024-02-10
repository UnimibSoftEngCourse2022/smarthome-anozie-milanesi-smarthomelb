package org.smarthome.controller;

import org.smarthome.domain.temperature.AirConditioner;

public class TemperatureControl {

    private final AirConditioner airConditioner;

    public TemperatureControl(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    public void handleAirConditioner() {
        airConditioner.handle();
    }

    public void increaseTemperature() {
        airConditioner.setTemperature(airConditioner.getTemperature() + 1);
    }

    public void decreaseTemperature() {
        airConditioner.setTemperature(airConditioner.getTemperature() - 1);
    }

}
