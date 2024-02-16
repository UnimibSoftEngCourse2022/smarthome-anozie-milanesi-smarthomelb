package org.smarthome.controller;

import org.smarthome.domain.temperature.AirConditioner;
import org.smarthome.domain.temperature.TemperaturePreference;

public class TemperatureControl extends AutomaticControl<Integer> {

    private final TemperaturePreference temperaturePreference;
    private final AirConditioner airConditioner;

    public TemperatureControl(TemperaturePreference temperaturePreference, AirConditioner airConditioner) {
        this.temperaturePreference = temperaturePreference;
        this.airConditioner = airConditioner;
    }

    public void handleAirConditioner() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.handle();
        }
    }

    public void increaseTemperature() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.setTemperature(airConditioner.getTemperature() + 1);
        }
    }

    public void decreaseTemperature() {
        if (airConditioner != null) {
            setAutomationActive(false);
            airConditioner.setTemperature(airConditioner.getTemperature() - 1);
        }
    }

    @Override
    public void handleAutomation(Integer temperature) {
        if (temperaturePreference != null && airConditioner != null && temperature != null) {
            int temperatureDifference = temperature - temperaturePreference.getIdealTemperature();

            if (Math.abs(temperatureDifference) > temperaturePreference.getThreshold()) {
                // Temperature exceeds the threshold, adjust it to the ideal temperature
                airConditioner.setTemperature(temperaturePreference.getIdealTemperature());
            } else if (temperatureDifference == 0) {
                airConditioner.off();
            }
        }
    }

}
