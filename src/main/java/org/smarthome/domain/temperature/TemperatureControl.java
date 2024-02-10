package org.smarthome.domain.temperature;

public class TemperatureControl {

    private final AirConditioner airConditioner;

    public TemperatureControl(AirConditioner airConditioner) {
        this.airConditioner = airConditioner;
    }

    public void handleAirConditioner() {
        airConditioner.handle();
    }

    public int increaseTemperature() {
        return airConditioner.getTemperature() + 1;
    }

    public int decreaseTemperature() {
        return airConditioner.getTemperature() - 1;
    }



}
